package com.fastbee.platform.service.impl;

import java.sql.SQLException;
import java.util.*;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dtflys.forest.http.ForestRequest;
import com.fastbee.common.exception.base.BaseException;
import com.fastbee.common.utils.DateUtils;
import com.fastbee.common.utils.SecurityUtils;
import com.fastbee.http.service.HttpClientFactory;
import com.fastbee.http.service.SuccessCondition;
import com.fastbee.iot.domain.*;
import com.fastbee.iot.ruleEngine.MqttClientFactory;
import com.fastbee.platform.domain.ApiDevice;
import com.fastbee.platform.domain.ApiParamDetail;
import com.fastbee.platform.handler.DynamicApiClient;
import com.fastbee.platform.mapper.ApiParamDetailMapper;
import com.fastbee.platform.service.IApiDeviceService;
import com.fastbee.script.domain.ApiScript;
import com.fastbee.script.model.ApiScriptCondition;
import com.fastbee.script.ruleEngine.ApiMsgContext;
import com.fastbee.script.service.IApiScriptService;
import com.yomahub.liteflow.builder.LiteFlowNodeBuilder;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.fastbee.common.utils.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import com.fastbee.platform.mapper.ApiDefinitionMapper;
import com.fastbee.platform.domain.ApiDefinition;
import com.fastbee.platform.service.IApiDefinitionService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 接口定义Service业务层处理
 *
 * @author lwb
 * @date 2025-04-27
 */
@Service
public class ApiDefinitionServiceImpl extends ServiceImpl<ApiDefinitionMapper,ApiDefinition> implements IApiDefinitionService {
    @Autowired
    private ApiParamDetailMapper apiParamDetailMapper;
    @Resource
    private DynamicApiClient dynamicApiClient;
    @Resource
    private IApiScriptService apiScriptService;
    @Resource
    private IApiDeviceService apiDeviceService;
    /**
     * 查询接口定义
     *
     * @param id 主键
     * @return 接口定义
     */
    @Override
    @Cacheable(cacheNames = "ApiDefinition", key = "#id")
    // 查询时更新key缓存，更新和删除时删除缓存，新增时不更新，下一次查询会更新缓存
    public ApiDefinition queryByIdWithCache(Long id){
        return this.getById(id);
    }

    /**
     * 查询接口定义
     *
     * @param id 主键
     * @return 接口定义
     */
    @Override
    @Cacheable(cacheNames = "ApiDefinition", key = "#id")
    // 查询时更新key缓存，更新和删除时删除缓存，新增时不更新，下一次查询会更新缓存
    public ApiDefinition selectApiDefinitionById(Long id){
        return this.getById(id);
    }

    /**
     * 查询接口定义列表
     *
     * @param apiDefinition 接口定义
     * @return 接口定义
     */
    @Override
    public List<ApiDefinition> selectApiDefinitionList(ApiDefinition apiDefinition) {
        LambdaQueryWrapper<ApiDefinition> lqw = buildQueryWrapper(apiDefinition);
        return baseMapper.selectList(lqw);
    }

    private LambdaQueryWrapper<ApiDefinition> buildQueryWrapper(ApiDefinition query) {
        Map<String, Object> params = query.getParams();
        LambdaQueryWrapper<ApiDefinition> lqw = Wrappers.lambdaQuery();
                    lqw.like(StringUtils.isNotBlank(query.getApiName()), ApiDefinition::getApiName, query.getApiName());
                    lqw.eq(StringUtils.isNotBlank(query.getApiUrl()), ApiDefinition::getApiUrl, query.getApiUrl());
                    lqw.eq(StringUtils.isNotBlank(query.getApiType()), ApiDefinition::getApiType, query.getApiType());
                    lqw.eq(StringUtils.isNotBlank(query.getMethod()), ApiDefinition::getMethod, query.getMethod());
                    lqw.eq(StringUtils.isNotBlank(query.getRequestFormat()), ApiDefinition::getRequestFormat, query.getRequestFormat());

        if (!Objects.isNull(params.get("beginTime")) &&
        !Objects.isNull(params.get("endTime"))) {
            lqw.between(ApiDefinition::getCreateTime, params.get("beginTime"), params.get("endTime"));
        }
        return lqw;
    }

    /**
     * 新增接口定义
     *
     * @param add 接口定义
     * @return 是否新增成功
     */
    @Override
    @Transactional
    public Boolean insertWithCache(ApiDefinition add) {
        validEntityBeforeSave(add);
        boolean save = this.save(add);
        if (save && CollUtil.isNotEmpty(add.getApiParamDetailList())) {
            for (ApiParamDetail apiParamDetail : add.getApiParamDetailList()) {
                apiParamDetail.setApiId(add.getId());
            }
            apiParamDetailMapper.insertBatch(add.getApiParamDetailList());
        }
        return save;
    }

    /**
     * 新增接口定义
     *
     * @param apiDefinitionList 接口定义
     * @return 是否新增成功
     */
    @Override
    @Transactional
    public Boolean insertWithCache(List<ApiDefinition> apiDefinitionList) {
        for (ApiDefinition add : apiDefinitionList) {
            validEntityBeforeSave(add);
            boolean save = this.save(add);
            if (save && CollUtil.isNotEmpty(add.getApiParamDetailList())) {
                for (ApiParamDetail apiParamDetail : add.getApiParamDetailList()) {
                    apiParamDetail.setApiId(add.getId());
                }
                apiParamDetailMapper.insertBatch(add.getApiParamDetailList());
            }
        }
        return true;
    }

    /**
     * 修改接口定义
     *
     * @param update 接口定义
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(cacheNames = "ApiDefinition", key = "#update.id")
    @Transactional
    public Boolean updateWithCache(ApiDefinition update) {
        validEntityBeforeSave(update);
        boolean save = this.updateById(update);
        if (save && CollUtil.isNotEmpty(update.getApiParamDetailList())) {
            for (ApiParamDetail apiParamDetail : update.getApiParamDetailList()) {
                apiParamDetail.setApiId(update.getId());
            }
            apiParamDetailMapper.deleteApiDetailId(update.getId());
            apiParamDetailMapper.insertBatch(update.getApiParamDetailList());
        }
        return save;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ApiDefinition entity){
        //TODO 做一些数据校验,如唯一约束
        if (StrUtil.isBlank(entity.getApiType())) {
            entity.setApiType("1");
        }
        if (StrUtil.isBlank(entity.getRequestFormat())) {
            entity.setRequestFormat("JSON");
        }
    }

    /**
     * 校验并批量删除接口定义信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    @CacheEvict(cacheNames = "ApiDefinition", keyGenerator = "deleteKeyGenerator" )
    public Boolean deleteWithCacheByIds(Long[] ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return this.removeByIds(Arrays.asList(ids));
    }

    @Override
    public JSONObject connect(Long id) {
        return dynamicApiClient.execute(id);
    }

    @Override
    public List<ApiDevice> connectDevice(Long apiDefinitionId) {
        ApiDefinition apiDefinitions = selectApiDefinitionById(apiDefinitionId);
        JSONObject execute = dynamicApiClient.execute(apiDefinitions.getId());
        ApiScript apiScript = apiScriptService.selectRuleScriptById(apiDefinitions.getApiScriptId());
        List<ApiDevice> apiDevices = new ArrayList<>();
        if (StrUtil.isNotBlank(apiDefinitions.getApiScriptId())) {
            // 动态刷新脚本
            LiteFlowNodeBuilder builder = null;
            builder = LiteFlowNodeBuilder.createScriptNode();
            if (builder != null) {
                builder.setId(apiScript.getScriptId())
                        .setName("ApiScript")
                        .setScript(apiScript.getScriptData())
                        .build();
            }
            ApiScriptCondition apiScriptConditionBuilder = ApiScriptCondition.builder().scriptId(apiScript.getScriptId()).build();
            //组装消息对象
            ApiMsgContext apiMsgContext = ApiMsgContext.builder().topic("connectDevice").payload(JSONUtil.toJsonStr(execute)).protocolCode("JSON").build();
            ApiMsgContext execRuleScript = apiScriptService.execRuleScript(apiScriptConditionBuilder, apiMsgContext);
            if (StrUtil.isNotBlank(execRuleScript.getPayload())) {
                apiDevices = JSONUtil.toList(execRuleScript.getPayload(), ApiDevice.class);
            }
        }else {
            try {
                apiDevices = JSONUtil.toList(JSONUtil.toJsonStr(execute), ApiDevice.class);
            }catch (Exception e){
                throw new BaseException("数据结构解析异常" + e.getMessage());
            }
        }
        List<String> collect = apiDevices.stream().map(ApiDevice::getSerialNumber).collect(Collectors.toList());
        LambdaQueryWrapper<ApiDevice> lqw = Wrappers.lambdaQuery();
        lqw.in(ApiDevice::getSerialNumber,collect);
        List<ApiDevice> apiDevices1 = apiDeviceService.list(lqw);

        // 将查询结果转换为序列号到设备的映射
        Map<String, ApiDevice> existingDevices = apiDevices1.stream()
                .collect(Collectors.toMap(ApiDevice::getSerialNumber, device -> device));

        // 设置状态标识
        apiDevices.forEach(device -> {
            if(existingDevices.containsKey(device.getSerialNumber())){
                device.setIsBinding(existingDevices.get(device.getSerialNumber()).getIsBinding());
            }
        });
        return apiDevices;
    }

}
