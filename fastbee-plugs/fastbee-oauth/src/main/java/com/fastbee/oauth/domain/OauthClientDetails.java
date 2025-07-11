package com.fastbee.oauth.domain;

import com.fastbee.common.annotation.Excel;
import com.fastbee.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 云云对接对象 oauth_client_details
 *
 * @author kerwincui
 * @date 2022-02-07
 */
@ApiModel(value = "OauthClientDetails", description = "云云对接对象 oauth_client_details")
public class OauthClientDetails extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**
     * 主键编号
     */
    @ApiModelProperty("编号")
    @Excel(name = "编号")
    private Long id;

    /** 客户端ID */
    @ApiModelProperty("客户端ID")
    @Excel(name = "客户端ID")
    private String clientId;

    /** 资源 */
    @ApiModelProperty("资源")
    @Excel(name = "资源")
    private String resourceIds;

    /** 客户端秘钥 */
    @ApiModelProperty("客户端秘钥")
    private String clientSecret;

    /** 权限范围 */
    @ApiModelProperty("权限范围")
    @Excel(name = "权限范围")
    private String scope;

    /** 授权模式 */
    @ApiModelProperty("授权模式")
    @Excel(name = "授权模式")
    private String authorizedGrantTypes;

    /** 回调地址 */
    @ApiModelProperty("回调地址")
    @Excel(name = "回调地址")
    private String webServerRedirectUri;

    /** 权限 */
    @ApiModelProperty("权限")
    @Excel(name = "权限")
    private String authorities;

    /** access token有效时间 */
    @ApiModelProperty("access token有效时间")
    @Excel(name = "access token有效时间")
    private Long accessTokenValidity;

    /** refresh token有效时间 */
    @ApiModelProperty("refresh token有效时间")
    @Excel(name = "refresh token有效时间")
    private Long refreshTokenValidity;

    /** 预留的字段 */
    @ApiModelProperty("预留的字段")
    @Excel(name = "预留的字段")
    private String additionalInformation;

    /** 自动授权 */
    @ApiModelProperty("自动授权")
    @Excel(name = "自动授权")
    private String autoapprove;

    /** 平台 */
    @ApiModelProperty("平台")
    @Excel(name = "平台")
    private Integer type;

    /**
     * 启用状态
     */
    @ApiModelProperty("启用状态")
    @Excel(name = "启用状态")
    private Integer status;

    /**
     * 图标
     */
    @ApiModelProperty("图标")
    private String icon;

    /**
     * 云技能id
     */
    private String cloudSkillId;

    /** 租户id */
    private Long tenantId;

    /** 租户名称 */
    private String tenantName;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCloudSkillId() {
        return cloudSkillId;
    }

    public void setCloudSkillId(String cloudSkillId) {
        this.cloudSkillId = cloudSkillId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    public String getClientId()
    {
        return clientId;
    }
    public void setResourceIds(String resourceIds)
    {
        this.resourceIds = resourceIds;
    }

    public String getResourceIds()
    {
        return resourceIds;
    }
    public void setClientSecret(String clientSecret)
    {
        this.clientSecret = clientSecret;
    }

    public String getClientSecret()
    {
        return clientSecret;
    }
    public void setScope(String scope)
    {
        this.scope = scope;
    }

    public String getScope()
    {
        return scope;
    }
    public void setAuthorizedGrantTypes(String authorizedGrantTypes)
    {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    public String getAuthorizedGrantTypes()
    {
        return authorizedGrantTypes;
    }
    public void setWebServerRedirectUri(String webServerRedirectUri)
    {
        this.webServerRedirectUri = webServerRedirectUri;
    }

    public String getWebServerRedirectUri()
    {
        return webServerRedirectUri;
    }
    public void setAuthorities(String authorities)
    {
        this.authorities = authorities;
    }

    public String getAuthorities()
    {
        return authorities;
    }
    public void setAccessTokenValidity(Long accessTokenValidity)
    {
        this.accessTokenValidity = accessTokenValidity;
    }

    public Long getAccessTokenValidity()
    {
        return accessTokenValidity;
    }
    public void setRefreshTokenValidity(Long refreshTokenValidity)
    {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public Long getRefreshTokenValidity()
    {
        return refreshTokenValidity;
    }
    public void setAdditionalInformation(String additionalInformation)
    {
        this.additionalInformation = additionalInformation;
    }

    public String getAdditionalInformation()
    {
        return additionalInformation;
    }
    public void setAutoapprove(String autoapprove)
    {
        this.autoapprove = autoapprove;
    }

    public String getAutoapprove()
    {
        return autoapprove;
    }
    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getType()
    {
        return type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("clientId", getClientId())
            .append("resourceIds", getResourceIds())
            .append("clientSecret", getClientSecret())
            .append("scope", getScope())
            .append("authorizedGrantTypes", getAuthorizedGrantTypes())
            .append("webServerRedirectUri", getWebServerRedirectUri())
            .append("authorities", getAuthorities())
            .append("accessTokenValidity", getAccessTokenValidity())
            .append("refreshTokenValidity", getRefreshTokenValidity())
            .append("additionalInformation", getAdditionalInformation())
            .append("autoapprove", getAutoapprove())
            .append("type", getType())
            .toString();
    }
}
