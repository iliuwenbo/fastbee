package com.fastbee.http.stream.api;

import com.dtflys.forest.annotation.*;

@BaseRequest(baseURL = "${stream.dahua.apiUrl}")
public interface DahuaApiClient {
    @Get("/streamInfo")
    Object getStreamInfo(@Query("deviceId") String deviceId,
                                    @Query("username") String username);
}
