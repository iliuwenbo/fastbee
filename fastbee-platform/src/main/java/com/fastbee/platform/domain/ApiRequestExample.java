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
 * API 请求示例对象 api_request_example
 *
 * @author lwb
 * @date 2025-04-27
 */

@ApiModel(value = "ApiRequestExample", description = "API 请求示例 api_request_example")
@Data
@TableName("api_request_example" )
public class ApiRequestExample implements Serializable{
    private static final long serialVersionUID=1L;

    /** 请求示例 ID */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("请求示例 ID")
    private Long id;

    /** 接口 ID，关联 api_definition 表 */
    @ApiModelProperty("接口 ID，关联 api_definition 表")
    private Long apiId;

    /** 入参示例 */
    @ApiModelProperty("入参示例")
    private String requestExample;

    /** 请求头示例 */
    @ApiModelProperty("请求头示例")
    private String requestHeadersExample;

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
