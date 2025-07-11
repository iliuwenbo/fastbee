package com.fastbee.iot.service;

import java.util.List;
import java.util.Set;

import com.fastbee.iot.domain.Scene;
import com.fastbee.iot.model.SceneTerminalUserVO;

/**
 * 场景联动Service接口
 *
 * @author kerwincui
 * @date 2022-01-13
 */
public interface ISceneService
{
    /**
     * 查询场景联动
     *
     * @param sceneId 场景联动主键
     * @return 场景联动
     */
    public Scene selectSceneBySceneId(Long sceneId);

    /**
     * 查询场景联动列表
     *
     * @param scene 场景联动
     * @return 场景联动集合
     */
    public List<Scene> selectSceneList(Scene scene);

    /**
     * 新增场景联动
     *
     * @param scene 场景联动
     * @return 结果
     */
    public int insertScene(Scene scene);


    /**
     * 修改场景联动
     *
     * @param scene 场景联动
     * @return 结果
     */
    public int updateScene(Scene scene);

    /**
     * 批量删除场景联动
     *
     * @param sceneIds 需要删除的场景联动主键集合
     * @return 结果
     */
    public int deleteSceneBySceneIds(Long[] sceneIds);

    /**
     * 删除场景联动信息
     *
     * @param sceneId 场景联动主键
     * @return 结果
     */
    public int deleteSceneBySceneId(Long sceneId);

    /**
     * 修改场景联动状态
     * @param scene 场景联动
     * @return int
     */
    int updateStatus(Scene scene);

    public void openSceneLog(String chainName);
    public void closeSceneLog(String chainName);

    /**
     * 查询场景用户信息
     * @param sceneIdSet 场景id
     * @return java.util.List<com.fastbee.iot.model.SceneTerminalUserVO>
     */
    List<SceneTerminalUserVO> selectTerminalUserBySceneIds(Set<Long> sceneIdSet);
}
