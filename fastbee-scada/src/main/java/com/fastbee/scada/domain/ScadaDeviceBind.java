package com.fastbee.scada.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fastbee.common.annotation.Excel;
import com.fastbee.common.core.domain.BaseEntity;

/**
 * 组态设备关联对象 scada_device_bind
 *
 * @author kerwincui
 * @date 2023-11-13
 */
@Data
public class ScadaDeviceBind {

    /** id唯一标识 */
    private Long id;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String serialNumber;

    /** 组态guid */
    @Excel(name = "组态guid")
    private String scadaGuid;

    /**
     * 设备名称
     */
    @Excel(name = "设备名称")
    private String deviceName;

    /**
     * 设备状态
     */
    private Integer status;

}
