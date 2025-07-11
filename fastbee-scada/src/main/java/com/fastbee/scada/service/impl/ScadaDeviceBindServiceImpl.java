package com.fastbee.scada.service.impl;

import com.fastbee.scada.domain.ScadaDeviceBind;
import com.fastbee.scada.mapper.ScadaDeviceBindMapper;
import com.fastbee.scada.service.IScadaDeviceBindService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 组态设备关联Service业务层处理
 *
 * @author kerwincui
 * @date 2023-11-13
 */
@Service
public class ScadaDeviceBindServiceImpl implements IScadaDeviceBindService
{
    @Resource
    private ScadaDeviceBindMapper scadaDeviceBindMapper;

    /**
     * 查询组态设备关联
     *
     * @param id 组态设备关联主键
     * @return 组态设备关联
     */
    @Override
    public ScadaDeviceBind selectScadaDeviceBindById(Long id)
    {
        return scadaDeviceBindMapper.selectScadaDeviceBindById(id);
    }

    /**
     * 查询组态设备关联列表
     *
     * @param scadaDeviceBind 组态设备关联
     * @return 组态设备关联
     */
    @Override
    public List<ScadaDeviceBind> selectScadaDeviceBindList(ScadaDeviceBind scadaDeviceBind)
    {
        return scadaDeviceBindMapper.selectScadaDeviceBindList(scadaDeviceBind);
    }

    /**
     * 新增组态设备关联
     *
     * @param scadaDeviceBind 组态设备关联
     * @return 结果
     */
    @Override
    public int insertScadaDeviceBind(ScadaDeviceBind scadaDeviceBind)
    {
        return scadaDeviceBindMapper.insertScadaDeviceBind(scadaDeviceBind);
    }

    /**
     * 修改组态设备关联
     *
     * @param scadaDeviceBind 组态设备关联
     * @return 结果
     */
    @Override
    public int updateScadaDeviceBind(ScadaDeviceBind scadaDeviceBind)
    {
        return scadaDeviceBindMapper.updateScadaDeviceBind(scadaDeviceBind);
    }

    /**
     * 批量删除组态设备关联
     *
     * @param ids 需要删除的组态设备关联主键
     * @return 结果
     */
    @Override
    public int deleteScadaDeviceBindByIds(Long[] ids)
    {
        return scadaDeviceBindMapper.deleteScadaDeviceBindByIds(ids);
    }

    /**
     * 删除组态设备关联信息
     *
     * @param id 组态设备关联主键
     * @return 结果
     */
    @Override
    public int deleteScadaDeviceBindById(Long id)
    {
        return scadaDeviceBindMapper.deleteScadaDeviceBindById(id);
    }

    @Override
    public List<ScadaDeviceBind> listByGuidAndSerialNumber(String scadaGuid, List<String> serialNumberList) {
        return scadaDeviceBindMapper.listByGuidAndSerialNumber(scadaGuid, serialNumberList);
    }

}
