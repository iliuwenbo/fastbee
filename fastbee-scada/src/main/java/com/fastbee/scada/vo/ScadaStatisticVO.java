package com.fastbee.scada.vo;

import lombok.Data;

/**
 * id和name
 *
 * @author kerwincui
 * @date 2021-12-16
 */
@Data
public class ScadaStatisticVO
{
    /** 设备数量 **/
    private Integer deviceCount;

    /** 设备数量 **/
    private Integer deviceOnlineCount;

    /** 产品数量 **/
    private Integer productCount;

    /** 告警 **/
    private Long alertCount;

    /** 属性上报 **/
    private Long propertyCount;

    /** 功能上报 **/
    private Long functionCount;

    /** 事件上报 **/
    private Long eventCount;

    /** 监测数据上报 **/
    private Long monitorCount;

    /** 告警设备数量 **/
    private Long alertDeviceCount;

    /** 设备离线数量 **/
    private Integer deviceOfflineCount;

    /** 告警未处理数量 **/
    private Long alertNotProcessedCount;

    /** 告警已处理数量 **/
    private Long alertProcessedCount;
}
