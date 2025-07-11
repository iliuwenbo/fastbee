package com.fastbee.http.hk.utils;


import cn.hutool.json.JSONUtil;
import com.fastbee.http.hk.factory.HkParserFactory;
import com.fastbee.http.hk.service.AlarmDataParser;

// 设备解析工具类
public class HkParsingUtil {
    public static Object parseDeviceData(String code, String jsonData) {

        AlarmDataParser parser = HkParserFactory.getParser(code);
        if (parser != null) {
            return JSONUtil.toJsonStr(parser.parse(jsonData));
        }
        return null;
    }
}
