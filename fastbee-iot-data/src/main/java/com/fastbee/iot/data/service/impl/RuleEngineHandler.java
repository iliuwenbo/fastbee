package com.fastbee.iot.data.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fastbee.common.core.thingsModel.ThingsModelSimpleItem;
import com.fastbee.common.utils.uuid.UUID;
import com.fastbee.iot.data.ruleEngine.SceneContext;
import com.fastbee.iot.data.ruleEngine.SceneParams;
import com.fastbee.iot.data.service.IRuleEngine;
import com.fastbee.iot.domain.*;
import com.fastbee.iot.mapper.SceneMapper;
import com.fastbee.iot.model.ScriptCondition;
import com.fastbee.iot.service.ISceneDeviceService;
import com.fastbee.iot.service.ISceneMiddleService;
import com.fastbee.iot.service.IScriptService;
import com.fastbee.common.core.mq.message.ReportDataBo;
import com.fastbee.platform.domain.SceneMiddleLog;
import com.fastbee.platform.mapper.SceneMiddleLogMapper;
import com.fastbee.ruleEngine.core.FlowLogExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 规则引擎处理数据方法
 *
 * @author bill
 */
@Component
@Slf4j
public class RuleEngineHandler implements IRuleEngine {

    @Resource
    private ISceneDeviceService sceneDeviceService;

    @Resource
    private IScriptService scriptService;

    @Autowired
    private FlowLogExecutor flowExecutor;


    @Resource
    private ISceneMiddleService sceneMiddleService;

    @Resource
    private SceneMapper sceneMapper;

    @Resource
    private SceneMiddleLogMapper sceneMiddleLogMapper;

    private static final Map<String, ScheduledExecutorService> CEHCK_CACHE = new ConcurrentHashMap<>();

    /**
     * 规则匹配(告警和场景联动)
     *
     * @param bo 上报数据模型bo
     * @see ReportDataBo
     */
    public void ruleMatch(ReportDataBo bo) {
        try {
            // 场景联动处理
            this.sceneProcess(bo);
        } catch (Exception e) {
            log.error("接收数据，解析数据时异常 message={}", e, e.getMessage());
        }
    }

    /**
     * 场景规则处理
     */
    public void sceneProcess(ReportDataBo bo) throws ExecutionException, InterruptedException {
        // 查询设备关联的场景
        SceneDevice sceneDeviceParam = new SceneDevice();
        sceneDeviceParam.setProductId(bo.getProductId());
        sceneDeviceParam.setSerialNumber(bo.getSerialNumber());
        List<Scene> sceneListVo = sceneDeviceService.selectTriggerDeviceRelateScenes(sceneDeviceParam);
        int type = bo.getType();
        // 获取上报的物模型
        List<ThingsModelSimpleItem> thingsModelSimpleItems = bo.getDataList();
        if (CollectionUtils.isEmpty(bo.getDataList())) {
            thingsModelSimpleItems = JSON.parseArray(bo.getMessage(), ThingsModelSimpleItem.class);
        }

        for (Scene sceneVo : sceneListVo) {
            //获取中包场景集合
            List<SceneMiddle> sceneMiddles = sceneMiddleService.selectSceneMiddleList(SceneMiddle.builder().iotSceneId(sceneVo.getSceneId()).enable(1).build());
            for (SceneMiddle sceneMiddle : sceneMiddles) {
                //查询中包关联的所有小包
                LambdaQueryWrapper<Scene> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.in(Scene::getSceneId, sceneMiddle.getIotSceneIds().split(","));
                //查询所有小包
                List<Scene> sceneList = sceneMapper.selectList(queryWrapper);
                String requestId = sceneMiddle.getChainName();
                // 执行场景规则,异步非阻塞
                for (Scene scene : sceneList) {
                    ScriptCondition scriptCondition = ScriptCondition.builder().sceneId(scene.getSceneId()).build();
                    List<Script> list = scriptService.selectExecRuleScriptList(scriptCondition);
                    for(Script script : list) {
                        requestId  = requestId + "/" + script.getScriptId();
                    }
                }
                SceneParams sceneParams = getSceneMiddleLog(bo,sceneMiddle,requestId);
                SceneContext context = new SceneContext(bo.getSerialNumber(), bo.getProductId(), type, thingsModelSimpleItems, sceneParams);
                flowExecutor.execute2FutureWithRid(sceneMiddle.getChainName(), null, requestId, context);
            }
        }
    }

    public void addCheckTask(String key, ScheduledExecutorService task) {
        ScheduledExecutorService taskold = CEHCK_CACHE.get(key);
        if (taskold != null) {
            taskold.shutdown();
        }
        CEHCK_CACHE.put(key, task);
    }

    public void removeCheckTask(String key) {
        ScheduledExecutorService taskold = CEHCK_CACHE.get(key);
        if (taskold != null) {
            taskold.shutdown();
            CEHCK_CACHE.remove(key);
        }
    }

    private SceneParams getSceneMiddleLog(ReportDataBo bo,SceneMiddle sceneMiddle,String requestId){
        if (Arrays.asList(1, 2, 3).contains(bo.getType())) {
            SceneParams sceneParams = new SceneParams();
            sceneParams.setMiddleId(sceneMiddle.getMiddleId());
            sceneParams.setMiddleName(sceneMiddle.getMiddleName());
            sceneParams.setRequestId(requestId);
            sceneParams.setMessageId(IdUtil.getSnowflakeNextIdStr());
            sceneParams.setStatus("1");
            sceneParams.setMessageType("1");
            String messageValue = "";
            if (bo.getDataList() != null) {
                messageValue = JSONUtil.toJsonStr(bo.getDataList());
            }else if(ObjectUtil.isNotEmpty(bo.getSources())){
                messageValue = String.valueOf(bo.getSources());
            }else if(StrUtil.isNotBlank(bo.getMessage())){
                messageValue = String.valueOf(bo.getMessage());
            }
            sceneParams.setMessageValue(messageValue);
            sceneParams.setSerialNumber(bo.getSerialNumber());
            sceneParams.setDeviceName(bo.getDeviceName());
            sceneParams.setReportType(bo.getType());
            sceneParams.setId(Long.valueOf(sceneParams.getMessageId()));
            return sceneParams;
        }
        return null;
    }


}
