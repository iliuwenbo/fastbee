package com.fastbee.http.stream.dto;

import com.fastbee.http.stream.enums.ManufacturerType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 流请求数据传输对象
 */
@Data
public class StreamDTO {

    /**
     * 设备唯一标识
     */
    @NotBlank(message = "设备ID不能为空")
    private String deviceId;

    /**
     * 设备厂商类型
     */
    @NotNull(message = "厂商类型不能为空")
    private ManufacturerType manufacturerType;

    /**
     * 厂商特有参数
     */
    private Map<String, Object> params;
}