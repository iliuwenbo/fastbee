package com.fastbee.http.hk.properties;

import lombok.Data;

import java.util.List;

/**
 * 事件数据的公共基类，包含所有类型事件共有的属性
 * 采用继承结构设计，具体事件类型可扩展此类
 */
@Data
public class CommonEventData {
    /** 事件通知方法，固定为"OnEventNotify" */
    private String method;

    /** 事件参数，包含事件详情 */
    private Params params;

    /**
     * 事件参数内部类
     */
    @Data
    public static class Params {
        /** 事件能力类型，如"event_vss"表示视频监控事件 */
        private String ability;

        /** 事件列表，通常包含一个或多个事件 */
        private List<Event> events;

        /** 事件发送时间 */
        private String sendTime;
    }

    /**
     * 事件详情内部类，包含具体事件的核心信息
     */
    @Data
    public static class Event {
        /** 事件唯一标识ID */
        private String eventId;

        /** 事件类型编码，对应HkAlarmType枚举中的code */
        private int eventType;

        /** 事件发生时间，ISO 8601格式 */
        private String happenTime;

        /** 事件源设备索引 */
        private String srcIndex;

        /** 事件源设备名称 */
        private String srcName;

        /** 事件源设备的父设备索引 */
        private String srcParentIndex;

        /** 事件源设备类型，如"camera"表示摄像机 */
        private String srcType;

        /** 事件状态：0-瞬时，1-开始，2-停止，3-事件脉冲等 */
        private int status;

        /** 事件超时时间（秒） */
        private int timeout;
    }
}