package com.fastbee;

import cn.hutool.json.JSONObject;
import com.fastbee.platform.handler.DynamicApiClient;
import com.fastbee.script.domain.ApiScript;
import com.fastbee.script.model.ApiScriptCondition;
import com.fastbee.script.ruleEngine.ApiMsgContext;
import com.fastbee.script.service.IApiScriptService;
import com.yomahub.liteflow.builder.LiteFlowNodeBuilder;
import com.yomahub.liteflow.flow.FlowBus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ApiScriptServiceTest {
    @Resource
    private IApiScriptService scriptService;

    @Test
    public void execute() {

        ApiScript d1917029243565510656 = scriptService.selectRuleScriptById("D1917029243565510656");
        // 动态刷新脚本
            LiteFlowNodeBuilder builder = null;
            if (d1917029243565510656.getScriptType().equals("script")) {
                builder = LiteFlowNodeBuilder.createScriptNode();
            } else if (d1917029243565510656.getScriptType().equals("switch_script")) {
                builder = LiteFlowNodeBuilder.createScriptSwitchNode();
            } else if (d1917029243565510656.getScriptType().equals("boolean_script")) {
                builder = LiteFlowNodeBuilder.createScriptBooleanNode();
            } else if (d1917029243565510656.getScriptType().equals("for_script")) {
                builder = LiteFlowNodeBuilder.createScriptForNode();
            }
            if (builder != null) {
                builder.setId(d1917029243565510656.getScriptId())
//                        .setName("ApiScript")
                        .setScript(d1917029243565510656.getScriptData())
                        .build();
            }
        ApiScriptCondition apiScriptConditionBuilder = ApiScriptCondition.builder().scriptId(d1917029243565510656.getScriptId()).build();
        ApiMsgContext apiMsgContext = new ApiMsgContext();
        apiMsgContext.setTopic("demo");
        apiMsgContext.setPayload("{\n" +
                "    \"code\": \"0\",\n" +
                "    \"msg\": \"SUCCESS\",\n" +
                "    \"data\": {\n" +
                "        \"total\": 4,\n" +
                "        \"pageNo\": 1,\n" +
                "        \"pageSize\": 1,\n" +
                "        \"list\": [\n" +
                "            {\n" +
                "                \"indexCode\": \"e747cb6f0f3d4762b024a9c8f2e7793f\",\n" +
                "                \"name\": \"门b_门1\",\n" +
                "                \"resourceType\": \"door\",\n" +
                "                \"doorNo\": \"1\",\n" +
                "                \"description\": \"null\",\n" +
                "                \"parentIndexCodes\": \"aefba9b4208c43f88d1015f951d9e181\",\n" +
                "                \"regionIndexCode\": \"0129900000000001\",\n" +
                "                \"regionPath\": \"root000000\",\n" +
                "                \"channelType\": \"door\",\n" +
                "                \"channelNo\": \"1\",\n" +
                "                \"installLocation\": \"null\",\n" +
                "                \"capabilitySet\": \"null\",\n" +
                "                \"controlOneId\": \"5b7e23fa-12b7-44be-aad1-f5941b9a53c6\",\n" +
                "                \"controlTwoId\": \"null\",\n" +
                "                \"readerInId\": \"2aab1eab-d410-45a2-89ac-1409b07d5d7e\",\n" +
                "                \"readerOutId\": \"d8a5476e-25c0-4aa2-b7e3-CC3788ba1f77\",\n" +
                "                \"comId\": \"acs\",\n" +
                "                \"createTime\": \"2018-11-28T16:47:27:358+08:00\",\n" +
                "                \"updateTime\": \"2018-11-28T16:48:34:011+08:00\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}\n");
        apiMsgContext.setProtocolCode("JSON");
        ApiMsgContext apiMsgContext1 = scriptService.execRuleScript(apiScriptConditionBuilder, apiMsgContext);

        System.out.println();
    }
}

