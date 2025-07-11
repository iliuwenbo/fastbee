package com.fastbee.platform.vo;

import lombok.Data;

import java.util.Map;

/**
 * API 请求详细信息
 */
@Data
public class ApiRequestDetails {
    /** 接口 ID */
    private Long id;

    private String apiThirdPartyPlatformId;

    /** 接口名称 */
    private String apiName;

    /** 接口 URL */
    private String apiUrl;

    /** 接口类型 */
    private String apiType;

    /** 请求方法 path query header body returnData */
    private String method;

    /** 请求体格式（xml 或 json） */
    private String requestFormat;

    /** 备注 */
    private String remark;
    /**
     * 路径参数
     */
    private String returnType;
    /**
     * 路径参数
     */
    private Map<String, Object> path;

    /**
     * 查询参数
     */
    private Map<String, Object> query;

    /**
     * 请求头参数
     */
    private Map<String, Object> header;

    /**
     * 请求体参数
     */
    private Map<String, Object> body;

    /**
     * 返回参数
     */
    private Map<String, Object> returnData;
}

