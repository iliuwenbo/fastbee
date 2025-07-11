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
import com.fastbee.platform.mapper.SceneMiddleLogMapper;
import com.fastbee.platform.domain.SceneMiddleLog;
import com.fastbee.platform.service.ISceneMiddleLogService;

/**
 * 中包执行日志Service业务层处理
 *
 * @author lwb
 * @date 2025-06-04
 */
@Service
public class SceneMiddleLogServiceImpl extends ServiceImpl<SceneMiddleLogMapper,SceneMiddleLog> implements ISceneMiddleLogService {

    /**
     * 查询中包执行日志
     *
     * @param id 主键
     * @return 中包执行日志
     */
    @Override
    @Cacheable(cacheNames = "SceneMiddleLog", key = "#id")
    // 查询时更新key缓存，更新和删除时删除缓存，新增时不更新，下一次查询会更新缓存
    public SceneMiddleLog queryByIdWithCache(Long id){
        return this.getById(id);
    }

    /**
     * 查询中包执行日志
     *
     * @param id 主键
     * @return 中包执行日志
     */
    @Override
    @Cacheable(cacheNames = "SceneMiddleLog", key = "#id")
    // 查询时更新key缓存，更新和删除时删除缓存，新增时不更新，下一次查询会更新缓存
    public SceneMiddleLog selectSceneMiddleLogById(Long id){
        return this.getById(id);
    }

    /**
     * 查询中包执行日志列表
     *
     * @param sceneMiddleLog 中包执行日志
     * @return 中包执行日志
     */
    @Override
    public List<SceneMiddleLog> selectSceneMiddleLogList(SceneMiddleLog sceneMiddleLog) {
        LambdaQueryWrapper<SceneMiddleLog> lqw = buildQueryWrapper(sceneMiddleLog);
        return baseMapper.selectList(lqw);
    }

    private LambdaQueryWrapper<SceneMiddleLog> buildQueryWrapper(SceneMiddleLog query) {
        Map<String, Object> params = query.getParams();
        LambdaQueryWrapper<SceneMiddleLog> lqw = Wrappers.lambdaQuery();
                    lqw.eq(query.getMiddleId() != null, SceneMiddleLog::getMiddleId, query.getMiddleId());
                    lqw.eq(query.getSceneId() != null, SceneMiddleLog::getSceneId, query.getSceneId());
                    lqw.eq(StringUtils.isNotBlank(query.getIdentify()), SceneMiddleLog::getIdentify, query.getIdentify());
                    lqw.eq(StringUtils.isNotBlank(query.getMessageId()), SceneMiddleLog::getMessageId, query.getMessageId());
                    lqw.eq(StringUtils.isNotBlank(query.getMessageType()), SceneMiddleLog::getMessageType, query.getMessageType());
                    lqw.eq(StringUtils.isNotBlank(query.getMessageValue()), SceneMiddleLog::getMessageValue, query.getMessageValue());
                    lqw.like(StringUtils.isNotBlank(query.getDeviceName()), SceneMiddleLog::getDeviceName, query.getDeviceName());
                    lqw.eq(StringUtils.isNotBlank(query.getSerialNumber()), SceneMiddleLog::getSerialNumber, query.getSerialNumber());
                    lqw.eq(query.getMode() != null, SceneMiddleLog::getMode, query.getMode());
                    lqw.eq(query.getReportType() != null, SceneMiddleLog::getReportType, query.getReportType());
                    lqw.eq(query.getPreviousId() != null, SceneMiddleLog::getPreviousId, query.getPreviousId());


        if (!Objects.isNull(params.get("beginTime")) &&
        !Objects.isNull(params.get("endTime"))) {
            lqw.between(SceneMiddleLog::getCreateTime, params.get("beginTime"), params.get("endTime"));
        }
        return lqw;
    }

    /**
     * 新增中包执行日志
     *
     * @param add 中包执行日志
     * @return 是否新增成功
     */
    @Override
    public Boolean insertWithCache(SceneMiddleLog add) {
        validEntityBeforeSave(add);
        return this.save(add);
    }

    /**
     * 修改中包执行日志
     *
     * @param update 中包执行日志
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(cacheNames = "SceneMiddleLog", key = "#update.id")
    public Boolean updateWithCache(SceneMiddleLog update) {
        validEntityBeforeSave(update);
        return this.updateById(update);
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SceneMiddleLog entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除中包执行日志信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    @CacheEvict(cacheNames = "SceneMiddleLog", keyGenerator = "deleteKeyGenerator" )
    public Boolean deleteWithCacheByIds(Long[] ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return this.removeByIds(Arrays.asList(ids));
    }

}
