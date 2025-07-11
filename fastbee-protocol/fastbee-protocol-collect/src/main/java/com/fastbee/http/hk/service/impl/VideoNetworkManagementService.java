package com.fastbee.http.hk.service.impl;

import com.fastbee.http.hk.properties.VideoNetworkManagementData;
import com.fastbee.http.hk.service.AlarmDataParser;

public class VideoNetworkManagementService implements AlarmDataParser {
    @Override
    public VideoNetworkManagementData parse(String rawData) {
        VideoNetworkManagementData data = new VideoNetworkManagementData();
        // 解析原始数据逻辑
        return data;
    }
}    