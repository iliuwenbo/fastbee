package com.fastbee.http.stream.enums;

import com.fastbee.common.utils.spring.SpringUtils;
import com.fastbee.http.stream.service.StreamService;
import com.fastbee.http.stream.service.impl.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 视频设备厂商类型枚举
 *
 * <p>用于标识不同厂商的设备类型，并提供对应的流服务获取</p>
 */
public enum ManufacturerType {
    /**
     * 海康威视设备
     */
    HIKVISION("HK", "海康威视", HikvisionStreamService.class),

    /**
     * 大华设备
     */
    DAHUA("DH", "大华", null),

    /**
     * 宇视设备
     */
    UNIVIEW("UV", "宇视", null),

    /**
     * 天地伟业设备
     */
    TIANDI("TD", "天地伟业", null);

    /**
     * -- GETTER --
     *  获取设备ID前缀
     *
     */
    @Getter
    private final String prefix;          // 设备ID前缀
    /**
     * -- GETTER --
     *  获取厂商名称
     *
     */
    @Getter
    private final String manufacturerName; // 厂商名称
    private final Class<? extends StreamService> serviceClass; // 对应的服务类

    /**
     * 构造函数
     * @param prefix 设备ID前缀
     * @param manufacturerName 厂商名称
     * @param serviceClass 对应的流服务类
     */
    ManufacturerType(String prefix, String manufacturerName, Class<? extends StreamService> serviceClass) {
        this.prefix = prefix;
        this.manufacturerName = manufacturerName;
        this.serviceClass = serviceClass;
    }

    /**
     * 根据设备ID获取厂商类型
     * @param deviceId 设备ID
     * @return 对应的厂商类型
     * @throws IllegalArgumentException 如果设备ID不匹配任何厂商类型
     */
    public static ManufacturerType fromDeviceId(String deviceId) {
        if (deviceId == null || deviceId.isEmpty()) {
            throw new IllegalArgumentException("设备ID不能为空");
        }

        for (ManufacturerType type : values()) {
            if (deviceId.startsWith(type.prefix)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知设备类型: " + deviceId);
    }

    /**
     * 根据厂商编码获取厂商类型(用于JSON反序列化)
     * @param code 厂商编码
     * @return 对应的厂商类型
     */
    @JsonCreator
    public static ManufacturerType fromCode(String code) {
        for (ManufacturerType type : values()) {
            if (type.prefix.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知厂商编码: " + code);
    }

    /**
     * 获取厂商编码(用于JSON序列化)
     * @return 厂商编码
     */
    @JsonValue
    public String getCode() {
        return prefix;
    }

    /**
     * 获取对应的流服务实例
     * @return 流服务实例
     * @throws UnsupportedOperationException 如果该厂商服务未实现
     */
    public StreamService getService() {
        if (serviceClass == null) {
            throw new UnsupportedOperationException(manufacturerName + "流服务暂未实现");
        }
        return SpringUtils.getBean(serviceClass);
    }

    /**
     * 检查该厂商服务是否已实现
     * @return 是否已实现
     */
    public boolean isServiceImplemented() {
        return serviceClass != null;
    }
}