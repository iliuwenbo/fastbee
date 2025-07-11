package com.fastbee.platform.service;

import java.util.List;
import com.fastbee.platform.domain.SceneMiddleLog;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 中包执行日志Service接口
 *
 * @author lwb
 * @date 2025-06-04
 */
public interface ISceneMiddleLogService extends IService<SceneMiddleLog>
{

    /**
     * 查询中包执行日志列表
     *
     * @param sceneMiddleLog 中包执行日志
     * @return 中包执行日志集合
     */
     List<SceneMiddleLog> selectSceneMiddleLogList(SceneMiddleLog sceneMiddleLog);

    /**
     * 查询中包执行日志
     *
     * @param id 主键
     * @return 中包执行日志
     */
     SceneMiddleLog selectSceneMiddleLogById(Long id);

    /**
     * 查询中包执行日志
     *
     * @param id 主键
     * @return 中包执行日志
     */
    SceneMiddleLog queryByIdWithCache(Long id);

    /**
     * 新增中包执行日志
     *
     * @param sceneMiddleLog 中包执行日志
     * @return 是否新增成功
     */
    Boolean insertWithCache(SceneMiddleLog sceneMiddleLog);

    /**
     * 修改中包执行日志
     *
     * @param sceneMiddleLog 中包执行日志
     * @return 是否修改成功
     */
    Boolean updateWithCache(SceneMiddleLog sceneMiddleLog);

    /**
     * 校验并批量删除中包执行日志信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithCacheByIds(Long[] ids, Boolean isValid);

}
