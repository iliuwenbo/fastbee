package com.fastbee.http.hk.properties;

import lombok.Data;

@Data
public class VideoNetworkManagementData {
    private String deviceId;
    private String deviceName;
    private String ipAddress;
    private String status;
    private String offlineTime;
    private String lastOnlineTime;

}    