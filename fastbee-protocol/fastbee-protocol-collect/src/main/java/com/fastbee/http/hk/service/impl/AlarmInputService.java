package com.fastbee.http.hk.service.impl;

import com.fastbee.http.hk.properties.AlarmInputData;
import com.fastbee.http.hk.service.AlarmDataParser;

public class AlarmInputService implements AlarmDataParser {
    @Override
    public AlarmInputData parse(String rawData) {
        AlarmInputData data = new AlarmInputData();
        // 解析原始数据逻辑
        return data;
    }
}    