package com.fastbee.hk;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fastbee.common.core.domain.AjaxResult;
import com.fastbee.common.exception.base.BaseException;
import com.fastbee.http.hk.properties.CommonEventData;
import com.fastbee.iot.domain.Device;
import com.fastbee.iot.model.DeviceMqttConnectVO;
import com.fastbee.iot.service.IDeviceService;
import com.fastbee.mqttclient.PubMqttClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/hk")
@Api(tags = "API 接口定义")
public class HkController {
    @Resource
    private IDeviceService deviceService;
    @Resource
    private PubMqttClient pubMqttClient;
    /**
     * 获取平台接入设备中间详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:device:query')")
    @GetMapping(value = "/url")
    @ApiOperation("获取平台接入设备中间详细信息")
    public void getInfo(@RequestBody String msg) {
        if (StrUtil.isBlank(msg)) {
            msg = "{\n" +
                    "\t\"method\": \"OnEventNotify\",\n" +
                    "\t\"params\": {\n" +
                    "\t\t\"ability\": \"event_vss\",\n" +
                    "\t\t\"events\": [\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t\"eventId\": \"0BEC42FF-105F-6D43-AC4A-C37F00A387B2\",\n" +
                    "\t\t\t\t\"eventType\": 131330,\n" +
                    "\t\t\t\t\"happenTime\": \"2018-08-14T09: 49: 10.953+08: 00\",\n" +
                    "\t\t\t\t\"srcIndex\": \"3dc5f0fb06b94891a6ee71c5186f3627\",\n" +
                    "\t\t\t\t\"srcName\": \"\",\n" +
                    "\t\t\t\t\"srcParentIndex\": \"07d70dc6968e4a1b8c5a79b6b49e7d57\",\n" +
                    "\t\t\t\t\"srcType\": \"camera\",\n" +
                    "\t\t\t\t\"status\": 1,\n" +
                    "\t\t\t\t\"timeout\": 30\n" +
                    "\t\t\t}\n" +
                    "\t\t],\n" +
                    "\t\t\"sendTime\": \"2018-08-14T09: 49: 10.954+08: 00\"\n" +
                    "\t}\n" +
                    "}\n";
        }
        CommonEventData commonEventData = JSONUtil.toBean(msg, CommonEventData.class);
        if (!commonEventData.getMethod().equals("OnEventNotify")) {
            throw new BaseException("非事件回调通知方法！");
        }
        CommonEventData.Params params = commonEventData.getParams();
        String ability = params.getAbility();
        if ("event_vss".equals(ability)) {
            for (CommonEventData.Event event : params.getEvents()) {
                // 根据eventType映射到枚举
                String srcIndex = event.getSrcIndex();
                Device device = deviceService.selectDeviceBySerialNumber(srcIndex);
                if (device == null) {
                    throw new BaseException("设备不存在！");
                }
                DeviceMqttConnectVO mqttConnectData = deviceService.getMqttConnectData(device.getDeviceId());
                if (mqttConnectData == null) {
                    throw new BaseException("Mqtt连接参数不存在！");
                }
                pubMqttClient.publish(1,false,mqttConnectData.getReportTopic(),msg);
            }
        }
    }
}
