package com.fastbee.http.hk.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BehaviorAnalysisData extends CommonEventData{
    private String targetId;
    private String behaviorType;
    private String startTime;
    private String endTime;
    private String location;
    private double confidence;
}