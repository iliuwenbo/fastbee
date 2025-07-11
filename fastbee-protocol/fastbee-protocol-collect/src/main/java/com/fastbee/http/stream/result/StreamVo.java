package com.fastbee.http.stream.result;

import lombok.Data;
import java.util.Map;

@Data
public class StreamVo {
    private String deviceId;
    private String url;
    private Map<String, Object> extra; // 特有返回参数
}
