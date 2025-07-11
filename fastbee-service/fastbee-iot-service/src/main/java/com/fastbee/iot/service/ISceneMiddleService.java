package com.fastbee.iot.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fastbee.iot.domain.SceneMiddle;

/**
 * 场景中包Service接口
 *
 * @author lwb
 * @date 2025-05-22
 */
public interface ISceneMiddleService extends IService<SceneMiddle>
{

    /**
     * 查询场景中包列表
     *
     * @param sceneMiddle 场景中包
     * @return 场景中包集合
     */
     List<SceneMiddle> selectSceneMiddleList(SceneMiddle sceneMiddle);

    /**
     * 查询场景中包
     *
     * @param middleId 主键
     * @return 场景中包
     */
     SceneMiddle selectSceneMiddleById(Long middleId);

    /**
     * 查询场景中包
     *
     * @param middleId 主键
     * @return 场景中包
     */
    SceneMiddle queryByIdWithCache(Long middleId);

    /**
     * 新增场景中包
     *
     * @param sceneMiddle 场景中包
     * @return 是否新增成功
     */
    Boolean insertWithCache(SceneMiddle sceneMiddle);

    /**
     * 修改场景中包
     *
     * @param sceneMiddle 场景中包
     * @return 是否修改成功
     */
    Boolean updateWithCache(SceneMiddle sceneMiddle);

    Boolean updateBySceneId(Long sceneId);


    /**
     * 校验并批量删除场景中包信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithCacheByIds(Long[] ids, Boolean isValid);

}
