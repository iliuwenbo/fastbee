package com.fastbee.scada.service;

import java.io.IOException;
import java.util.List;

import com.fastbee.common.exception.file.InvalidExtensionException;
import com.fastbee.scada.domain.ScadaEchart;

/**
 * 图表管理Service接口
 * 
 * @author kerwincui
 * @date 2023-11-10
 */
public interface IScadaEchartService 
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
     * 批量删除图表管理
     * 
     * @param ids 需要删除的图表管理主键集合
     * @return 结果
     */
    public int deleteScadaEchartByIds(Long[] ids);

    /**
     * 删除图表管理信息
     * 
     * @param id 图表管理主键
     * @return 结果
     */
    public int deleteScadaEchartById(Long id);
}
