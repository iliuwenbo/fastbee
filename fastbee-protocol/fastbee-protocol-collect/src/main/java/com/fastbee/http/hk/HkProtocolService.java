package com.fastbee.http.hk;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fastbee.common.annotation.SysProtocol;
import com.fastbee.common.constant.FastBeeConstant;
import com.fastbee.common.core.mq.DeviceReport;
import com.fastbee.common.core.mq.MQSendMessageBo;
import com.fastbee.common.core.mq.message.DeviceData;
import com.fastbee.common.core.mq.message.FunctionCallBackBo;
import com.fastbee.common.core.thingsModel.ThingsModelSimpleItem;
import com.fastbee.common.exception.ServiceException;
import com.fastbee.common.exception.base.BaseException;
import com.fastbee.common.utils.DateUtils;
import com.fastbee.http.hk.properties.CommonEventData;
import com.fastbee.http.hk.utils.HkParsingUtil;
import com.fastbee.iot.model.ThingsModels.ValueItem;
import com.fastbee.protocol.base.protocol.IProtocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author gsb
 * @date 2022/10/10 16:55
 */
@Slf4j
@Component
@SysProtocol(name = "海康开放平台解析协议", protocolCode = FastBeeConstant.PROTOCOL.HIKVISION, description = "海康开放平台解析协议")
public class HkProtocolService implements IProtocol {

    /**
     * 上报数据格式 <p>
     * [
     *   {
     *     "id": "switch",
     *     "value": "0"
     *   },
     *   {
     *     "id": "gear",
     *     "value": "0"
     *   }
     * ]
     */
    @Override
    public DeviceReport decode(DeviceData deviceData, String clientId) {
        try {
            // bytep[] 转String
            String rawData = new String(deviceData.getData(), StandardCharsets.UTF_8);
            String data = getParseDeviceData(rawData);
            DeviceReport reportMessage = new DeviceReport();
            List<ThingsModelSimpleItem> values = JSON.parseArray(data, ThingsModelSimpleItem.class);
            //上报数据时间
            for (ThingsModelSimpleItem value : values) {
                value.setTs(DateUtils.getNowDate());
            }
            reportMessage.setThingsModelSimpleItem(values);
            reportMessage.setClientId(clientId);
            reportMessage.setSerialNumber(clientId);
            reportMessage.setSources(data);
            return reportMessage;
        } catch (Exception e) {
            throw new ServiceException("数据解析异常" + e);
        }
    }


    /**
     * 下发 [{"id":"switch","value":"0","remark":""}]
     *
     * @param message
     * @return
     */
    @Override
    public FunctionCallBackBo encode(MQSendMessageBo message) {
        try {
            FunctionCallBackBo callBack = new FunctionCallBackBo();
            JSONObject params = message.getParams();
            ValueItem valueItem = new ValueItem();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                valueItem.setId(entry.getKey());
                valueItem.setValue(entry.getValue() + "");
                valueItem.setRemark("");
            }
            String msg = "[" + JSONObject.toJSONString(valueItem) + "]";
            callBack.setSources(msg);
            callBack.setMessage(msg.getBytes());
            return callBack;
        } catch (Exception e) {
            log.error("=>指令编码异常,device={},data={}", message.getSerialNumber(),
                    message.getParams());
            return null;
        }
    }

    private String getParseDeviceData(String rawData){
        try {
            // 处理报警数据
            CommonEventData commonEventData = JSON.parseObject(rawData, CommonEventData.class);
            int alarmCode = commonEventData.getParams().getEvents().get(0).getEventType();
            Object data = HkParsingUtil.parseDeviceData(String.valueOf(alarmCode), rawData);
            return "";
        }catch (Exception e){
            throw new BaseException("数据结构解析异常！");
        }

    }

}
