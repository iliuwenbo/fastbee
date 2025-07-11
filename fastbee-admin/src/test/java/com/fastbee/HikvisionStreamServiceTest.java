package com.fastbee;

import cn.hutool.json.JSONUtil;
import com.fastbee.http.stream.dto.HikvisionStreamRequest;
import com.fastbee.http.stream.enums.ManufacturerType;
import com.fastbee.http.stream.factory.StreamServiceFactory;
import com.fastbee.http.stream.result.StreamVo;
import com.fastbee.http.stream.service.StreamService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
public class HikvisionStreamServiceTest {

    @Autowired
    private StreamServiceFactory factory;

    @Test
    public void testBasicFunctionality() {
        // 测试从编码获取枚举
        System.out.println("测试编码转换:");
        System.out.println("HK -> " + ManufacturerType.fromCode("HK"));
        System.out.println("DH -> " + ManufacturerType.fromCode("DH"));

        // 测试从设备ID获取枚举
        System.out.println("\n测试设备ID转换:");
        System.out.println("HK-123 -> " + ManufacturerType.fromDeviceId("HK-123"));
        System.out.println("DH-456 -> " + ManufacturerType.fromDeviceId("DH-456"));

        // 测试枚举值输出
        System.out.println("\n测试枚举值输出:");
        for (ManufacturerType type : ManufacturerType.values()) {
            System.out.println(type.name() + " -> 编码: " + type.getCode());
        }

        // 简单断言验证
    }

    @Test
    public void testServiceRetrieval() {
        System.out.println("\n测试服务获取:");
        try {
            StreamService hikService = ManufacturerType.HIKVISION.getService();
            System.out.println("海康服务获取成功: " + hikService.getClass().getSimpleName());
            StreamVo streamInfo = hikService.getStreamInfo("", null);
            System.out.println("海康数据获取成功: " + JSONUtil.toJsonStr(streamInfo));
        } catch (Exception e) {
            System.out.println("海康服务获取失败: " + e.getMessage());
        }
    }

    @Test
    public void testServiceRetrieval2() {
        System.out.println("\n测试服务获取:");
        try {
            StreamService serviceByManufacturer = factory.getServiceByManufacturer(ManufacturerType.HIKVISION);
            StreamVo streamInfo = serviceByManufacturer.getStreamInfo("", null);
            System.out.println("海康数据获取成功: " + JSONUtil.toJsonStr(streamInfo));
        } catch (Exception e) {
            System.out.println("海康服务获取失败: " + e.getMessage());
        }
    }

}

