package com.fastbee.http.hk.enums;

import com.fastbee.common.exception.base.BaseException;
import com.fastbee.http.hk.service.AlarmDataParser;
import com.fastbee.http.hk.service.impl.*;
import lombok.Getter;

/**
 * 海康报警类型枚举
 * 定义了所有支持的报警类型及其对应的解析服务工厂
 */
@Getter
public enum HkAlarmType {
    // 视频监控类
    VIDEO_LOSS("131329", "视频丢失", new VideoLossEventService()),
    VIDEO_OCCLUSION("131330", "视频遮挡", new VideoOcclusionEventService()),
    MOTION_DETECTION("131331", "移动侦测", new MotionDetectionEventService()),
    SCENE_CHANGE("131612", "场景变更", new VideoSurveillanceService()),
    OUT_OF_FOCUS("131613", "虚焦", new VideoSurveillanceService()),

    // 报警输入类
    ALARM_INPUT("589825", "报警输入", new AlarmInputService()),

    // 可视域事件类
    VIEWING_AREA_EVENT("196355", "可视域事件", new ViewingAreaService()),

    // GPS采集类
    GPS_COLLECTION("851969", "GPS采集", new GpsCollectionService()),

    // 行为分析类
    REGION_INTRUSION("131588", "区域入侵", new BehaviorAnalysisService()),
    CROSS_LINE_DETECTION("131585", "越界侦测", new BehaviorAnalysisService()),
    ENTER_REGION("131586", "进入区域", new BehaviorAnalysisService()),
    LEAVE_REGION("131587", "离开区域", new BehaviorAnalysisService()),
    LOITERING_DETECTION("131590", "徘徊侦测", new BehaviorAnalysisService()),
    PERSONNEL_GATHERING("131593", "人员聚集", new BehaviorAnalysisService()),
    RAPID_MOVEMENT("131592", "快速移动", new BehaviorAnalysisService()),
    PARKING_DETECTION("131591", "停车侦测", new BehaviorAnalysisService()),
    OBJECT_LEFT("131594", "物品遗留", new BehaviorAnalysisService()),
    OBJECT_TAKEN("131595", "物品拿取", new BehaviorAnalysisService()),
    ABNORMAL_PEOPLE_COUNT("131664", "人数异常", new BehaviorAnalysisService()),
    ABNORMAL_DISTANCE("131665", "间距异常", new BehaviorAnalysisService()),
    VIOLENT_MOTION("131596", "剧烈运动", new BehaviorAnalysisService()),
    POST_DUTY("131603", "岗位值守", new BehaviorAnalysisService()),
    FALL_DOWN("131605", "倒地", new BehaviorAnalysisService()),
    CLIMBING("131597", "攀高", new BehaviorAnalysisService()),
    KEY_TARGET_STAND_UP("131610", "重点目标起身", new BehaviorAnalysisService()),
    PERSON_STANDING("131666", "人员站立", new BehaviorAnalysisService()),
    WIND_FARM_STAY("131609", "防风场滞留", new BehaviorAnalysisService()),
    STAND_UP("131598", "起身", new BehaviorAnalysisService()),
    PERSON_NEAR_ATM("131599", "人靠近ATM", new BehaviorAnalysisService()),
    OPERATION_TIMEOUT("131600", "操作超时", new BehaviorAnalysisService()),
    POST_NOTE("131601", "贴纸条", new BehaviorAnalysisService()),
    INSTALL_CARD_READER("131602", "安装读卡器", new BehaviorAnalysisService()),
    TAILGATING("131604", "尾随", new BehaviorAnalysisService()),
    SOUND_INTENSITY_CHANGE("131606", "声强突变", new BehaviorAnalysisService()),
    BROKEN_LINE_CLIMBING("131607", "折线攀高", new BehaviorAnalysisService()),
    BROKEN_LINE_WARNING("131611", "折线警戒面", new BehaviorAnalysisService()),

    // 环境监测类
    TEMPERATURE_DIFFERENCE_ALARM("192518", "温差报警", new EnvironmentMonitoringService()),
    TEMPERATURE_ALARM("192517", "温度报警", new EnvironmentMonitoringService()),
    VESSEL_DETECTION("192516", "船只检测", new EnvironmentMonitoringService()),
    FIRE_DETECTION("192515", "火点检测", new EnvironmentMonitoringService()),
    SMOKE_FIRE_DETECTION("192514", "烟火检测", new EnvironmentMonitoringService()),
    SMOKE_DETECTION("192513", "烟雾检测", new EnvironmentMonitoringService()),

    // 视频网管类
    MONITOR_OFFLINE("88919654", "监控点离线", new VideoNetworkManagementService());

    /** 报警类型编码，与设备返回的eventType对应 */
    private final String code;

    /** 报警类型文本描述 */
    private final String text;
    private final AlarmDataParser parser;

    HkAlarmType(String code, String text,AlarmDataParser parser) {
        this.code = code;
        this.text = text;
        this.parser = parser;
    }

    public static HkAlarmType getByCode(String code) {
        for (HkAlarmType value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        throw new BaseException("报警类型枚举格式不存在！");
    }

    public static AlarmDataParser getByParser(String code) {
        for (HkAlarmType value : values()) {
            if (value.getCode().equals(code)) {
                return value.getParser();
            }
        }
        throw new BaseException("报警类型枚举格式不存在！");
    }

}