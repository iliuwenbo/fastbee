package com.fastbee.platform.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fastbee.common.exception.base.BaseException;
import com.fastbee.common.utils.StringUtils;
import com.fastbee.iot.cache.ITSLCache;
import com.fastbee.iot.domain.Device;
import com.fastbee.iot.domain.SceneScript;
import com.fastbee.iot.mapper.DeviceMapper;
import com.fastbee.iot.model.ThingsModels.ThingsModelValueItem;
import com.fastbee.platform.domain.ApiParamDetail;
import com.fastbee.platform.domain.ApiThirdPartyPlatform;
import com.fastbee.platform.enums.EncryptionType;
import com.fastbee.platform.mapper.ApiDefinitionMapper;
import com.fastbee.platform.mapper.ApiThirdPartyPlatformMapper;
import com.fastbee.platform.utils.ApiDetailTransformer;
import com.fastbee.platform.vo.ApiDefinitionVo;
import com.fastbee.platform.vo.ApiRequestDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态API客户端（已重构简化）
 * <p>
 * 该类的所有公共 execute 方法都遵循一个原则：
 * 1. 准备 ApiRequestDetails 对象。
 * 2. 调用唯一的私有核心方法 processAndSendRequest() 来完成所有后续工作。
 */
@Component
public class DynamicApiClient {

    @Autowired
    private ApiService apiService;
    @Autowired
    private ApiDefinitionMapper apiDefinitionMapper;
    @Autowired
    private ApiThirdPartyPlatformMapper apiThirdPartyPlatformMapper;
    @Autowired
    private ITSLCache itslCache;
    @Resource
    private DeviceMapper deviceMapper;

    /**
     * 【便捷入口1】: 根据API定义ID执行请求（如：拉取设备列表）
     *
     * @param apiDefinitionId API定义的数据库ID
     * @return JSONObject 响应结果
     */
    public JSONObject execute(Long apiDefinitionId) {
        ApiDefinitionVo definitionVo = apiDefinitionMapper.selectDetailById(apiDefinitionId);
        if (definitionVo == null) {
            throw new BaseException("API定义不存在, ID: " + apiDefinitionId);
        }
        ApiRequestDetails requestDetails = ApiDetailTransformer.transformParamDetails(definitionVo);
        return processAndSendRequest(requestDetails);
    }

    /**
     * 【便捷入口2】: 根据API定义ID和自定义请求体执行
     *
     * @param apiDefinitionId API定义的数据库ID
     * @param body            自定义的请求体
     * @return JSONObject 响应结果
     */
    public JSONObject execute(Long apiDefinitionId, Map<String, Object> body) {
        ApiDefinitionVo definitionVo = apiDefinitionMapper.selectDetailById(apiDefinitionId);
        if (definitionVo == null) {
            throw new BaseException("API定义不存在, ID: " + apiDefinitionId);
        }
        // 将body中的键值对应用到参数转换中
        ApiRequestDetails requestDetails = ApiDetailTransformer.transformParamDetails(definitionVo, body);
        return processAndSendRequest(requestDetails);
    }

    /**
     * 【便捷入口3】: 为特定设备执行指令
     *
     * @param serialNumber 设备序列号
     * @param commandBody  指令内容, 例如 {"switch": "on"}
     * @return JSONObject 响应结果
     */
    public JSONObject execute(String serialNumber, Map<String, Object> commandBody) {
        Device device = deviceMapper.selectDeviceBySerialNumber(serialNumber);
        if (device == null) {
            throw new BaseException("设备不存在, 序列号: " + serialNumber);
        }
        // 从指令体中获取第一个指令作为标识符
        String identify = commandBody.keySet().iterator().next();

        ThingsModelValueItem thingModel = itslCache.getSingleThingModels(device.getProductId(), identify);
        if (thingModel == null || thingModel.getApiDefinitionId() == null) {
            throw new BaseException("物模型或其关联的API定义不存在, 产品ID: " + device.getProductId() + ", 标识符: " + identify);
        }

        ApiDefinitionVo definitionVo = apiDefinitionMapper.selectDetailById(thingModel.getApiDefinitionId());
        if (definitionVo == null) {
            throw new BaseException("指令关联的API定义不存在, ID: " + thingModel.getApiDefinitionId());
        }

        for (ApiParamDetail apiParamDetail : definitionVo.getApiParamDetailList()) {
            if ("deviceId".equals(apiParamDetail.getManufacturerId())) {
                apiParamDetail.setParamValue(serialNumber);
            }
        }

        ApiRequestDetails requestDetails = ApiDetailTransformer.transformParamDetails(definitionVo, commandBody);
        return processAndSendRequest(requestDetails);
    }


    /**
     * 动态调用 API 并返回响应
     * 服务下发
     */
    public JSONObject execute(String serialNumber, String command) {
        Device device = deviceMapper.selectDeviceBySerialNumber(serialNumber);
        Map<String,Object> map = new HashMap<>();
        List<SceneScript> list = JSONUtil.toList(command, SceneScript.class);
        String identify = "";
        for (SceneScript script : list) {
            String id = script.getId();
            identify = id;
            String value = script.getValue();
            map.put(id,value);
        }
        map.put("id",serialNumber);
        ThingsModelValueItem singleThingModels = itslCache.getSingleThingModels(device.getProductId(), identify);
        if (singleThingModels == null) {
            throw new BaseException("查询物模型为空,产品id:{"+device.getProductId()+"},标识符:{"+identify+"}");
        }
        ApiDefinitionVo apiDefinitionVo = apiDefinitionMapper.selectDetailById(singleThingModels.getApiDefinitionId());
        if (apiDefinitionVo == null) {
            throw new BaseException("不存在的接口调用！");
        }
        ApiRequestDetails apiRequestDetails = ApiDetailTransformer.transformParamDetails(apiDefinitionVo,map);
        return processAndSendRequest(apiRequestDetails);
    }

    /**
     * 【核心实现】: 处理并发送请求的唯一方法
     *
     * @param requestDetails 完整构造的请求详情对象
     * @return JSONObject 最终的API响应
     */
    private JSONObject processAndSendRequest(ApiRequestDetails requestDetails) {
        // 1. 准备URL: 如果URL不是完整的HTTP路径，则根据平台信息补全
        if (!StringUtils.ishttp(requestDetails.getApiUrl())) {
            String platformId = requestDetails.getApiThirdPartyPlatformId();
            if (StrUtil.isBlank(platformId)) {
                throw new BaseException("请求URL不完整，且未关联第三方平台，无法确定API端点！");
            }

            ApiThirdPartyPlatform platform = apiThirdPartyPlatformMapper.selectById(platformId);
            if (platform == null) {
                throw new BaseException("关联的第三方平台不存在, ID: " + platformId);
            }

            // 拼接完整的URL
            if (StrUtil.isNotBlank(platform.getApiEndpoint())) {
                requestDetails.setApiUrl(platform.getApiEndpoint() + requestDetails.getApiUrl());
            }

            // 2. 应用加密策略
            applyEncryption(requestDetails, platform);
        }

        // 3. 发送最终请求
        return apiService.sendRequest(requestDetails);
    }

    /**
     * 私有辅助方法: 应用加密和认证策略
     */
    private void applyEncryption(ApiRequestDetails requestDetails, ApiThirdPartyPlatform platform) {
        // 1. 从 code 获取对应的枚举实例
        EncryptionType encryptionType = EncryptionType.fromCode(platform.getEncryptionType());
        try {
            // 2. 调用枚举实例的 processWithSignature, 它会将任务委托给内部的策略对象
            encryptionType.process(requestDetails,platform.getAppKey(),platform.getAppSecret());
        } catch (Exception e) {
            throw new BaseException("请求签名或认证处理失败: " + e.getMessage());
        }
    }
}