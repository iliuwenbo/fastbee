package com.fastbee.scada.vo;

import lombok.Data;

/**
 * 设备关联物模型VO
 * @author fastb
 * @date 2023-11-14 10:46
 */
@Data
public class DeviceRealDataVO {

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备编号
     */
    private String serialNumber;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 物模型标识
     */
    private String identifier;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 设备在线状态
     */
    private Integer status;

    /**
     * 物模型单位
     */
    private String unit;

    /**
     * 物模型类别
     */
    private Integer type;
}
