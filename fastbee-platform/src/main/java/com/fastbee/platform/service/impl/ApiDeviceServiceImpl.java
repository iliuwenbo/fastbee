package com.fastbee.platform.service.impl;

import java.util.*;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fastbee.common.exception.base.BaseException;
import com.fastbee.common.utils.DateUtils;
import com.fastbee.iot.domain.Device;
import com.fastbee.iot.service.IDeviceService;
import com.fastbee.iot.service.impl.DeviceUserServiceImpl;
import com.fastbee.platform.domain.ApiDefinition;
import com.fastbee.platform.handler.DynamicApiClient;
import com.fastbee.platform.service.IApiDefinitionService;
import com.fastbee.script.domain.ApiScript;
import com.fastbee.script.model.ApiScriptCondition;
import com.fastbee.script.ruleEngine.ApiMsgContext;
import com.fastbee.script.service.IApiScriptService;
import com.yomahub.liteflow.builder.LiteFlowNodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import com.fastbee.common.utils.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import com.fastbee.platform.mapper.ApiDeviceMapper;
import com.fastbee.platform.domain.ApiDevice;
import com.fastbee.platform.service.IApiDeviceService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 平台接入设备中间Service业务层处理
 *
 * @author lwb
 * @date 2025-04-27
 */
@Service
public class ApiDeviceServiceImpl extends ServiceImpl<ApiDeviceMapper,ApiDevice> implements IApiDeviceService {
    @Autowired
    private IDeviceService deviceService;
    @Resource
    private IApiDefinitionService apiDefinitionService;
    @Resource
    private DynamicApiClient dynamicApiClient;
    @Resource
    private IApiScriptService scriptService;
    @Resource
    private IApiDeviceService apiDeviceService;
    /**
     * 查询平台接入设备中间
     *
     * @param id 主键
     * @return 平台接入设备中间
     */
    @Override
    @Cacheable(cacheNames = "ApiDevice", key = "#id")
    // 查询时更新key缓存，更新和删除时删除缓存，新增时不更新，下一次查询会更新缓存
    public ApiDevice queryByIdWithCache(String id){
        return this.getById(id);
    }

    /**
     * 查询平台接入设备中间
     *
     * @param id 主键
     * @return 平台接入设备中间
     */
    @Override
    @Cacheable(cacheNames = "ApiDevice", key = "#id")
    // 查询时更新key缓存，更新和删除时删除缓存，新增时不更新，下一次查询会更新缓存
    public ApiDevice selectApiDeviceById(String id){
        return this.getById(id);
    }

    /**
     * 查询平台接入设备中间列表
     *
     * @param apiDevice 平台接入设备中间
     * @return 平台接入设备中间
     */
    @Override
    public List<ApiDevice> selectApiDeviceList(ApiDevice apiDevice) {
        LambdaQueryWrapper<ApiDevice> lqw = buildQueryWrapper(apiDevice);
        return baseMapper.selectList(lqw);
    }

    private LambdaQueryWrapper<ApiDevice> buildQueryWrapper(ApiDevice query) {
        LambdaQueryWrapper<ApiDevice> lqw = Wrappers.lambdaQuery();
                    lqw.eq(StringUtils.isNotBlank(query.getManufacturerId()), ApiDevice::getManufacturerId, query.getManufacturerId());
                    lqw.like(StringUtils.isNotBlank(query.getDeviceName()), ApiDevice::getDeviceName, query.getDeviceName());
                    lqw.eq(query.getDeviceType() != null, ApiDevice::getDeviceType, query.getDeviceType());
                    lqw.eq(query.getIsBinding() != null, ApiDevice::getIsBinding, query.getIsBinding());

        return lqw;
    }

    /**
     * 新增平台接入设备中间
     *
     * @param add 平台接入设备中间
     * @return 是否新增成功
     */
    @Override
    public Boolean insertWithCache(ApiDevice add) {
        validEntityBeforeSave(add);
        return this.save(add);
    }

    /**
     * 修改平台接入设备中间
     *
     * @param update 平台接入设备中间
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(cacheNames = "ApiDevice", key = "#update.id")
    public Boolean updateWithCache(ApiDevice update) {
        validEntityBeforeSave(update);
        return this.updateById(update);
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ApiDevice entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除平台接入设备中间信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    @CacheEvict(cacheNames = "ApiDevice", keyGenerator = "deleteKeyGenerator" )
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithCacheByIds(String[] ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return this.removeByIds(Arrays.asList(ids));
    }

}
