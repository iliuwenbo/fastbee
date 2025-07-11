package com.fastbee.platform.enums;

import com.fastbee.common.exception.base.BaseException;
import com.fastbee.platform.handler.strategy.HikvisionEncryptionStrategy;
import com.fastbee.platform.handler.strategy.LoginAuthStrategy;
import com.fastbee.platform.handler.strategy.NoneEncryptionStrategy;
import com.fastbee.platform.vo.ApiRequestDetails;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum EncryptionType {

    /**
     * 无需加密
     */
    NONE("none", "无需加密", new NoneEncryptionStrategy()),

    /**
     * 海康云眸加密
     */
    HIKVISION("hikvision", "海康云眸加密", new HikvisionEncryptionStrategy()),

    /**
     * 登录认证
     */
    LOGIN("login", "登录认证", new LoginAuthStrategy());

    private final String code;
    private final String description;
    private final EncryptionStrategy strategy; // 每个枚举实例持有一个策略对象

    EncryptionType(String code, String description, EncryptionStrategy strategy) {
        this.code = code;
        this.description = description;
        this.strategy = strategy;
    }


    /**
     * 根据code获取对应的枚举实例（即策略分发器）
     */
    public static EncryptionType fromCode(String code) {
        return Arrays.stream(values())
                .filter(type -> type.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(NONE); // 默认为NONE，提供安全的默认行为
    }

    /**
     * 加密策略接口
     */
    public interface EncryptionStrategy {

        /**
         * 对请求进行签名处理
         * @param requestDetails 原始请求详情
         * @param appKey 应用Key (AK)
         * @param appSecret 应用密钥 (SK)
         * @return 经过签名处理后的请求详情
         * @throws Exception 处理过程中可能抛出的任何异常
         */
        ApiRequestDetails processWithSignature(ApiRequestDetails requestDetails, String appKey, String appSecret) throws Exception;
    }

    /**
     * 将加密任务委托给持有的策略对象
     */
    public ApiRequestDetails process(ApiRequestDetails requestDetails, String appKey, String appSecret) throws Exception {
        if (this.strategy == null) {
            throw new BaseException("未找到 code 为 [" + this.code + "] 的加密策略实现");
        }
        return this.strategy.processWithSignature(requestDetails, appKey, appSecret);
    }

}

