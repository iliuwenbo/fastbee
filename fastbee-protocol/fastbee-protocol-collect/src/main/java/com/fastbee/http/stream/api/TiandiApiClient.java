package com.fastbee.http.stream.api;

import com.dtflys.forest.annotation.*;

@BaseRequest(baseURL = "${stream.tiandi.apiUrl}")
public interface TiandiApiClient {
    @Get("/stream")
    Object getStream(@Query("deviceId") String deviceId,
                                 @Query("apiKey") String apiKey);
}
