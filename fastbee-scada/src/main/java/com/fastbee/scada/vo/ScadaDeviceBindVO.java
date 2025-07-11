package com.fastbee.scada.vo;

import lombok.Data;

/**
 * @author fastb
 * @date 2023-11-13 16:44
 */
@Data
public class ScadaDeviceBindVO {

    /**
     * 主键id
     */
    private Long id;

    /** 设备编号 */
    private String serialNumber;

    /**
     * 设备名称
     */
    private String deviceName;

    /** 组态guid */
    private String scadaGuid;

    /**
     * 产品id
     */
    private Long productId;
}
