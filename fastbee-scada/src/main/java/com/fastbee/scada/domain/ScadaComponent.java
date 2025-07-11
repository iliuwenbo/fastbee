package com.fastbee.scada.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fastbee.common.annotation.Excel;
import com.fastbee.common.core.domain.BaseEntity;

/**
 * 组件管理对象 scada_component
 * 
 * @author kerwincui
 * @date 2023-11-10
 */
public class ScadaComponent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 组件名称 */
    @Excel(name = "组件名称")
    private String componentName;

    /** 组件模版 */
    @Excel(name = "组件模版")
    private String componentTemplate;

    /** 组件风格 */
    @Excel(name = "组件风格")
    private String componentStyle;

    /** 组件脚本 */
    @Excel(name = "组件脚本")
    private String componentScript;

    /** 组件缩略图 */
    @Excel(name = "组件缩略图")
    private String componentImage;

    /** 租户id */
    @Excel(name = "租户id")
    private Long tenantId;

    /** 租户名称 */
    @Excel(name = "租户名称")
    private String tenantName;

    /** 逻辑删除标识 */
    private Integer delFlag;

    /** 转图片参数 */
    private String base64;

    /** 拥有类型 0-私有；1-公有 */
    private Integer isShare;

    /** 用户id */
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getIsShare() {
        return isShare;
    }

    public void setIsShare(Integer isShare) {
        this.isShare = isShare;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setComponentName(String componentName) 
    {
        this.componentName = componentName;
    }

    public String getComponentName() 
    {
        return componentName;
    }
    public void setComponentTemplate(String componentTemplate) 
    {
        this.componentTemplate = componentTemplate;
    }

    public String getComponentTemplate() 
    {
        return componentTemplate;
    }
    public void setComponentStyle(String componentStyle) 
    {
        this.componentStyle = componentStyle;
    }

    public String getComponentStyle() 
    {
        return componentStyle;
    }
    public void setComponentScript(String componentScript) 
    {
        this.componentScript = componentScript;
    }

    public String getComponentScript() 
    {
        return componentScript;
    }
    public void setComponentImage(String componentImage) 
    {
        this.componentImage = componentImage;
    }

    public String getComponentImage() 
    {
        return componentImage;
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
    public void setDelFlag(Integer delFlag) 
    {
        this.delFlag = delFlag;
    }

    public Integer getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("componentName", getComponentName())
            .append("componentTemplate", getComponentTemplate())
            .append("componentStyle", getComponentStyle())
            .append("componentScript", getComponentScript())
            .append("componentImage", getComponentImage())
            .append("tenantId", getTenantId())
            .append("tenantName", getTenantName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
