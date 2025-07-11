package com.fastbee.http.hk.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EnvironmentMonitoringData extends CommonEventData{
    private String deviceId;
    private String eventType;
    private double value;
    private String unit;
    private String detectionTime;

}    