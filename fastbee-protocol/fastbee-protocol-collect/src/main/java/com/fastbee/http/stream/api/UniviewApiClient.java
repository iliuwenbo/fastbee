package com.fastbee.http.stream.api;

import com.dtflys.forest.annotation.*;

@BaseRequest(baseURL = "${stream.uniview.apiUrl}")
public interface UniviewApiClient {
    @Get("/stream")
    Object getStream(@Query("deviceId") String deviceId,
                                  @Query("sessionToken") String sessionToken);
}
