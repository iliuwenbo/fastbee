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
import com.fastbee.platform.mapper.ApiRequestExampleMapper;
import com.fastbee.platform.domain.ApiRequestExample;
import com.fastbee.platform.service.IApiRequestExampleService;

/**
 * API 请求示例Service业务层处理
 *
 * @author lwb
 * @date 2025-04-27
 */
@Service
public class ApiRequestExampleServiceImpl extends ServiceImpl<ApiRequestExampleMapper,ApiRequestExample> implements IApiRequestExampleService {

    /**
     * 查询API 请求示例
     *
     * @param id 主键
     * @return API 请求示例
     */
    @Override
    @Cacheable(cacheNames = "ApiRequestExample", key = "#id")
    // 查询时更新key缓存，更新和删除时删除缓存，新增时不更新，下一次查询会更新缓存
    public ApiRequestExample queryByIdWithCache(Long id){
        return this.getById(id);
    }

    /**
     * 查询API 请求示例
     *
     * @param id 主键
     * @return API 请求示例
     */
    @Override
    @Cacheable(cacheNames = "ApiRequestExample", key = "#id")
    // 查询时更新key缓存，更新和删除时删除缓存，新增时不更新，下一次查询会更新缓存
    public ApiRequestExample selectApiRequestExampleById(Long id){
        return this.getById(id);
    }

    /**
     * 查询API 请求示例列表
     *
     * @param apiRequestExample API 请求示例
     * @return API 请求示例
     */
    @Override
    public List<ApiRequestExample> selectApiRequestExampleList(ApiRequestExample apiRequestExample) {
        LambdaQueryWrapper<ApiRequestExample> lqw = buildQueryWrapper(apiRequestExample);
        return baseMapper.selectList(lqw);
    }

    private LambdaQueryWrapper<ApiRequestExample> buildQueryWrapper(ApiRequestExample query) {
        Map<String, Object> params = query.getParams();
        LambdaQueryWrapper<ApiRequestExample> lqw = Wrappers.lambdaQuery();
                    lqw.eq(query.getApiId() != null, ApiRequestExample::getApiId, query.getApiId());
                    lqw.eq(StringUtils.isNotBlank(query.getRequestExample()), ApiRequestExample::getRequestExample, query.getRequestExample());
                    lqw.eq(StringUtils.isNotBlank(query.getRequestHeadersExample()), ApiRequestExample::getRequestHeadersExample, query.getRequestHeadersExample());

        if (!Objects.isNull(params.get("beginTime")) &&
        !Objects.isNull(params.get("endTime"))) {
            lqw.between(ApiRequestExample::getCreateTime, params.get("beginTime"), params.get("endTime"));
        }
        return lqw;
    }

    /**
     * 新增API 请求示例
     *
     * @param add API 请求示例
     * @return 是否新增成功
     */
    @Override
    public Boolean insertWithCache(ApiRequestExample add) {
        validEntityBeforeSave(add);
        return this.save(add);
    }

    /**
     * 修改API 请求示例
     *
     * @param update API 请求示例
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(cacheNames = "ApiRequestExample", key = "#update.id")
    public Boolean updateWithCache(ApiRequestExample update) {
        validEntityBeforeSave(update);
        return this.updateById(update);
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ApiRequestExample entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除API 请求示例信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    @CacheEvict(cacheNames = "ApiRequestExample", keyGenerator = "deleteKeyGenerator" )
    public Boolean deleteWithCacheByIds(Long[] ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return this.removeByIds(Arrays.asList(ids));
    }

}
