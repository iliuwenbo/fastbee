package com.fastbee.iot.mapper;

import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fastbee.framework.mybatis.mapper.BaseMapperX;
import com.fastbee.iot.domain.Device;
import com.fastbee.iot.domain.Scene;
import com.fastbee.iot.model.SceneTerminalUserVO;
import org.springframework.stereotype.Repository;

/**
 * 场景联动Mapper接口
 *
 * @author kerwincui
 * @date 2022-01-13
 */
@Repository
public interface SceneMapper extends BaseMapperX<Scene>
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
     * 删除场景联动
     *
     * @param sceneId 场景联动主键
     * @return 结果
     */
    public int deleteSceneBySceneId(Long sceneId);

    /**
     * 批量删除场景联动
     *
     * @param sceneIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSceneBySceneIds(Long[] sceneIds);

    /**
     * 根据ids批量查询场景联动
     * @param sceneIds 场景联动id数组
     * @return 集合
     */
    List<Scene> selectSceneListBySceneIds(Long[] sceneIds);

    /**
     * 查询场景用户信息
     * @param sceneIdSet 场景id
     * @return java.util.List<com.fastbee.iot.model.SceneTerminalUserVO>
     */
    List<SceneTerminalUserVO> selectTerminalUserBySceneIds(Set<Long> sceneIdSet);
}
