package com.fastbee.iot.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fastbee.common.exception.ServiceException;
import com.fastbee.common.utils.spring.SpringUtils;
import com.fastbee.iot.domain.Scene;
import com.fastbee.iot.domain.SceneMiddle;
import com.fastbee.iot.mapper.SceneMapper;
import com.fastbee.iot.mapper.SceneMiddleMapper;
import com.fastbee.iot.service.ISceneMiddleService;
import com.fastbee.iot.util.LiteFlowJsonParser;
import com.yomahub.liteflow.builder.el.LiteFlowChainELBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.fastbee.common.utils.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import javax.annotation.Resource;

/**
 * 场景中包Service业务层处理
 *
 * @author lwb
 * @date 2025-05-22
 */
@Service
@Order(5)
@Log4j2
public class SceneMiddleServiceImpl extends ServiceImpl<SceneMiddleMapper, SceneMiddle> implements ISceneMiddleService {

    @Resource
    private SceneMapper sceneMapper;
    public void init() {
        List<SceneMiddle> sceneMiddles = selectSceneMiddleList(new SceneMiddle());
        for (SceneMiddle sceneMiddle : sceneMiddles) {
            try {
                LiteFlowChainELBuilder.createChain().setChainName(sceneMiddle.getChainName()).setEL(sceneMiddle.getElData()).build();
            }catch (Exception e){
                log.error("初始化中包规则链失败，请联系管理员检查场景建设列表是否存在错误:{},{}" ,sceneMiddle.getMiddleId(),sceneMiddle.getMiddleName());
            }
        }
    }

    /**
     * 查询场景中包
     *
     * @param middleId 主键
     * @return 场景中包
     */
    @Override
    @Cacheable(cacheNames = "SceneMiddle", key = "#middleId")
    // 查询时更新key缓存，更新和删除时删除缓存，新增时不更新，下一次查询会更新缓存
    public SceneMiddle queryByIdWithCache(Long middleId) {
        return this.getById(middleId);
    }

    /**
     * 查询场景中包
     *
     * @param middleId 主键
     * @return 场景中包
     */
    @Override
    @Cacheable(cacheNames = "SceneMiddle", key = "#middleId")
    // 查询时更新key缓存，更新和删除时删除缓存，新增时不更新，下一次查询会更新缓存
    public SceneMiddle selectSceneMiddleById(Long middleId) {
        return this.getById(middleId);
    }

    /**
     * 查询场景中包列表
     *
     * @param sceneMiddle 场景中包
     * @return 场景中包
     */
    @Override
    public List<SceneMiddle> selectSceneMiddleList(SceneMiddle sceneMiddle) {
        LambdaQueryWrapper<SceneMiddle> lqw = buildQueryWrapper(sceneMiddle);
        List<SceneMiddle> sceneMiddles = baseMapper.selectList(lqw);
        //将小包name返回到列表中
        for (SceneMiddle middle : sceneMiddles) {
            String sceneIdsStr = middle.getIotSceneIds();
            if (sceneIdsStr == null) continue;

            List<Long> sceneIds = Arrays.stream(sceneIdsStr.split(",")).filter(s -> !s.isEmpty()).map(Long::valueOf).collect(Collectors.toList());

            if (!sceneIds.isEmpty()) {
                String names = sceneMapper.selectList(
                                Wrappers.<Scene>lambdaQuery().in(Scene::getSceneId, sceneIds).select(Scene::getSceneName)
                        ).stream().map(Scene::getSceneName).filter(Objects::nonNull).collect(Collectors.joining(","));
                middle.setIotSceneNames(names);
            }
        }
        return sceneMiddles;
    }

    private LambdaQueryWrapper<SceneMiddle> buildQueryWrapper(SceneMiddle query) {
        Map<String, Object> params = query.getParams();
        LambdaQueryWrapper<SceneMiddle> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(query.getMiddleName()), SceneMiddle::getMiddleName, query.getMiddleName());
        lqw.eq(query.getEnable() != null, SceneMiddle::getEnable, query.getEnable());
        lqw.eq(query.getMiddleId() != null, SceneMiddle::getMiddleId, query.getMiddleId());
        lqw.eq(StringUtils.isNotBlank(query.getIotSceneIds()), SceneMiddle::getIotSceneIds, query.getIotSceneIds());
        lqw.eq(StringUtils.isNotBlank(query.getElData()), SceneMiddle::getElData, query.getElData());
        lqw.eq(query.getIotSceneId() != null, SceneMiddle::getIotSceneId, query.getIotSceneId());

        if (!Objects.isNull(params.get("beginTime")) &&
                !Objects.isNull(params.get("endTime"))) {
            lqw.between(SceneMiddle::getCreateTime, params.get("beginTime"), params.get("endTime"));
        }
        return lqw;
    }

    /**
     * 新增场景中包
     *
     * @param add 场景中包
     * @return 是否新增成功
     */
    @Override
    public Boolean insertWithCache(SceneMiddle add) {
        validEntityBeforeSave(add);

        // TODO 新增中包场景
        try {
            buildElData result = getBuildElData(add);
            add.setChainName(result.chainId);
            add.setElData(result.elData);
            add.setJson(JSONUtil.toJsonStr(add).trim());
        } catch (Exception e) {
            log.error("中包场景解析失败，请检查配置",e);
            throw new ServiceException("中包场景解析失败，请检查配置");
        }

        return this.save(add);
    }


    /**
     * 修改场景中包
     *
     * @param update 场景中包
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(cacheNames = "SceneMiddle", key = "#update.middleId")
    public Boolean updateWithCache(SceneMiddle update) {
        validEntityBeforeSave(update);

        try {
            buildElData result = getBuildElData(update);
            update.setChainName(result.chainId);
            update.setElData(result.elData);
            update.setJson(JSONUtil.toJsonStr(update).trim());
        } catch (Exception e) {
            log.error("中包场景解析失败，请检查配置",e);
            throw new ServiceException("中包场景解析失败，请检查配置");
        }

        return this.updateById(update);
    }


    /**
     * 通过小包更新场景中包
     *
     * @param sceneId 小包id
     * @return 是否修改成功
     */
    @Override
    public Boolean updateBySceneId(Long sceneId) {
        LambdaQueryWrapper<SceneMiddle> lqw = Wrappers.lambdaQuery();
        lqw.like(SceneMiddle::getIotSceneIds, sceneId);
        List<SceneMiddle> sceneMiddles = baseMapper.selectList(lqw);
        int count = 0;
        for (SceneMiddle update : sceneMiddles) {
            try {
                buildElData result = getBuildElData(update);
                update.setChainName(result.chainId);
                update.setElData(result.elData);
                update.setJson(JSONUtil.toJsonStr(update).trim());
                count += baseMapper.updateById(update);
            } catch (Exception e) {
                log.error("中包场景解析失败，请检查配置",e);
                throw new ServiceException("中包场景解析失败，请检查配置");
            }
        }
        return count > 0;
    }


    private static buildElData getBuildElData(SceneMiddle add) {
        FlowNodeElDataService flowNodeElDataService = SpringUtils.getBean(FlowNodeElDataService.class);
        flowNodeElDataService.fillFlowNodeData(add);
        String elData = LiteFlowJsonParser.parseNode(add.getFlowNode());
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        String chainId = "Z" + snowflake.nextId();
        LiteFlowChainELBuilder.createChain().setChainName(chainId).setEL(elData).build();
        return new buildElData(elData, chainId);
    }

    private static class buildElData {
        public final String elData;
        public final String chainId;

        public buildElData(String elData, String chainId) {
            this.elData = elData;
            this.chainId = chainId;
        }
    }
    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SceneMiddle entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除场景中包信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    @CacheEvict(cacheNames = "SceneMiddle", keyGenerator = "deleteKeyGenerator")
    public Boolean deleteWithCacheByIds(Long[] ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return this.removeByIds(Arrays.asList(ids));
    }

}
