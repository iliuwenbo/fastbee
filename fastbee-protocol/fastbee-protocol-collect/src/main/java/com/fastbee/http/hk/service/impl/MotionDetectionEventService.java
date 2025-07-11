package com.fastbee.http.hk.service.impl;

import com.alibaba.fastjson2.JSON;
import com.fastbee.http.hk.properties.MotionDetectionEventData;
import com.fastbee.http.hk.service.AlarmDataParser;

/**
 * 移动监测事件解析服务实现
 * 负责将原始JSON数据解析为MotionDetectionEventData对象
 */
public class MotionDetectionEventService implements AlarmDataParser {
    /**
     * 解析移动监测事件原始数据
     * @param rawData 原始JSON格式数据
     * @return 解析后的移动监测事件数据对象
     */
    @Override
    public MotionDetectionEventData parse(String rawData) {
        return JSON.parseObject(rawData, MotionDetectionEventData.class);
    }
}