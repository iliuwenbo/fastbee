package com.fastbee.http.hk.service.impl;

import com.alibaba.fastjson2.JSON;
import com.fastbee.http.hk.properties.VideoOcclusionEventData;
import com.fastbee.http.hk.service.AlarmDataParser;

/**
 * 视频遮挡事件解析服务实现
 * 负责将原始JSON数据解析为VideoOcclusionEventData对象
 */
public class VideoOcclusionEventService implements AlarmDataParser {
    /**
     * 解析视频遮挡事件原始数据
     * @param rawData 原始JSON格式数据
     * @return 解析后的视频遮挡事件数据对象
     */
    @Override
    public VideoOcclusionEventData parse(String rawData) {
        return JSON.parseObject(rawData, VideoOcclusionEventData.class);
    }
}