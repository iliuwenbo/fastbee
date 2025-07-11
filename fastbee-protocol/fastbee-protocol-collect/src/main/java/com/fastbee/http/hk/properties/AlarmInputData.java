package com.fastbee.http.hk.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AlarmInputData extends CommonEventData{
    private String inputId;
    private boolean status;
    private String triggerTime;
    private String alarmLevel;
}