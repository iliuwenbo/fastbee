package com.fastbee.platform.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fastbee.common.utils.DateUtils;
import org.springframework.stereotype.Service;
import java.util.Objects;
import com.fastbee.common.utils.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import com.fastbee.platform.mapper.ApiParamDetailMapper;
import com.fastbee.platform.domain.ApiParamDetail;
import com.fastbee.platform.service.IApiParamDetailService;

/**
 * API 参数详情Service业务层处理
 *
 * @author lwb
 * @date 2025-04-27
 */
@Service
public class ApiParamDetailServiceImpl extends ServiceImpl<ApiParamDetailMapper,ApiParamDetail> implements IApiParamDetailService {

    /**
     * 查询API 参数详情
     *
     * @param id 主键
     * @return API 参数详情
     */
    @Override
    @Cacheable(cacheNames = "ApiParamDetail", key = "#id")
    // 查询时更新key缓存，更新和删除时删除缓存，新增时不更新，下一次查询会更新缓存
    public ApiParamDetail queryByIdWithCache(Long id){
        return this.getById(id);
    }

    /**
     * 查询API 参数详情
     *
     * @param id 主键
     * @return API 参数详情
     */
    @Override
    @Cacheable(cacheNames = "ApiParamDetail", key = "#id")
    // 查询时更新key缓存，更新和删除时删除缓存，新增时不更新，下一次查询会更新缓存
    public ApiParamDetail selectApiParamDetailById(Long id){
        return this.getById(id);
    }

    /**
     * 查询API 参数详情列表
     *
     * @param apiParamDetail API 参数详情
     * @return API 参数详情
     */
    @Override
    public List<ApiParamDetail> selectApiParamDetailList(ApiParamDetail apiParamDetail) {
        LambdaQueryWrapper<ApiParamDetail> lqw = buildQueryWrapper(apiParamDetail);
        return baseMapper.selectList(lqw);
    }

    private LambdaQueryWrapper<ApiParamDetail> buildQueryWrapper(ApiParamDetail query) {
        Map<String, Object> params = query.getParams();
        LambdaQueryWrapper<ApiParamDetail> lqw = Wrappers.lambdaQuery();
                    lqw.eq(query.getApiId() != null, ApiParamDetail::getApiId, query.getApiId());
                    lqw.eq(StringUtils.isNotBlank(query.getParamType()), ApiParamDetail::getParamType, query.getParamType());
                    lqw.like(StringUtils.isNotBlank(query.getParamName()), ApiParamDetail::getParamName, query.getParamName());
                    lqw.eq(StringUtils.isNotBlank(query.getParamValue()), ApiParamDetail::getParamValue, query.getParamValue());
                    lqw.eq(query.getRequired() != null, ApiParamDetail::getRequired, query.getRequired());

        if (!Objects.isNull(params.get("beginTime")) &&
        !Objects.isNull(params.get("endTime"))) {
            lqw.between(ApiParamDetail::getCreateTime, params.get("beginTime"), params.get("endTime"));
        }
        return lqw;
    }

    /**
     * 新增API 参数详情
     *
     * @param add API 参数详情
     * @return 是否新增成功
     */
    @Override
    public Boolean insertWithCache(ApiParamDetail add) {
        validEntityBeforeSave(add);
        return this.save(add);
    }

    /**
     * 修改API 参数详情
     *
     * @param update API 参数详情
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(cacheNames = "ApiParamDetail", key = "#update.id")
    public Boolean updateWithCache(ApiParamDetail update) {
        validEntityBeforeSave(update);
        return this.updateById(update);
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ApiParamDetail entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除API 参数详情信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    @CacheEvict(cacheNames = "ApiParamDetail", keyGenerator = "deleteKeyGenerator" )
    public Boolean deleteWithCacheByIds(Long[] ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return this.removeByIds(Arrays.asList(ids));
    }

}
