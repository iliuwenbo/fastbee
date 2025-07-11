package com.fastbee.http.hk.service.impl;

import com.fastbee.http.hk.properties.BehaviorAnalysisData;
import com.fastbee.http.hk.service.AlarmDataParser;
import org.springframework.stereotype.Service;

@Service
public class BehaviorAnalysisService implements AlarmDataParser {
    @Override
    public BehaviorAnalysisData parse(String rawData) {
        BehaviorAnalysisData data = new BehaviorAnalysisData();
        // 解析原始数据逻辑
        return data;
    }
}    