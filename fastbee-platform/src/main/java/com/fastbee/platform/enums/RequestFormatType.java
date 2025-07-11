package com.fastbee.platform.enums;

import lombok.Data;
import lombok.Getter;

@Getter
public enum RequestFormatType {
    /**
     * 路径参数
     */
    XML("XML"),

    /**
     * 查询参数
     */
    JSON("JSON");
    private final String value;

    RequestFormatType(String value) {
        this.value = value;
    }

    /**
     * 根据字符串值获取对应的 ParamType
     * @param value 字符串值
     * @return ParamType 对象
     */
    public static RequestFormatType fromValue(String value) {
        for (RequestFormatType formatType : values()) {
            if (formatType.getValue().equalsIgnoreCase(value)) {
                return formatType;
            }
        }
        throw new IllegalArgumentException("Unknown from type: " + value);
    }
}
