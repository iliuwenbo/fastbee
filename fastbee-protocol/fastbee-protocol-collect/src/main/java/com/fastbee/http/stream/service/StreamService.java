package com.fastbee.http.stream.service;

import com.fastbee.http.stream.result.StreamVo;

import java.util.Map;

public interface StreamService {
    StreamVo getStreamInfo(String deviceId, Map<String, Object> params);
}
