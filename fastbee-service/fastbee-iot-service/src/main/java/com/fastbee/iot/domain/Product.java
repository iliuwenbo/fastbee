package com.fastbee.iot.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fastbee.common.annotation.Excel;
import com.fastbee.common.core.domain.BaseEntity;
import org.springframework.data.annotation.Transient;

/**
 * 产品对象 iot_product
 *
 * @author kerwincui
 * @date 2021-12-16
 */
@ApiModel(value = "Product", description = "产品对象 iot_product")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 产品ID */
    @ApiModelProperty("产品ID")
    private Long productId;

    /** 产品名称 */
    @ApiModelProperty("产品名称")
    @Excel(name = "产品名称")
    private String productName;

    /** 产品分类ID */
    @ApiModelProperty("产品分类ID")
    @Excel(name = "产品分类ID")
    private Long categoryId;

    /** 产品分类名称 */
    @ApiModelProperty("产品分类名称")
    @Excel(name = "产品分类名称")
    private String categoryName;

    /** 租户ID */
    @ApiModelProperty("租户ID")
    @Excel(name = "租户ID")
    private Long tenantId;

    /** 租户名称 */
    @ApiModelProperty("租户名称")
    @Excel(name = "租户名称")
    private String tenantName;

    /** 是否私有产品（0-否，1-是）私有下级不能共享 */
    @ApiModelProperty(value = "是否私有产品", notes = "（0-否，1-是）")
    @Excel(name = "是否私有产品", readConverterExp = "0-否，1-是")
    private Integer isSys;

    /** 是否启用授权码（0-否，1-是） */
    @ApiModelProperty(value = "是否启用授权码", notes = "（0-否，1-是）")
    @Excel(name = "是否启用授权码", readConverterExp = "0=-否，1-是")
    private Integer isAuthorize;

    /** mqtt账号 */
    @ApiModelProperty("mqtt账号")
    private String mqttAccount;

    /** mqtt密码 */
    @ApiModelProperty("mqtt密码")
    private String mqttPassword;

    /** 产品秘钥 */
    @ApiModelProperty("产品秘钥")
    private String mqttSecret;

    /*产品协议编号*/
    @ApiModelProperty("产品协议编号")
    private String protocolCode;

    /*产品支持的传输协议，多个的选一个即可*/
    @ApiModelProperty("产品支持的传输协议，多个的选一个即可")
    private String transport;

    /** 是否自定义位置 **/
    @ApiModelProperty("定位方式(1=ip自动定位，2=设备定位，3=自定义)")
    private Integer locationWay;

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getProtocolCode() {
        return protocolCode;
    }

    public void setProtocolCode(String protocolCode) {
        this.protocolCode = protocolCode;
    }

    public String getMqttSecret() {
        return mqttSecret;
    }

    public void setMqttSecret(String mqttSecret) {
        this.mqttSecret = mqttSecret;
    }

    /** 状态（1-未发布，2-已发布，不能修改） */
    @ApiModelProperty(value = "状态", notes = "（1-未发布，2-已发布，不能修改）")
    @Excel(name = "状态", readConverterExp = "1==未发布，2=已发布，不能修改")
    private Integer status;

    /** 设备类型（1-直连设备、2-网关子设备、3-网关设备） */
    @ApiModelProperty(value = "设备类型", notes = "（1-直连设备、2-网关子设备、3-网关设备、4-网关子设备）")
    @Excel(name = "设备类型", readConverterExp = "1=直连设备、2=网关设备、3=监控设备、4-网关子设备")
    private Integer deviceType;

    /** 联网方式（1=-wifi、2-蜂窝(2G/3G/4G/5G)、3-以太网、4-其他） */
    @ApiModelProperty(value = "联网方式", notes = "（1=-wifi、2-蜂窝(2G/3G/4G/5G)、3-以太网、4-其他）")
    @Excel(name = "联网方式", readConverterExp = "1=-wifi、2=蜂窝(2G/3G/4G/5G)、3=以太网、4=其他")
    private Integer networkMethod;

    /** 认证方式（1-账号密码、2-证书、3-Http） */
    @ApiModelProperty(value = "认证方式", notes = "（1-账号密码、2-证书、3-Http）")
    @Excel(name = "认证方式", readConverterExp = "1=账号密码、2=证书、3=Http")
    private Integer vertificateMethod;

    /** 图片地址 */
    @ApiModelProperty("图片地址")
    private String imgUrl;

    /** 删除标志（0代表存在 2代表删除） */
    @ApiModelProperty(value = "删除标志", notes = "（0代表存在 2代表删除）")
    private String delFlag;

    /** 物模型Json **/
    @ApiModelProperty("物模型Json")
    private String thingsModelsJson;

    /**采集点模板id*/
    @ApiModelProperty("采集点模板id")
    private Long templateId;

    @Transient
    @ApiModelProperty("是否显示上级")
    private Boolean showSenior;

    @Transient
    @ApiModelProperty("机构ID")
    private Long deptId;

    @Transient
    @ApiModelProperty("是否属于本机构 1-是，0-否")
    private Integer isOwner;

    @Transient
    private Boolean isAdmin;

    @ApiModelProperty("关联组态guId")
    private String guid;

    /**
     * 组态主键id
     */
    @ApiModelProperty("关联组态id")
    private Long scadaId;

    public Long getScadaId() {
        return scadaId;
    }

    public void setScadaId(Long scadaId) {
        this.scadaId = scadaId;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Integer getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(Integer isOwner) {
        this.isOwner = isOwner;
    }

    public Boolean getShowSenior() {
        return showSenior;
    }

    public void setShowSenior(Boolean showSenior) {
        this.showSenior = showSenior;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Integer getLocationWay() {
        return locationWay;
    }

    public void setLocationWay(Integer locationWay) {
        this.locationWay = locationWay;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getThingsModelsJson() {
        return thingsModelsJson;
    }

    public void setThingsModelsJson(String thingsModelsJson) {
        this.thingsModelsJson = thingsModelsJson;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public Long getProductId()
    {
        return productId;
    }
    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getProductName()
    {
        return productName;
    }
    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId()
    {
        return categoryId;
    }
    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public String getCategoryName()
    {
        return categoryName;
    }
    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public Long getTenantId()
    {
        return tenantId;
    }

    public void setTenantName(String tenantName)
    {
        this.tenantName = tenantName;
    }
    public String getTenantName()
    {
        return tenantName;
    }

    public void setIsSys(Integer isSys)
    {
        this.isSys = isSys;
    }
    public Integer getIsSys()
    {
        return isSys;
    }

    public void setIsAuthorize(Integer isAuthorize) {this.isAuthorize = isAuthorize;}
    public Integer getIsAuthorize() {return isAuthorize;}

    public void setMqttAccount(String mqttAccount)
    {
        this.mqttAccount = mqttAccount;
    }
    public String getMqttAccount()
    {
        return mqttAccount;
    }

    public void setMqttPassword(String mqttPassword)
    {
        this.mqttPassword = mqttPassword;
    }
    public String getMqttPassword()
    {
        return mqttPassword;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }
    public void setDeviceType(Integer deviceType)
    {
        this.deviceType = deviceType;
    }

    public Integer getDeviceType()
    {
        return deviceType;
    }
    public void setNetworkMethod(Integer networkMethod)
    {
        this.networkMethod = networkMethod;
    }

    public Integer getNetworkMethod()
    {
        return networkMethod;
    }
    public void setVertificateMethod(Integer vertificateMethod)
    {
        this.vertificateMethod = vertificateMethod;
    }

    public Integer getVertificateMethod()
    {
        return vertificateMethod;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("productId", getProductId())
            .append("productName", getProductName())
            .append("categoryId", getCategoryId())
            .append("categoryName", getCategoryName())
            .append("tenantId", getTenantId())
            .append("tenantName", getTenantName())
            .append("isSys", getIsSys())
            .append("isAuthorize", getIsAuthorize())
            .append("status", getStatus())
            .append("deviceType", getDeviceType())
            .append("networkMethod", getNetworkMethod())
            .append("vertificateMethod", getVertificateMethod())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
