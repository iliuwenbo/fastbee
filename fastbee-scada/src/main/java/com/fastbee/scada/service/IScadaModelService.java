package com.fastbee.scada.service;

import java.util.List;
import com.fastbee.scada.domain.ScadaModel;

/**
 * 模型管理Service接口
 * 
 * @author kerwincui
 * @date 2023-11-10
 */
public interface IScadaModelService 
{
    /**
     * 查询模型管理
     * 
     * @param id 模型管理主键
     * @return 模型管理
     */
    public ScadaModel selectScadaModelById(Long id);

    /**
     * 查询模型管理列表
     * 
     * @param scadaModel 模型管理
     * @return 模型管理集合
     */
    public List<ScadaModel> selectScadaModelList(ScadaModel scadaModel);

    /**
     * 新增模型管理
     * 
     * @param scadaModel 模型管理
     * @return 结果
     */
    public int insertScadaModel(ScadaModel scadaModel);

    /**
     * 修改模型管理
     * 
     * @param scadaModel 模型管理
     * @return 结果
     */
    public int updateScadaModel(ScadaModel scadaModel);

    /**
     * 批量删除模型管理
     * 
     * @param ids 需要删除的模型管理主键集合
     * @return 结果
     */
    public int deleteScadaModelByIds(Long[] ids);

    /**
     * 删除模型管理信息
     * 
     * @param id 模型管理主键
     * @return 结果
     */
    public int deleteScadaModelById(Long id);
}
