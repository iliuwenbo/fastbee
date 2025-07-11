package com.fastbee.http.stream.api;

import com.dtflys.forest.annotation.*;
import com.fastbee.http.stream.dto.HikvisionStreamRequest;
import com.fastbee.http.stream.result.HikvisionStreamVo;

import java.util.Map;

@BaseRequest(baseURL = "{hkBaseUrl}")
public interface HikvisionApiClient {
    @Post
    HikvisionStreamVo getStreamUrl(@JSONBody HikvisionStreamRequest request, @Header Map<String,String> headerMap);
}
