package com.fastbee.http.hk.factory;

import com.fastbee.http.hk.enums.HkAlarmType;
import com.fastbee.http.hk.service.AlarmDataParser;

/**
 * 解析器工厂
 */
public class HkParserFactory {

    public static AlarmDataParser getParser(String code) {
        return HkAlarmType.getByParser(code);
    }
}
