package com.fastbee.http.hk.service.impl;

import com.fastbee.http.hk.properties.ViewingAreaData;
import com.fastbee.http.hk.service.AlarmDataParser;

public class ViewingAreaService implements AlarmDataParser {
    @Override
    public ViewingAreaData parse(String rawData) {
        ViewingAreaData data = new ViewingAreaData();
        // 解析原始数据逻辑
        return data;
    }
}    