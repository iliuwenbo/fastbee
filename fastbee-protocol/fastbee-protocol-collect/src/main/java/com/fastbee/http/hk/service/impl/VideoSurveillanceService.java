package com.fastbee.http.hk.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fastbee.http.hk.enums.HkAlarmType;
import com.fastbee.http.hk.properties.VideoSurveillanceData;
import com.fastbee.http.hk.service.AlarmDataParser;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class VideoSurveillanceService implements AlarmDataParser {
    private static final DateTimeFormatter ISO_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ROOT);

    @Override
    public VideoSurveillanceData parse(String rawData) {
        try {
            // 尝试解析JSON格式数据
            JSONObject json = JSON.parseObject(rawData);

            // 验证基础结构
            if (!"OnEventNotify".equals(json.getString("method"))) {
                throw new IllegalArgumentException("无效的事件通知格式");
            }

            JSONObject params = json.getJSONObject("params");
            JSONArray events = params.getJSONArray("events");

            if (events == null || events.isEmpty()) {
                throw new IllegalArgumentException("事件列表为空");
            }

            // 处理第一个事件（通常只有一个）
            JSONObject event = events.getJSONObject(0);
            VideoSurveillanceData data = new VideoSurveillanceData();

            // 解析基础信息
            data.setDeviceId(event.getString("srcIndex"));
            data.setEventTime(parseIsoDateTime(event.getString("happenTime")));
            data.setConfidence(1.0); // 默认为100%置信度

            // 根据eventType映射到枚举
            int eventTypeCode = event.getIntValue("eventType");
            HkAlarmType alarmType = HkAlarmType.getByCode(String.valueOf(eventTypeCode)); // 默认使用视频丢失

            data.setEventType(alarmType.getText());

            // 解析状态
            int status = event.getIntValue("status");
            data.setStatusDescription(mapStatusToString(status));

            return data;
        } catch (Exception e) {
            // 回退到旧的解析逻辑（如果不是JSON格式）
            return parseLegacyFormat(rawData);
        }
    }

    private String parseIsoDateTime(String isoString) {
        try {
            // 处理可能的空格问题
            String cleanString = isoString.replace(" ", "");
            Instant instant = Instant.from(ISO_FORMAT.parse(cleanString));
            return String.valueOf(instant.toEpochMilli());
        } catch (Exception e) {
            // 解析失败时返回当前时间
            return String.valueOf(System.currentTimeMillis());
        }
    }

    private String mapStatusToString(int status) {
        switch (status) {
            case 0: return "瞬时";
            case 1: return "开始";
            case 2: return "停止";
            case 3: return "事件脉冲";
            case 4: return "事件联动结果更新";
            case 5: return "异步图片上传";
            default: return "未知状态";
        }
    }

    private VideoSurveillanceData parseLegacyFormat(String rawData) {
        // 旧的解析逻辑（保持兼容性）
        VideoSurveillanceData data = new VideoSurveillanceData();
        data.setDeviceId(rawData.substring(0, 16));
        data.setEventTime(rawData.substring(16, 32));
        data.setEventType(rawData.substring(32, 40));
        data.setConfidence(Double.parseDouble(rawData.substring(40)));
        return data;
    }
}    