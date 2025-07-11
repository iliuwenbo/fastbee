package com.fastbee.http.hk.service;

/**
 * 报警数据解析器接口
 * 定义了解析原始报警数据的统一方法
 */
public interface AlarmDataParser {
    /**
     * 解析原始报警数据
     * @param rawData 原始数据字符串，通常为JSON格式
     * @return 解析后的事件数据对象
     */
    Object parse(String rawData);
}