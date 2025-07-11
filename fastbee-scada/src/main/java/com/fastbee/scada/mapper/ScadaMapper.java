package com.fastbee.scada.mapper;

import java.util.Collection;
import java.util.List;

import com.fastbee.iot.domain.EventLog;
import com.fastbee.iot.domain.FunctionLog;
import com.fastbee.iot.model.DeviceStatistic;
import com.fastbee.scada.domain.Scada;
import com.fastbee.scada.vo.ScadaHistoryModelVO;
import com.fastbee.scada.vo.ScadaStatisticVO;
import org.apache.ibatis.annotations.Param;

/**
 * 组态中心Mapper接口
 *
 * @author kerwincui
 * @date 2023-11-10
 */
public interface ScadaMapper
{
    /**
     * 查询组态中心
     *
     * @param id 组态中心主键
     * @return 组态中心
     */
    public Scada selectScadaById(Long id);

    /**
     * 查询组态中心列表
     *
     * @param scada 组态中心
     * @return 组态中心集合
     */
    public List<Scada> selectScadaList(Scada scada);

    /**
     * 新增组态中心
     *
     * @param scada 组态中心
     * @return 结果
     */
    public int insertScada(Scada scada);

    /**
     * 修改组态中心
     *
     * @param scada 组态中心
     * @return 结果
     */
    public int updateScada(Scada scada);

    /**
     * 删除组态中心
     *
     * @param id 组态中心主键
     * @return 结果
     */
    public int deleteScadaById(Long id);

    /**
     * 批量删除组态中心
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteScadaByIds(Long[] ids);

    /**
     * 根据guid获取组态详情
     * @param guid 组态id
     * @return
     */
    Scada selectScadaByGuid(String guid);

    /**
     * 查询设备运行状态
     * @param serialNumber 设备编号
     * @return java.lang.String
     */
    Integer getStatusBySerialNumber(String serialNumber);

    ScadaStatisticVO selectDeviceProductAlertCount(@Param("tenantId") Long tenantId, @Param("userId") Long userId);

    /**
     * 查询功能物模型历史数据
     * @param functionLog 功能物模型日志
     * @return java.util.List<com.fastbee.scada.vo.ScadaHistoryModelVO>
     */
    List<ScadaHistoryModelVO> listFunctionLogHistory(FunctionLog functionLog);

    /**
     * 查询时间物模型历史数据
     * @param eventLog 事件物模型日志
     * @return java.util.List<com.fastbee.scada.vo.ScadaHistoryModelVO>
     */
    List<ScadaHistoryModelVO> listEventLogHistory(EventLog eventLog);
}
