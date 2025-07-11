package com.fastbee.platform.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * API 参数详情对象 api_param_detail
 *
 * @author lwb
 * @date 2025-04-27
 */

@ApiModel(value = "ApiParamDetail", description = "API 参数详情 api_param_detail")
@Data
@TableName("api_param_detail" )
public class ApiParamDetail implements Serializable{
    private static final long serialVersionUID=1L;

    /** 参数详情 ID */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("参数详情 ID")
    private Long id;

    /** 接口 ID，关联 api_definition 表 */
    @ApiModelProperty("接口 ID，关联 api_definition 表")
    private Long apiId;

    /** 参数类型（路径参数（path）、查询参数（query）、请求头参数（header）、请求体参数（body）、返回参数（return）） */
    @ApiModelProperty("参数类型（路径参数（path）、查询参数（query）、请求头参数（header）、请求体参数（body）、返回参数（return））")
    private String paramType;

    /** 参数名称 */
    @ApiModelProperty("参数名称")
    private String paramName;

    /** 参数值 */
    @ApiModelProperty("参数值")
    private String paramValue;

    /** 是否必填 */
    @ApiModelProperty("是否必填")
    private Long required;

    /** 删除标识（0 - 未删除，1 - 已删除） */
    @ApiModelProperty("删除标识")
    private Long delFlag;

    /** 创建者 */
    @ApiModelProperty("创建者")
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /** 更新者 */
    @ApiModelProperty("更新者")
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /** 备注 */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 是否使用外部参数传入
     */
    @ApiModelProperty("是否使用外部参数传入")
    private String autoAppend;

    /**
     * 物模型与http请求映射关系字段对应
     */
    private String manufacturerId;

    @Setter
    @TableField(exist = false)
    @ApiModelProperty("请求参数")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> params;

    public Map<String, Object> getParams(){
        if (params == null){
            params = new HashMap<>();
        }
        return params;
    }

}
