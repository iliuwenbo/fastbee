package com.fastbee.scada.mapper;

import java.util.List;
import com.fastbee.scada.domain.ScadaComponent;

/**
 * 组件管理Mapper接口
 * 
 * @author kerwincui
 * @date 2023-11-10
 */
public interface ScadaComponentMapper 
{
    /**
     * 查询组件管理
     * 
     * @param id 组件管理主键
     * @return 组件管理
     */
    public ScadaComponent selectScadaComponentById(Long id);

    /**
     * 查询组件管理列表
     * 
     * @param scadaComponent 组件管理
     * @return 组件管理集合
     */
    public List<ScadaComponent> selectScadaComponentList(ScadaComponent scadaComponent);

    /**
     * 新增组件管理
     * 
     * @param scadaComponent 组件管理
     * @return 结果
     */
    public int insertScadaComponent(ScadaComponent scadaComponent);

    /**
     * 修改组件管理
     * 
     * @param scadaComponent 组件管理
     * @return 结果
     */
    public int updateScadaComponent(ScadaComponent scadaComponent);

    /**
     * 删除组件管理
     * 
     * @param id 组件管理主键
     * @return 结果
     */
    public int deleteScadaComponentById(Long id);

    /**
     * 批量删除组件管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteScadaComponentByIds(Long[] ids);
}
