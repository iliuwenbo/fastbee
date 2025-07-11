package com.fastbee.http.hk.properties;

import lombok.Data;

@Data
public class ViewingAreaData {
    private String areaId;
    private double occupancyRate;
    private int personCount;
    private String detectionTime;

}    