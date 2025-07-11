//package com.fastbee;
//
//import cn.hutool.core.util.ObjUtil;
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.json.JSON;
//import cn.hutool.json.JSONObject;
//import cn.hutool.json.JSONUtil;
//import com.alibaba.fastjson2.JSONArray;
//import com.fastbee.platform.domain.ApiDefinition;
//import com.fastbee.platform.domain.ApiDevice;
//import com.fastbee.platform.handler.DynamicApiClient;
//import com.fastbee.platform.service.IApiDefinitionService;
//import com.fastbee.platform.service.IApiDeviceService;
//import com.fastbee.script.domain.ApiScript;
//import com.fastbee.script.model.ApiScriptCondition;
//import com.fastbee.script.ruleEngine.ApiMsgContext;
//import com.fastbee.script.service.IApiScriptService;
//import com.yomahub.liteflow.builder.LiteFlowNodeBuilder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.annotation.Resource;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class ApiServiceTest {
//    @Resource
//    private IApiDefinitionService apiDefinitionService;
//    @Resource
//    private DynamicApiClient dynamicApiClient;
//    @Resource
//    private IApiScriptService scriptService;
//    @Resource
//    private IApiDeviceService apiDeviceService;
//
//    @Test
//    public void execute() {
//        List<ApiDefinition> apiDefinitions = apiDefinitionService.selectApiDefinitionList(new ApiDefinition());
//                // 筛选出在当前设备登录的 token key
//        apiDefinitions = apiDefinitions.stream().filter(item -> {
//                    boolean deviceBool = "1".equals(item.getApiType());
////                    if (deviceBool) {
////                        logger.info("当前分类设备登录 Token Key：{}", item);
////                    }
//                    return deviceBool;
//                }).collect(Collectors.toList());
//        for (ApiDefinition apiDefinition : apiDefinitions) {
//            JSONObject execute = dynamicApiClient.execute(apiDefinition.getId());
//            ApiScript d1917029243565510656 = scriptService.selectRuleScriptById(apiDefinition.getApiScriptId());
//            // 动态刷新脚本
//            LiteFlowNodeBuilder builder = null;
//            if (d1917029243565510656.getScriptType().equals("script")) {
//                builder = LiteFlowNodeBuilder.createScriptNode();
//            } else if (d1917029243565510656.getScriptType().equals("switch_script")) {
//                builder = LiteFlowNodeBuilder.createScriptSwitchNode();
//            } else if (d1917029243565510656.getScriptType().equals("boolean_script")) {
//                builder = LiteFlowNodeBuilder.createScriptBooleanNode();
//            } else if (d1917029243565510656.getScriptType().equals("for_script")) {
//                builder = LiteFlowNodeBuilder.createScriptForNode();
//            }
//            if (builder != null) {
//                builder.setId(d1917029243565510656.getScriptId())
////                        .setName("ApiScript")
//                        .setScript(d1917029243565510656.getScriptData())
//                        .build();
//            }
//            ApiScriptCondition apiScriptConditionBuilder = ApiScriptCondition.builder().scriptId(d1917029243565510656.getScriptId()).build();
//            ApiMsgContext apiMsgContext = new ApiMsgContext();
//            apiMsgContext.setTopic("demo");
//            apiMsgContext.setPayload(JSONUtil.toJsonStr(execute));
//            apiMsgContext.setProtocolCode("JSON");
//            ApiMsgContext apiMsgContext1 = scriptService.execRuleScript(apiScriptConditionBuilder, apiMsgContext);
//            if (StrUtil.isNotBlank(apiMsgContext1.getPayload())) {
//                List<ApiDevice> list = JSONUtil.toList(apiMsgContext1.getPayload(), ApiDevice.class);
//                apiDeviceService.saveOrUpdateBatch(list);
//            }
//        }
//    }
//
//    @Test
//    public void execute5() {
//        // 请求的 URL
//        String url = "http://192.168.0.96:30002/gateway/hkplus-uacs/code";
//
//        // 请求方法
//        String method = "GET";
//
//        // 执行请求并获取响应
//        JSONObject execute = dynamicApiClient.execute(url, method,"json", null,null, null, null,null);
//
//        // 输出响应
//        System.out.println("Response: " + execute);
//    }
//
//    @Test
//    public void execute4() {
//        ApiScriptCondition apiScriptConditionBuilder = ApiScriptCondition.builder().scriptId("").build();
//        ApiMsgContext apiMsgContext = new ApiMsgContext();
//        scriptService.execRuleScript(apiScriptConditionBuilder,apiMsgContext);
//
//        JSONObject execute = dynamicApiClient.execute(3L);
//        // 输出响应
//        System.out.println("Response: " + execute);
//    }
//
//    @Test
//    public void execute3() {
//        JSONObject execute = dynamicApiClient.execute(2L);
//        // 输出响应
//        System.out.println("Response: " + execute);
//    }
//
//    @Test
//    public void execute2() {
//        JSONObject execute = dynamicApiClient.execute(1L);
//
//        // 输出响应
//        System.out.println("Response: " + execute);
//    }
//}
//
