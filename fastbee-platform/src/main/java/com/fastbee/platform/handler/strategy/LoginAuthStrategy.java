package com.fastbee.platform.handler.strategy;

import com.fastbee.platform.enums.EncryptionType;
import com.fastbee.platform.utils.HttpRequestLoginUtil;
import com.fastbee.platform.vo.ApiRequestDetails;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("login")
public class LoginAuthStrategy implements EncryptionType.EncryptionStrategy {

    @Override
    public ApiRequestDetails processWithSignature(ApiRequestDetails requestDetails, String appKey, String appSecret) throws Exception {
        // 假设apiUrl的域名部分是基础URL
        String baseUrl = requestDetails.getApiUrl().substring(0, requestDetails.getApiUrl().indexOf("/", 8));

        // 此处tenantName可以硬编码，也可以从其他地方获取，例如requestDetails的一个新字段
        String tenantName = "芋道源码";

        Map<String, Object> authHeaders = HttpRequestLoginUtil.getAccessToken(baseUrl, appKey, appSecret, tenantName);

        if (authHeaders != null) {
            requestDetails.getHeader().putAll(authHeaders);
        }

        return requestDetails;
    }
}