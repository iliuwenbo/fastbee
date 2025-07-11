package com.fastbee.platform.enums;

import lombok.Getter;

@Getter
public enum ParamType {
    /**
     * 路径参数
     */
    PATH("path"),

    /**
     * 查询参数
     */
    QUERY("query"),

    /**
     * 请求头参数
     */
    HEADER("header"),

    /**
     * 请求体参数
     */
    BODY("body"),
    /**
     * 返回
     */
    RETURN("return");

    private final String value;

    ParamType(String value) {
        this.value = value;
    }

    /**
     * 根据字符串值获取对应的 ParamType
     * @param value 字符串值
     * @return ParamType 对象
     */
    public static ParamType fromValue(String value) {
        for (ParamType paramType : values()) {
            if (paramType.getValue().equalsIgnoreCase(value)) {
                return paramType;
            }
        }
        throw new IllegalArgumentException("Unknown param type: " + value);
    }
}
