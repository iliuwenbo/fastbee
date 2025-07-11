package com.fastbee.http.hk.service.impl;

import com.alibaba.fastjson2.JSON;
import com.fastbee.http.hk.properties.VideoLossEventData;
import com.fastbee.http.hk.service.AlarmDataParser;

/**
 * 视频丢失事件解析服务实现
 * 负责将原始JSON数据解析为VideoLossEventData对象
 */
public class VideoLossEventService implements AlarmDataParser {
    /**
     * 解析视频丢失事件原始数据
     * @param rawData 原始JSON格式数据
     * @return 解析后的视频丢失事件数据对象
     */
    @Override
    public VideoLossEventData parse(String rawData) {
        return JSON.parseObject(rawData, VideoLossEventData.class);
    }
}