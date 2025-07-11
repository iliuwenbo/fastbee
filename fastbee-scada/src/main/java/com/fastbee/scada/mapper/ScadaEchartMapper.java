package com.fastbee.scada.mapper;

import java.util.List;
import com.fastbee.scada.domain.ScadaEchart;

/**
 * 图表管理Mapper接口
 * 
 * @author kerwincui
 * @date 2023-11-10
 */
public interface ScadaEchartMapper 
{
    /**
     * 查询图表管理
     * 
     * @param id 图表管理主键
     * @return 图表管理
     */
    public ScadaEchart selectScadaEchartById(Long id);

    /**
     * 查询图表管理列表
     * 
     * @param scadaEchart 图表管理
     * @return 图表管理集合
     */
    public List<ScadaEchart> selectScadaEchartList(ScadaEchart scadaEchart);

    /**
     * 新增图表管理
     * 
     * @param scadaEchart 图表管理
     * @return 结果
     */
    public int insertScadaEchart(ScadaEchart scadaEchart);

    /**
     * 修改图表管理
     * 
     * @param scadaEchart 图表管理
     * @return 结果
     */
    public int updateScadaEchart(ScadaEchart scadaEchart);

    /**
     * 删除图表管理
     * 
     * @param id 图表管理主键
     * @return 结果
     */
    public int deleteScadaEchartById(Long id);

    /**
     * 批量删除图表管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteScadaEchartByIds(Long[] ids);
}
