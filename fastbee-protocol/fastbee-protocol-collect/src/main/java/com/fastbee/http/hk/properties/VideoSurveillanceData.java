package com.fastbee.http.hk.properties;

import lombok.Data;

@Data
public class VideoSurveillanceData {
    private String deviceId;
    private String eventTime;
    private String eventType;
    private double confidence;
    private String statusDescription; // 事件状态描述
    private String eventId;           // 事件唯一标识
    private int eventLevel;           // 事件等级（0-未配置，1-低，2-中，3-高）
}    