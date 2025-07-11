package com.fastbee.parse.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fastbee.common.core.domain.entity.SysUser;
import com.fastbee.common.utils.SecurityUtils;
import com.fastbee.iot.domain.Category;
import com.fastbee.iot.domain.Device;
import com.fastbee.iot.domain.Product;
import com.fastbee.iot.mapper.CategoryMapper;
import com.fastbee.iot.mapper.DeviceMapper;
import com.fastbee.iot.mapper.ProductMapper;
import com.fastbee.parse.domain.AiRequest;
import com.fastbee.platform.domain.ApiDefinition;
import com.fastbee.platform.domain.ApiDevice;
import com.fastbee.platform.domain.ApiParamDetail;
import com.fastbee.platform.domain.ApiThirdPartyPlatform;
import com.fastbee.platform.mapper.ApiDefinitionMapper;
import com.fastbee.platform.mapper.ApiDeviceMapper;
import com.fastbee.platform.mapper.ApiParamDetailMapper;
import com.fastbee.platform.mapper.ApiThirdPartyPlatformMapper;
import com.fastbee.script.domain.ApiScript;
import com.fastbee.script.mapper.ApiScriptMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * AI解析服务实现类
 * 负责将AI解析出的结构化数据持久化到数据库中。
 */
@Service
public class ParseServiceImpl {

    // 注入所有需要的Mapper接口
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ApiScriptMapper apiScriptMapper;
    @Autowired
    private ApiThirdPartyPlatformMapper apiThirdPartyPlatformMapper;
    @Autowired
    private ApiDeviceMapper apiDeviceMapper;
    @Autowired
    private ApiParamDetailMapper apiParamDetailMapper;

    // ApiDefinitionMapper 是一个假设的Mapper，你需要根据项目中实际的类名进行替换
    @Autowired
    private ApiDefinitionMapper apiDefinitionMapper;


    /**
     * 解析并持久化AI服务返回的结构化数据。
     * <p>
     * 该方法是一个核心的业务流程编排，用于处理来自AI解析服务（例如，从文档或API定义中提取信息）的{@link AiRequest}对象。
     * 它负责将解析出的产品、设备、API、脚本等信息，智能地创建或更新到系统数据库中。
     * 整个过程是事务性的，以确保数据的一致性。
     *
     * @param aiRequest 包含AI解析结果的请求对象。该对象内部嵌套了产品信息、设备列表、API定义、解析脚本等所有需要处理的数据。
     */
    @Transactional(rollbackFor = Exception.class)
    public void parse(AiRequest aiRequest) {
        if (aiRequest == null || aiRequest.getData() == null || CollectionUtils.isEmpty(aiRequest.getData().getProductInfos())) {
            // 如果没有有效数据，则直接返回
            return;
        }

        // 获取当前操作的用户信息，用于填充创建者、租户等信息
        SysUser user = SecurityUtils.getLoginUser().getUser();
        String operator = user.getUserName();
        Long tenantId = user.getTenantId();
        String tenantName = user.getTenantName();

        // 遍历所有AI解析出的产品信息
        for (AiRequest.ProductInfo productInfo : aiRequest.getData().getProductInfos()) {

            // 步骤 1: 创建或更新产品分类 (Category)
            Category category = findOrCreateCategory(productInfo.getProductName(), tenantId, tenantName, operator);

            // 步骤 2: 创建或更新产品 (Product)
            Product product = createOrUpdateProduct(productInfo, category.getCategoryId(), tenantId, tenantName, operator);

            // 步骤 6: 创建或更新解析脚本 (ApiScript)
            // 将脚本与产品关联
            if (productInfo.getParsingProtocol() != null) {
                createOrUpdateApiScript(productInfo.getParsingProtocol(), product.getProductId(), operator);
            }

            // 步骤 7: 创建或更新设备 (Device / ApiDevice)
            if (!CollectionUtils.isEmpty(productInfo.getDeviceInfos())) {
                for (AiRequest.DeviceInfo deviceInfo : productInfo.getDeviceInfos()) {
                    createOrUpdateDevice(deviceInfo, product, tenantId, tenantName, operator);
                }
            }
        }

        // 步骤 3, 4, 5, 8: 处理全局API、平台和关联关系
        if (!CollectionUtils.isEmpty(aiRequest.getData().getGeneratedApiList())) {
            for (AiRequest.GeneratedApiList generatedApi : aiRequest.getData().getGeneratedApiList()) {
                // 步骤 3: 创建或更新第三方平台信息
                ApiThirdPartyPlatform platform = null;
                if (generatedApi.getAuthenticationInfo() != null && StringUtils.hasText(generatedApi.getAuthenticationInfo().getPlatformId())) {
                    platform = findOrCreateThirdPartyPlatform(generatedApi.getAuthenticationInfo(), operator);
                }

                // 步骤 4: 创建或更新HTTP接口定义 (ApiDefinition & ApiParamDetail)
                createOrUpdateApiDefinition(generatedApi, platform, operator);
            }
        }
    }

    /**
     * 步骤 1: 查找或创建产品分类
     */
    private Category findOrCreateCategory(String productName, Long tenantId, String tenantName, String operator) {
        // 假设使用产品名称作为分类名称
        String categoryName = productName + "分类";
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getCategoryName, categoryName)
                .eq(Category::getTenantId, tenantId);
        Category category = categoryMapper.selectOne(queryWrapper);

        if (category == null) {
            category = new Category();
            category.setCategoryName(categoryName);
            category.setTenantId(tenantId);
            category.setTenantName(tenantName);
            category.setCreateBy(operator);
            category.setCreateTime(new Date());
            category.setIsSys(0); // 0-否
            categoryMapper.insert(category);
        }
        return category;
    }

    /**
     * 步骤 2 & 5: 创建或更新产品，并处理物模型
     */
    private Product createOrUpdateProduct(AiRequest.ProductInfo productInfo, Long categoryId, Long tenantId, String tenantName, String operator) {
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Product::getProductName, productInfo.getProductName())
                .eq(Product::getTenantId, tenantId);
        Product product = productMapper.selectOne(queryWrapper);

        if (product == null) {
            product = new Product();
            BeanUtils.copyProperties(productInfo, product); // 复制基础属性
            product.setProductName(productInfo.getProductName());
            product.setCategoryId(categoryId);
            product.setTenantId(tenantId);
            product.setTenantName(tenantName);
            product.setCreateBy(operator);
            product.setCreateTime(new Date());
            product.setStatus(1); // 1-未发布
            // 步骤 5: 处理物模型
            if (!CollectionUtils.isEmpty(productInfo.getProperties())) {
                product.setThingsModelsJson(JSON.toJSONString(productInfo.getProperties()));
            }
            productMapper.insert(product);
        } else {
            // 更新现有产品
            product.setUpdateBy(operator);
            product.setUpdateTime(new Date());
            // 步骤 5: 更新物模型（这里可以实现更复杂的合并逻辑）
            if (!CollectionUtils.isEmpty(productInfo.getProperties())) {
                // 简单覆盖或实现合并逻辑
                product.setThingsModelsJson(JSON.toJSONString(productInfo.getProperties()));
            }
            productMapper.updateById(product);
        }
        return product;
    }

    /**
     * 步骤 3: 查找或创建第三方平台
     */
    private ApiThirdPartyPlatform findOrCreateThirdPartyPlatform(AiRequest.AuthenticationInfo authInfo, String operator) {
        LambdaQueryWrapper<ApiThirdPartyPlatform> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiThirdPartyPlatform::getPlatformCode, authInfo.getPlatformId());
        ApiThirdPartyPlatform platform = apiThirdPartyPlatformMapper.selectOne(queryWrapper);

        if (platform == null) {
            platform = new ApiThirdPartyPlatform();
            platform.setPlatformCode(authInfo.getPlatformId());
            platform.setPlatformName(authInfo.getPlatformId()); // 默认使用ID作为名称
            platform.setStatus(1); // 1:启用
            platform.setCreateTime(new Date());
            // 其他字段可以从AI请求中获取或设置默认值
            apiThirdPartyPlatformMapper.insert(platform);
        }
        return platform;
    }

    /**
     * 步骤 4 & 8: 创建或更新API定义及其参数，并处理关联关系
     */
    private void createOrUpdateApiDefinition(AiRequest.GeneratedApiList generatedApi, ApiThirdPartyPlatform platform, String operator) {
        // 假设使用URL和方法作为API的唯一标识
        LambdaQueryWrapper<ApiDefinition> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiDefinition::getApiUrl, generatedApi.getApiUrl())
                .eq(ApiDefinition::getMethod, generatedApi.getMethod());
        ApiDefinition apiDefinition = apiDefinitionMapper.selectOne(queryWrapper);

        if (apiDefinition == null) {
            apiDefinition = new ApiDefinition();
            apiDefinition.setApiName(generatedApi.getApiName());
            apiDefinition.setApiUrl(generatedApi.getApiUrl());
            apiDefinition.setMethod(generatedApi.getMethod());
            apiDefinition.setCreateBy(operator);
            apiDefinition.setCreateTime(new Date());
            if (platform != null) {
                apiDefinition.setPlatformId(platform.getId());
            }
            // 其他字段...
            apiDefinitionMapper.insert(apiDefinition);
        }

        // 处理API参数
        if (!CollectionUtils.isEmpty(generatedApi.getApiParamDetailList())) {
            Long apiId = apiDefinition.getId();
            // 先删除旧的参数，再插入新的，避免重复
            apiParamDetailMapper.delete(new LambdaQueryWrapper<ApiParamDetail>().eq(ApiParamDetail::getApiId, apiId));

            for (AiRequest.GeneratedApiParameterDetail paramDetail : generatedApi.getApiParamDetailList()) {
                ApiParamDetail dbParam = new ApiParamDetail();
                BeanUtils.copyProperties(paramDetail, dbParam); // 复制属性
                dbParam.setApiId(apiId);
                dbParam.setCreateBy(operator);
                dbParam.setCreateTime(new Date());
                apiParamDetailMapper.insert(dbParam);
            }
        }
    }

    /**
     * 步骤 6: 创建或更新解析脚本
     */
    private void createOrUpdateApiScript(AiRequest.ParsingProtocol protocol, Long productId, String operator) {
        LambdaQueryWrapper<ApiScript> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiScript::getScriptName, protocol.getName())
                .eq(ApiScript::getProductId, productId);
        ApiScript script = apiScriptMapper.selectOne(queryWrapper);

        if (script == null) {
            script = new ApiScript();
            script.setScriptName(protocol.getName());
            script.setScriptData(protocol.getScript());
            script.setProductId(productId);
            script.setCreateBy(operator);
            script.setCreateTime(new Date());
            script.setScriptLanguage("js"); // 默认为JavaScript
            script.setEnable(1); // 1-生效
            apiScriptMapper.insert(script);

            // 关键点：如果需要，在此处调用LiteFlow的API动态注册脚本
            // liteflowClient.reloadRule();
        }
    }

    /**
     * 步骤 7: 创建或更新设备
     */
    private void createOrUpdateDevice(AiRequest.DeviceInfo deviceInfo, Product product, Long tenantId, String tenantName, String operator) {
        // 在 iot_device 表中查找设备
        Device device = deviceMapper.selectDeviceBySerialNumber(deviceInfo.getDeviceCode());
        if (device == null) {
            device = new Device();
            device.setDeviceName(deviceInfo.getDeviceName());
            device.setSerialNumber(deviceInfo.getDeviceCode());
            device.setProductId(product.getProductId());
            device.setProductName(product.getProductName());
            device.setTenantId(tenantId);
            device.setTenantName(tenantName);
            device.setCreateBy(operator);
            device.setCreateTime(new Date());
            device.setStatus(1); // 1-未激活
            deviceMapper.insertDevice(device);
        }

        // 在 api_device 中间表中查找或创建记录
        ApiDevice apiDevice = apiDeviceMapper.selectById(device.getDeviceId());
        if(apiDevice == null){
            apiDevice = new ApiDevice();
            BeanUtils.copyProperties(device, apiDevice);
            // 这里可以设置一些ApiDevice特有的字段
            apiDeviceMapper.insert(apiDevice);
        }
    }
}
