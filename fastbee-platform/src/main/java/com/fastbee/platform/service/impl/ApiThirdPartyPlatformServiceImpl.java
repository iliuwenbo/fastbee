package com.fastbee.platform.service.impl;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import com.fastbee.common.utils.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import com.fastbee.platform.mapper.ApiThirdPartyPlatformMapper;
import com.fastbee.platform.domain.ApiThirdPartyPlatform;
import com.fastbee.platform.service.IApiThirdPartyPlatformService;

/**
 * 第三方平台信息Service业务层处理
 *
 * @author lwb
 * @date 2025-06-04
 */
@Service
public class ApiThirdPartyPlatformServiceImpl extends ServiceImpl<ApiThirdPartyPlatformMapper,ApiThirdPartyPlatform> implements IApiThirdPartyPlatformService {


    /**
     * 查询第三方平台信息
     *
     * @param id 主键
     * @return 第三方平台信息
     */
    @Override
    @Cacheable(cacheNames = "ApiThirdPartyPlatform", key = "#id")
    // 查询时更新key缓存，更新和删除时删除缓存，新增时不更新，下一次查询会更新缓存
    public ApiThirdPartyPlatform queryByIdWithCache(Long id){
        return this.getById(id);
    }

    /**
     * 查询第三方平台信息
     *
     * @param id 主键
     * @return 第三方平台信息
     */
    @Override
    @Cacheable(cacheNames = "ApiThirdPartyPlatform", key = "#id")
    // 查询时更新key缓存，更新和删除时删除缓存，新增时不更新，下一次查询会更新缓存
    public ApiThirdPartyPlatform selectApiThirdPartyPlatformById(Long id){
        return this.getById(id);
    }

    /**
     * 查询第三方平台信息列表
     *
     * @param apiThirdPartyPlatform 第三方平台信息
     * @return 第三方平台信息
     */
    @Override
    public List<ApiThirdPartyPlatform> selectApiThirdPartyPlatformList(ApiThirdPartyPlatform apiThirdPartyPlatform) {
        LambdaQueryWrapper<ApiThirdPartyPlatform> lqw = buildQueryWrapper(apiThirdPartyPlatform);
        return baseMapper.selectList(lqw);
    }

    private LambdaQueryWrapper<ApiThirdPartyPlatform> buildQueryWrapper(ApiThirdPartyPlatform query) {
        Map<String, Object> params = query.getParams();
        LambdaQueryWrapper<ApiThirdPartyPlatform> lqw = Wrappers.lambdaQuery();
                    lqw.eq(StringUtils.isNotBlank(query.getPlatformCode()), ApiThirdPartyPlatform::getPlatformCode, query.getPlatformCode());
                    lqw.like(StringUtils.isNotBlank(query.getPlatformName()), ApiThirdPartyPlatform::getPlatformName, query.getPlatformName());
                    lqw.eq(StringUtils.isNotBlank(query.getPlatformType()), ApiThirdPartyPlatform::getPlatformType, query.getPlatformType());
                    lqw.eq(StringUtils.isNotBlank(query.getLogoUrl()), ApiThirdPartyPlatform::getLogoUrl, query.getLogoUrl());
                    lqw.eq(StringUtils.isNotBlank(query.getOfficialWebsite()), ApiThirdPartyPlatform::getOfficialWebsite, query.getOfficialWebsite());
                    lqw.eq(StringUtils.isNotBlank(query.getApiEndpoint()), ApiThirdPartyPlatform::getApiEndpoint, query.getApiEndpoint());
                    lqw.eq(StringUtils.isNotBlank(query.getAppKey()), ApiThirdPartyPlatform::getAppKey, query.getAppKey());
                    lqw.eq(StringUtils.isNotBlank(query.getAppSecret()), ApiThirdPartyPlatform::getAppSecret, query.getAppSecret());
                    lqw.eq(query.getStatus() != null, ApiThirdPartyPlatform::getStatus, query.getStatus());

        if (!Objects.isNull(params.get("beginTime")) &&
        !Objects.isNull(params.get("endTime"))) {
            lqw.between(ApiThirdPartyPlatform::getCreateTime, params.get("beginTime"), params.get("endTime"));
        }
        return lqw;
    }

    /**
     * 新增第三方平台信息
     *
     * @param add 第三方平台信息
     * @return 是否新增成功
     */
    @Override
    public Boolean insertWithCache(ApiThirdPartyPlatform add) {
        validEntityBeforeSave(add);
        return this.save(add);
    }

    /**
     * 修改第三方平台信息
     *
     * @param update 第三方平台信息
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(cacheNames = "ApiThirdPartyPlatform", key = "#update.id")
    public Boolean updateWithCache(ApiThirdPartyPlatform update) {
        validEntityBeforeSave(update);
        return this.updateById(update);
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ApiThirdPartyPlatform entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除第三方平台信息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    @CacheEvict(cacheNames = "ApiThirdPartyPlatform", keyGenerator = "deleteKeyGenerator" )
    public Boolean deleteWithCacheByIds(Long[] ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return this.removeByIds(Arrays.asList(ids));
    }
}
