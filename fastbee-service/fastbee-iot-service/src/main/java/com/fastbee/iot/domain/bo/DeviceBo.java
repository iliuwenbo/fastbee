package com.fastbee.iot.domain.bo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fastbee.common.annotation.Excel;
import com.fastbee.common.core.domain.BaseEntity;
import com.fastbee.iot.domain.SipRelation;
import com.fastbee.iot.domain.ThingsModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 设备对象 iot_device
 *
 * @author kerwincui
 * @since 2021-12-16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceBo extends BaseEntity{

    @Getter
    private static final long serialVersionUID = 1L;

    /**
     * 设备ID
     */
    @ApiModelProperty("设备ID")
    private Long deviceId;

    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    @Excel(name = "设备名称")
    private String deviceName;

    /**
     * 产品ID
     */
    @ApiModelProperty("产品ID")
    @Excel(name = "产品ID")
    private Long productId;

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    @Excel(name = "产品名称")
    private String productName;

    /**
     * 租户ID
     */
    @ApiModelProperty("租户ID")
    @Excel(name = "租户ID")
    private Long tenantId;

    /**
     * 租户名称
     */
    @ApiModelProperty("租户名称")
    @Excel(name = "租户名称")
    private String tenantName;

    /**
     * 设备编号
     */
    @ApiModelProperty("设备编号")
    @Excel(name = "设备编号")
    private String serialNumber;

    /**
     * 固件版本
     */
    @ApiModelProperty("固件版本")
    @Excel(name = "固件版本")
    private BigDecimal firmwareVersion;

    /**
     * WIFI固件版本
     */
    @ApiModelProperty("HTTP推送固件版本")
    @Excel(name = "HTTP推送固件版本")
    private BigDecimal wirelessVersion;

    /**
     * 设备状态（1-未激活，2-禁用，3-在线，4-离线）
     */
    @ApiModelProperty("设备状态（1-未激活，2-禁用，3-在线，4-离线）")
    @Excel(name = "设备状态")
    private Integer status;

    /**
     * 物模型值
     */
    @ApiModelProperty("物模型值")
    @Excel(name = "物模型")
    private String thingsModelValue;



}
