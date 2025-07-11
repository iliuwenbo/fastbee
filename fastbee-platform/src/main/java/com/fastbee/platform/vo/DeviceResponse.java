package com.fastbee.platform.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class DeviceResponse {
    @JSONField(name = "id")
    private String deviceId; // 接口中的设备 ID
    @JSONField(name = "manufacturer")
    private String manufacturerName; // 厂商名称（若接口返回 ID，可改为 manufacturerId）
    @JSONField(name = "name")
    private String deviceName;
    @JSONField(name = "type")
    private Integer deviceType;
    @JSONField(name = "ip")
    private String ipAddress;
    @JSONField(name = "isEnable")
    private Boolean isEnabled; // 接口中的启用状态，可映射为在线状态
    // 其他接口字段...
}
