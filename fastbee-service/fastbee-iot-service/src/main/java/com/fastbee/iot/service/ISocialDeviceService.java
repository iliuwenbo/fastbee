package com.fastbee.iot.service;

import com.fastbee.iot.domain.Device;
import com.fastbee.iot.domain.ThingsModel;
import com.fastbee.iot.domain.bo.DeviceBo;
import com.fastbee.iot.domain.bo.ThingsModelBo;
import com.fastbee.iot.model.*;

import java.util.List;

/**
 * 设备Service接口
 *
 * @author kerwincui
 * @date 2021-12-16
 */
public interface ISocialDeviceService
{

    /**
     * 查询设备简短列表
     *
     * @param device 设备
     * @return 设备集合
     */
    public List<SocialDeviceShortOutput> selectDeviceShortList(Device device);

    /**
     * 查询所有功能
     */
    List<ThingsModel> selectAll(ThingsModelBo thingsModel);

    /**
     * 查询所有设备
     * @return
     */
    List<Device> deviceSelectList(DeviceBo device);

    /**
     * 查询设备和运行状态
     *
     * @param deviceId 设备主键
     * @return 设备
     */
    public DeviceShortOutput selectDeviceRunningStatusByDeviceId(Long deviceId);


}
