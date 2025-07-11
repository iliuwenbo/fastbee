package com.fastbee.platform.handler.strategy;

import com.fastbee.platform.enums.EncryptionType;
import com.fastbee.platform.vo.ApiRequestDetails;
import org.springframework.stereotype.Component;

@Component("none")
public class NoneEncryptionStrategy implements EncryptionType.EncryptionStrategy {
    @Override
    public ApiRequestDetails processWithSignature(ApiRequestDetails requestDetails, String appKey, String appSecret) {
        // 无需任何操作，直接返回
        return requestDetails;
    }
}
