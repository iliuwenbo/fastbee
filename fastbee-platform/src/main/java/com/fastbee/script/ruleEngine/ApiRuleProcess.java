//package com.fastbee.script.ruleEngine;
//
//import com.alibaba.fastjson2.JSON;
//import com.fastbee.common.core.redis.RedisCache;
//import com.fastbee.common.core.redis.RedisKeyBuilder;
//import com.fastbee.iot.model.ProductCode;
//import com.fastbee.iot.model.ScriptCondition;
//import com.fastbee.platform.service.IApiDeviceService;
//import com.fastbee.script.model.ApiScriptCondition;
//import com.fastbee.script.service.IApiScriptService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.Objects;
//
///**
// * 执行规则引擎
// *
// * @author gsb
// * @date 2024/2/3 16:07
// */
//@Component
//@Slf4j
//public class ApiRuleProcess {
//
//    @Resource
//    private IApiScriptService scriptService;
//    @Resource
//    private RedisCache redisCache;
//    @Resource
//    private IApiDeviceService deviceService;
//
//    /**
//     * 规则引擎脚本处理
//     *
//     * @param topic
//     * @param payload
//     * @param event   1=设备上报 2=平台下发 3=设备上线 4=设备下线 （其他可以增加设备完成主题订阅之类）
//     * @return
//     */
//    public ApiMsgContext processRuleScript(String serialNumber, int event, String topic, String payload) {
//        ProductCode productCode = getDeviceDetail(serialNumber);
//        if (Objects.isNull(productCode)) {
//            return new ApiMsgContext();
//        }
//        // 查询数据流脚本组件
//        ApiScriptCondition scriptCondition = ApiScriptCondition.builder()
//                .scriptPurpose(1)
//                .scriptEvent(event)
//                .productId(productCode.getProductId())
//                .build();
//        ApiMsgContext context = ApiMsgContext.builder()
//                .serialNumber(serialNumber)
//                .productId(productCode.getProductId())
//                .protocolCode(productCode.getProtocolCode())
//                .payload(payload)
//                .topic(topic)
//                .build();
//        //返回处理完的消息上下文
//        return scriptService.execRuleScript(scriptCondition, context);
//    }
//
//}
