package com.fastbee.iot.domain.bo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fastbee.common.annotation.Excel;
import com.fastbee.common.core.domain.BaseEntity;
import com.fastbee.iot.domain.ModbusConfig;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 物模型对象 iot_things_model
 *
 * @author kerwincui
 * @date 2023-01-14
 */
@Data
public class ThingsModelBo {

    /**
     * 物模型ID
     */
    @ApiModelProperty("物模型ID")
    private Long modelId;

    /**
     * 物模型名称
     */
    @ApiModelProperty("物模型名称")
    private String modelName;

    /**
     * 产品ID
     */
    @ApiModelProperty("产品ID")
    private Long productId;

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String productName;

    /**
     * 标识符，产品下唯一
     */
    @ApiModelProperty("标识符，产品下唯一")
    private String identifier;

    /**
     * 模型类别（1-属性，2-功能，3-事件）
     */
    @ApiModelProperty(value = "模型类别", notes = "（1-属性，2-功能，3-事件）")
    private Integer type;

    /**
     * 数据类型（integer、decimal、string、bool、array、enum）
     */
    @ApiModelProperty(value = "数据类型", notes = "（integer、decimal、string、bool、array、enum）")
    private String datatype;

}
