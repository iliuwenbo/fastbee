package com.fastbee.http.hk.service.impl;

import com.fastbee.http.hk.properties.GpsCollectionData;
import com.fastbee.http.hk.service.AlarmDataParser;

public class GpsCollectionService implements AlarmDataParser {
    @Override
    public GpsCollectionData parse(String rawData) {
        GpsCollectionData data = new GpsCollectionData();
        // 解析原始数据逻辑
        return data;
    }
}    