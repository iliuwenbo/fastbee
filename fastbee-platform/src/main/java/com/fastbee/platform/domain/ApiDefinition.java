package com.fastbee.platform.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.injector.methods.Insert;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 接口定义对象 api_definition
 *
 * @author lwb
 * @date 2025-04-27
 */

@ApiModel(value = "ApiDefinition", description = "接口定义 api_definition")
@Data
@TableName("api_definition" )
public class ApiDefinition implements Serializable{
    private static final long serialVersionUID=1L;

    /** 接口 ID */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("接口 ID")
    private Long id;


    @ApiModelProperty("平台id")
    private String apiThirdPartyPlatformId;

    /** 接口名称 */
    @ApiModelProperty("接口名称")
    private String apiName;

    /** 接口 URL */
    @ApiModelProperty("接口 URL")
    @NotEmpty(message = "url不能为空", groups = Insert.class)
    private String apiUrl;

    /** 接口类型 0=功能；1=设备列表*/
    @ApiModelProperty("接口类型 0=功能；1=设备列表")
    private String apiType = "1";

    /** 请求方法（POST/GET/PUT...） */
    @ApiModelProperty("请求方法（POST/GET/PUT...）")
    @NotEmpty(message = "请求方法不能为空", groups = Insert.class)
    private String method;

    /** 请求体格式（xml 或 json） */
    @ApiModelProperty("请求体格式（XML 或 JSON）")
    private String requestFormat = "JSON";

    /** 脚本id */
    @ApiModelProperty("脚本id")
    private String apiScriptId;

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

    @ApiModelProperty("api参数详情对象")
    @TableField(exist = false)
    @NotNull(message = "参数详情对象不能为空", groups = Insert.class)
    private List<ApiParamDetail> apiParamDetailList;

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
