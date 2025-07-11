package com.fastbee.sip.service;

import com.fastbee.sip.model.RecordList;
import com.fastbee.sip.model.ZlmMediaServer;

public interface ISipCacheService {
    Long getCSEQ(String serverSipId);

    void updateMediaInfo(ZlmMediaServer mediaServerConfig);

    void setRecordList(String key, RecordList recordList);
}
