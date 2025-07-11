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
 * 第三方平台信息对象 api_third_party_platform
 *
 * @author lwb
 * @date 2025-06-04
 */

@ApiModel(value = "ApiThirdPartyPlatform", description = "第三方平台信息 api_third_party_platform")
@Data
@TableName("api_third_party_platform" )
public class ApiThirdPartyPlatform implements Serializable{
    private static final long serialVersionUID=1L;

    /** 平台ID */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("平台ID")
    private Long id;

    /** 平台唯一编码 */
    @ApiModelProperty("平台唯一编码")
    private String platformCode;

    /** 平台名称 */
    @ApiModelProperty("平台名称")
    private String platformName;

    /** 平台类型（电商/社交/支付/云计算等） */
    @ApiModelProperty("平台类型")
    private String platformType;

    /** 平台Logo链接 */
    @ApiModelProperty("平台Logo链接")
    private String logoUrl;

    /** 平台官网 */
    @ApiModelProperty("平台官网")
    private String officialWebsite;

    /** API接口地址 */
    @ApiModelProperty("API接口地址")
    private String apiEndpoint;

    /** 应用标识 */
    @ApiModelProperty("应用标识")
    private String appKey;

    /** 应用密钥 */
    @ApiModelProperty("应用密钥")
    private String appSecret;

    /** 状态（0:禁用 1:启用 2:维护中） */
    @ApiModelProperty("状态")
    private Integer status;


    /** 加密方式 */
    @ApiModelProperty("加密方式")
    private String encryptionType;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /** 备注信息 */
    @ApiModelProperty("备注信息")
    private String remark;

    /** 删除标识（0 - 未删除，1 - 已删除） */
    @ApiModelProperty("删除标识")
    private Long delFlag;

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
