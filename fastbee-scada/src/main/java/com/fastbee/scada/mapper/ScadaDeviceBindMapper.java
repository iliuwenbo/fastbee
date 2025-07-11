package com.fastbee.scada.mapper;

import java.util.List;
import com.fastbee.scada.domain.ScadaDeviceBind;
import com.fastbee.scada.vo.ScadaBindDeviceSimVO;
import org.apache.ibatis.annotations.Param;

/**
 * 组态设备关联Mapper接口
 *
 * @author kerwincui
 * @date 2023-11-13
 */
public interface ScadaDeviceBindMapper
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
     * 删除组态设备关联
     *
     * @param id 组态设备关联主键
     * @return 结果
     */
    public int deleteScadaDeviceBindById(Long id);

    /**
     * 批量删除组态设备关联
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteScadaDeviceBindByIds(Long[] ids);

    List<ScadaDeviceBind> listByGuidAndSerialNumber(@Param("scadaGuid") String scadaGuid, @Param("serialNumberList") List<String> serialNumberList);

    /**
     * 查询组态绑定设备
     * @param guid 组态guid
     * @return java.util.List<com.fastbee.scada.vo.ScadaBindDeviceSimVO>
     */
    List<ScadaBindDeviceSimVO> listDeviceSimByGuid(String guid);
}
