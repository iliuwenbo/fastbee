package com.fastbee.iot.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fastbee.common.annotation.Excel;
import com.fastbee.common.core.domain.BaseEntity;

/**
 * 通用物模型对象 iot_things_model_template
 *
 * @author kerwincui
 * @date 2023-01-15
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ThingsModelTemplate", description = "通用物模型对象 iot_things_model_template")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ThingsModelTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 物模型ID */
    @ApiModelProperty("物模型ID")
    private Long templateId;

    /** 物模型名称 */
    @ApiModelProperty("物模型名称")
    @Excel(name = "物模型名称" ,prompt = "必填")
    private String templateName;

    /** 租户ID */
    @ApiModelProperty("租户ID")
    private Long tenantId;

    /** 租户名称 */
    @ApiModelProperty("租户名称")
    private String tenantName;

    /** 标识符，产品下唯一 */
    @ApiModelProperty("标识符，产品下唯一")
    @Excel(name = "标识符",prompt = "modbus不填,默认为寄存器地址")
    private String identifier;

    /** 模型类别（1-属性，2-功能，3-事件） */
    @ApiModelProperty(value = "模型类别", notes = "（1-属性，2-功能，3-事件）")
    @Excel(name = "模型类别", readConverterExp = "1=属性,2=功能,3=事件",prompt ="1=属性，2-功能，3-事件")
    private Integer type;

    /** 计算公式 */
    @ApiModelProperty("计算公式")
    @Excel(name = "计算公式",prompt = "选填,例如:%s*10,%s是占位符")
    private String formula;

    /** 数据定义 */
    @ApiModelProperty("数据定义")
    private String specs;

    /** 是否系统通用（0-否，1-是） */
    @ApiModelProperty("是否系统通用（0-否，1-是）")
    private Integer isSys;


    /** 是否图表显示（0-否，1-是） */
    @ApiModelProperty("是否图表显示（0-否，1-是）")
    //@Excel(name = "是否图表显示", readConverterExp = "0=否,1=是")
    private Integer isChart;

    /** 是否历史存储（0-否，1-是） */
    @ApiModelProperty("是否历史存储（0-否，1-是）")
    @Excel(name = "是否历史存储", readConverterExp = "0=否,1=是")
    private Integer isHistory;

    /** 是否实时监测（0-否，1-是） */
    @ApiModelProperty("是否实时监测（0-否，1-是）")
    private Integer isMonitor;

    /** 是否分享设备权限（0-否，1-是） */
    @ApiModelProperty(value = "是否分享设备权限", notes = "（0-否，1-是） ")
    private Integer isSharePerm;


    /** 是否分享设备权限（0-否，1-是） */
    @ApiModelProperty(value = "是否分享设备权限", notes = "（0-否，1-是） ")
    @Excel(name = "是否分享设备权限", readConverterExp = "0=否,1=是")
    private Integer isApp;

    /** 删除标志（0代表存在 2代表删除） */
    @ApiModelProperty("删除标志（0代表存在 2代表删除）")
    private String delFlag;

    @Excel(name = "单位")
    private String unit;

    /** 数据类型（integer、decimal、string、bool、array、enum） */
    @ApiModelProperty(value = "数据类型", notes = "（integer、decimal、string、bool、array、enum）")
    @Excel(name = "数据类型", prompt = "integer、decimal、string、bool、array、enum")
    private String datatype;

    @ApiModelProperty("有效值范围")
    @Excel(name = "有效值范围")
    private String limitValue;

    /** 是否只读数据(0-否，1-是) */
    @ApiModelProperty("是否只读数据(0-否，1-是)")
    @Excel(name = "是否只读", readConverterExp = "0=否,1=是",prompt = "0=否,1=是")
    private Integer isReadonly;

    private Integer modelOrder;

    private String language;

    /**
     * 同一租户或用户为true
     */
    private Boolean owner;

}
