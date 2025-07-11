package com.fastbee.http.stream.factory;

import com.fastbee.http.stream.enums.ManufacturerType;
import com.fastbee.http.stream.service.StreamService;
import org.springframework.stereotype.Component;

/**
 * 流服务工厂类
 */
@Component
public class StreamServiceFactory {

    /**
     * 根据厂商类型获取流服务
     * @param manufacturerType 厂商类型枚举
     * @return 对应的流服务实例
     * @throws IllegalArgumentException 如果厂商类型为空或不支持
     */
    public StreamService getServiceByManufacturer(ManufacturerType manufacturerType) {
        if (manufacturerType == null) {
            throw new IllegalArgumentException("厂商类型不能为空");
        }
        return manufacturerType.getService();
    }

    /**
     * 根据设备ID获取流服务
     * @param deviceId 设备ID
     * @return 对应的流服务实例
     */
    public StreamService getServiceByDeviceId(String deviceId) {
        return ManufacturerType.fromDeviceId(deviceId).getService();
    }
}