package com.fastbee.http.hk.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GpsCollectionData extends CommonEventData{
    private String deviceId;
    private double latitude;
    private double longitude;
    private double altitude;
    private double speed;
    private String collectionTime;

}    