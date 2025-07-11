package com.fastbee;

import cn.hutool.json.JSONUtil;
import com.fastbee.common.exception.base.BaseException;
import com.fastbee.http.hk.properties.CommonEventData;
import com.fastbee.iot.domain.Device;
import com.fastbee.iot.model.DeviceMqttConnectVO;
import com.fastbee.iot.service.IDeviceService;
import com.fastbee.mqttclient.PubMqttClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class HkServiceTest {
    @Resource
    private IDeviceService deviceService;
    @Resource
    private PubMqttClient pubMqttClient;

    @Test
    public void execute() {
        String msg = "{\n" +
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

