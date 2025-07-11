package com.fastbee.platform.utils;

import cn.hutool.core.bean.BeanUtil;
import com.fastbee.platform.domain.ApiParamDetail;
import com.fastbee.platform.domain.ApiRequestExample;
import com.fastbee.platform.enums.ParamType;
import com.fastbee.platform.enums.RequestFormatType;
import com.fastbee.platform.vo.ApiDefinitionVo;
import com.fastbee.platform.vo.ApiRequestDetails;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * API 详情转换工具类
 */
public class ApiDetailTransformer {

    /**
     * 将参数列表转成统一的 ApiRequestDetails
     *
     * @param apiDefinitionVo 参数详情列表
     * @return ApiRequestDetails
     */
    public static ApiRequestDetails transformParamDetails(ApiDefinitionVo apiDefinitionVo) {
        return transformParamDetails(apiDefinitionVo,null,null);
    }


    /**
     * 将参数列表转成统一的 ApiRequestDetails
     *
     * @param apiDefinitionVo 参数详情列表
     * @return ApiRequestDetails
     */
    public static ApiRequestDetails transformParamDetails(ApiDefinitionVo apiDefinitionVo,Map<String,Object> map) {
        AtomicReference<ApiRequestDetails> apiRequestDetails = new AtomicReference<>(new ApiRequestDetails());
        map.forEach((key, value) -> {
            apiRequestDetails.set(transformParamDetails(apiDefinitionVo, key, value));
        });
        return apiRequestDetails.get();
    }


    /**
     * 将参数列表转成统一的 ApiRequestDetails
     *
     * @param apiDefinitionVo 参数详情列表
     * @return ApiRequestDetails
     */
    public static ApiRequestDetails transformParamDetails(ApiDefinitionVo apiDefinitionVo,String key,Object value) {
        ApiRequestDetails details = new ApiRequestDetails();
        BeanUtil.copyProperties(apiDefinitionVo,details);

        Map<String, Object> pathMap = new HashMap<>();
        Map<String, Object> queryMap = new HashMap<>();
        Map<String, Object> headerMap = new HashMap<>();
        Map<String, Object> bodyMap = new HashMap<>();
        Map<String, Object> returnMap = new HashMap<>();
        Boolean bool = true;
        if (apiDefinitionVo.getApiParamDetailList() != null) {
            for (ApiParamDetail param : apiDefinitionVo.getApiParamDetailList()) {
                ParamType type = ParamType.fromValue(param.getParamType());
                Object paramValue = param.getParamValue();
                if ("1".equals(param.getAutoAppend())) {
                    if (param.getParamName().equals(key)) {
                        paramValue = value;
                        bool = false;
                    }else {
                        break;
                    }
                }
                switch (type) {
                    case PATH:
                        pathMap.put(param.getParamName(), paramValue);
                        break;
                    case QUERY:
                        queryMap.put(param.getParamName(), paramValue);
                        break;
                    case HEADER:
                        headerMap.put(param.getParamName(), paramValue);
                        break;
                    case BODY:
                        bodyMap.put(param.getParamName(), paramValue);
                        break;
                    case RETURN:
                        returnMap.put(param.getParamName(), paramValue);
                        break;
                    default:
                        // 后续扩展其他类型在这里加
                        break;
                }
            }
        }
        if(bool){
            bodyMap.put(key, value);
        }
        details.setReturnType(RequestFormatType.fromValue(apiDefinitionVo.getRequestFormat()).getValue());
        details.setPath(pathMap);
        details.setQuery(queryMap);
        details.setHeader(headerMap);
        details.setBody(bodyMap);
        details.setReturnData(returnMap);

        return details;
    }

    /**
     * 将请求示例直接放入 ApiRequestDetails 中
     *
     * @param requestExample 请求示例
     * @return ApiRequestDetails
     */
    public static ApiRequestDetails transformRequestExample(ApiRequestExample requestExample) {
        ApiRequestDetails details = new ApiRequestDetails();

        if (requestExample != null) {
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("example", requestExample.getRequestExample());

            Map<String, Object> headerMap = new HashMap<>();
            headerMap.put("example", requestExample.getRequestHeadersExample());

            details.setBody(bodyMap);
            details.setHeader(headerMap);
        }

        return details;
    }
}



