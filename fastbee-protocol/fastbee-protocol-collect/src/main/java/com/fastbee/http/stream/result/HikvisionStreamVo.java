package com.fastbee.http.stream.result;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class HikvisionStreamVo extends StreamVo {
    private String code;
    private String msg;
    private DataDTO data;

    @Data
    public static class DataDTO {
        private String url;
    }
}