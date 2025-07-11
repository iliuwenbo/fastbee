package com.fastbee.iot.tdengine.service.impl;

import com.fastbee.common.utils.DateUtils;
import com.fastbee.iot.domain.Device;
import com.fastbee.iot.domain.DeviceLog;
import com.fastbee.iot.model.DeviceStatistic;
import com.fastbee.iot.model.HistoryModel;
import com.fastbee.iot.model.ThingsModelLogCountVO;
import com.fastbee.iot.model.param.DataCenterParam;
import com.fastbee.iot.tdengine.service.ILogService;
import com.fastbee.iot.model.MonitorModel;
import com.fastbee.iot.tdengine.config.TDengineConfig;
import com.fastbee.iot.tdengine.dao.TDDeviceLogDAO;
import com.fastbee.iot.tdengine.service.model.TdLogDto;
import com.fastbee.iot.util.SnowflakeIdWorker;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类名: TdengineLogServiceImpl
 * 描述: TDengine存储日志数据实现类
 * 时间: 2022/5/22,0022 13:38
 * 开发人: admin
 */
public class TdengineLogServiceImpl implements ILogService {

    private ApplicationContext applicationContext;

    private TDDeviceLogDAO tdDeviceLogDAO;

    private TDengineConfig tDengineConfig;

    private SnowflakeIdWorker snowflakeIdWorker;

    private String dbName;

    public TdengineLogServiceImpl(TDengineConfig _tDengineConfig, TDDeviceLogDAO _tdDeviceLogDAO) {
        this.tdDeviceLogDAO = _tdDeviceLogDAO;
        this.tDengineConfig = _tDengineConfig;
        snowflakeIdWorker = new SnowflakeIdWorker(1);
        this.dbName = _tDengineConfig.getDbName();
    }

    /***
     * 新增设备日志
     * @return
     */
    @Override
    public int saveDeviceLog(DeviceLog deviceLog) {
        long logId = snowflakeIdWorker.nextId();
        deviceLog.setLogId(logId);
        return tdDeviceLogDAO.save(dbName, deviceLog);
    }

    /**
     * 批量保存日志
     */
    @Override
    public int saveBatch(TdLogDto dto) {
        return tdDeviceLogDAO.saveBatch(dbName, dto);
    }

    /***
     * 设备属性、功能、事件和监测数据总数
     * @return
     */
    @Override
    public DeviceStatistic selectCategoryLogCount(Device device) {
        DeviceStatistic statistic = new DeviceStatistic();
        Long property = tdDeviceLogDAO.selectPropertyLogCount(dbName, device);
        Long function = tdDeviceLogDAO.selectFunctionLogCount(dbName, device);
        Long event = tdDeviceLogDAO.selectEventLogCount(dbName, device);
        Long monitor = tdDeviceLogDAO.selectMonitorLogCount(dbName, device);
        statistic.setPropertyCount(property == null ? 0 : property);
        statistic.setFunctionCount(function == null ? 0 : function);
        statistic.setEventCount(event == null ? 0 : event);
        statistic.setMonitorCount(monitor == null ? 0 : monitor);
        return statistic;
    }

    /***
     * 日志列表
     * @return
     */
    @Override
    public List<DeviceLog> selectDeviceLogList(DeviceLog deviceLog) {
        return tdDeviceLogDAO.selectDeviceLogList(dbName, deviceLog);
    }

    /***
     * 监测数据列表
     * @return
     */
    @Override
    public List<MonitorModel> selectMonitorList(DeviceLog deviceLog) {
        if (deviceLog.getIdentity() != null) {
            deviceLog.setIdentity("%" + deviceLog.getIdentity() + "%");
        }
        return tdDeviceLogDAO.selectMonitorList(dbName, deviceLog);
    }

    /***
     * 根据设备ID删除设备日志
     * @return
     */
    @Override
    public int deleteDeviceLogByDeviceNumber(String deviceNumber) {
        return tdDeviceLogDAO.deleteDeviceLogByDeviceNumber(dbName, deviceNumber);
    }

    /**
     * 查询历史数据
     * is_Montor=1 或 is_history=1
     */
    @Override
    public Map<String, List<HistoryModel>> selectHistoryList(DeviceLog deviceLog) {
        List<HistoryModel> historyList = tdDeviceLogDAO.selectHistoryList(dbName, deviceLog);
        return historyList.stream().collect(Collectors.groupingBy(HistoryModel::getIdentity));
    }

    @Override
    public List<HistoryModel> listHistory(DeviceLog deviceLog) {
        // 8时区问题
//        if (StringUtils.isNotEmpty(deviceLog.getBeginTime())) {
//            LocalDateTime localDateTime = DateUtils.toLocalDateTime(deviceLog.getBeginTime(), DateUtils.YYYY_MM_DD_HH_MM_SS);
//            deviceLog.setBeginTime(DateUtils.localDateTimeToStr(localDateTime.minusHours(8), DateUtils.YYYY_MM_DD_HH_MM_SS));
//        }
//        if (StringUtils.isNotEmpty(deviceLog.getEndTime())) {
//            LocalDateTime localDateTime = DateUtils.toLocalDateTime(deviceLog.getEndTime(), DateUtils.YYYY_MM_DD_HH_MM_SS);
//            deviceLog.setEndTime(DateUtils.localDateTimeToStr(localDateTime.minusHours(8), DateUtils.YYYY_MM_DD_HH_MM_SS));
//        }
        List<HistoryModel> historyModelList = tdDeviceLogDAO.listHistory(dbName, deviceLog);
        for (HistoryModel historyModel : historyModelList) {
            historyModel.setTime(DateUtils.dateRemoveMs(historyModel.getTime()));
        }
        return historyModelList;
    }

    @Override
    public List<HistoryModel> listhistoryGroupByCreateTime(DeviceLog deviceLog) {
        return tdDeviceLogDAO.listhistoryGroupByCreateTime(dbName, deviceLog);
    }

    @Override
    public List<String> selectStatsValue(DeviceLog deviceLog) {
        return tdDeviceLogDAO.selectStatsValue(dbName, deviceLog);
    }

    @Override
    public List<ThingsModelLogCountVO> countThingsModelInvoke(DataCenterParam dataCenterParam) {
        return tdDeviceLogDAO.countThingsModelInvoke(dbName, dataCenterParam);
    }

    public DeviceLog selectLastReport(DeviceLog deviceLog) {
        return null;
    }
}
