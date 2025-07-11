package com.fastbee.scada.service.impl;

import com.fastbee.common.utils.DateUtils;
import com.fastbee.scada.domain.ScadaModel;
import com.fastbee.scada.mapper.ScadaModelMapper;
import com.fastbee.scada.service.IScadaModelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 模型管理Service业务层处理
 *
 * @author kerwincui
 * @date 2023-11-10
 */
@Service
public class ScadaModelServiceImpl implements IScadaModelService
{
    @Resource
    private ScadaModelMapper scadaModelMapper;

    /**
     * 查询模型管理
     *
     * @param id 模型管理主键
     * @return 模型管理
     */
    @Override
    public ScadaModel selectScadaModelById(Long id)
    {
        return scadaModelMapper.selectScadaModelById(id);
    }

    /**
     * 查询模型管理列表
     *
     * @param scadaModel 模型管理
     * @return 模型管理
     */
    @Override
    public List<ScadaModel> selectScadaModelList(ScadaModel scadaModel)
    {
        return scadaModelMapper.selectScadaModelList(scadaModel);
    }

    /**
     * 新增模型管理
     *
     * @param scadaModel 模型管理
     * @return 结果
     */
    @Override
    public int insertScadaModel(ScadaModel scadaModel)
    {
        scadaModel.setCreateTime(DateUtils.getNowDate());
        return scadaModelMapper.insertScadaModel(scadaModel);
    }

    /**
     * 修改模型管理
     *
     * @param scadaModel 模型管理
     * @return 结果
     */
    @Override
    public int updateScadaModel(ScadaModel scadaModel)
    {
        scadaModel.setUpdateTime(DateUtils.getNowDate());
        return scadaModelMapper.updateScadaModel(scadaModel);
    }

    /**
     * 批量删除模型管理
     *
     * @param ids 需要删除的模型管理主键
     * @return 结果
     */
    @Override
    public int deleteScadaModelByIds(Long[] ids)
    {
        return scadaModelMapper.deleteScadaModelByIds(ids);
    }

    /**
     * 删除模型管理信息
     *
     * @param id 模型管理主键
     * @return 结果
     */
    @Override
    public int deleteScadaModelById(Long id)
    {
        return scadaModelMapper.deleteScadaModelById(id);
    }
}
