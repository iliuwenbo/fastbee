package com.fastbee.scada.vo;

import lombok.Data;

/**
 * @author fastb
 * @date 2023-11-13 16:44
 */
@Data
public class ScadaBindDeviceSimVO {


    /** 设备编号 */
    private String serialNumber;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 产品id
     */
    private Long productId;
}
