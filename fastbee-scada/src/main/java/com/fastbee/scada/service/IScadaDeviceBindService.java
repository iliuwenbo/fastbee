package com.fastbee.scada.service;

import java.util.List;
import com.fastbee.scada.domain.ScadaDeviceBind;

/**
 * 组态设备关联Service接口
 *
 * @author kerwincui
 * @date 2023-11-13
 */
public interface IScadaDeviceBindService
{
    /**
     * 查询组态设备关联
     *
     * @param id 组态设备关联主键
     * @return 组态设备关联
     */
    public ScadaDeviceBind selectScadaDeviceBindById(Long id);

    /**
     * 查询组态设备关联列表
     *
     * @param scadaDeviceBind 组态设备关联
     * @return 组态设备关联集合
     */
    public List<ScadaDeviceBind> selectScadaDeviceBindList(ScadaDeviceBind scadaDeviceBind);

    /**
     * 新增组态设备关联
     *
     * @param scadaDeviceBind 组态设备关联
     * @return 结果
     */
    public int insertScadaDeviceBind(ScadaDeviceBind scadaDeviceBind);

    /**
     * 修改组态设备关联
     *
     * @param scadaDeviceBind 组态设备关联
     * @return 结果
     */
    public int updateScadaDeviceBind(ScadaDeviceBind scadaDeviceBind);

    /**
     * 批量删除组态设备关联
     *
     * @param ids 需要删除的组态设备关联主键集合
     * @return 结果
     */
    public int deleteScadaDeviceBindByIds(Long[] ids);

    /**
     * 删除组态设备关联信息
     *
     * @param id 组态设备关联主键
     * @return 结果
     */
    public int deleteScadaDeviceBindById(Long id);

    /**
     * 查询组态关联设备
     * @param scadaGuid 组态guid
     * @param serialNumberList 设备编号集合
     * @return 绑定设备
     */
    List<ScadaDeviceBind> listByGuidAndSerialNumber(String scadaGuid, List<String> serialNumberList);

}
