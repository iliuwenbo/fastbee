package com.fastbee.scada.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fastbee.common.annotation.Excel;
import com.fastbee.common.core.domain.BaseEntity;

/**
 * 图库管理对象 scada_gallery
 * 
 * @author kerwincui
 * @date 2023-11-10
 */
public class ScadaGallery extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id唯一标识 */
    private Long id;

    /** 文件名称 */
    @Excel(name = "文件名称")
    private String fileName;

    /** 分类名称 */
    @Excel(name = "分类名称")
    private String categoryName;

    /** 资源请求路径 */
    @Excel(name = "资源请求路径")
    private String resourceUrl;

    /** 租户id */
    @Excel(name = "租户id")
    private Long tenantId;

    /** 租户名称 */
    @Excel(name = "租户名称")
    private String tenantName;

    /** 逻辑删除标识 */
    private Integer delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }
    public void setCategoryName(String categoryName) 
    {
        this.categoryName = categoryName;
    }

    public String getCategoryName() 
    {
        return categoryName;
    }
    public void setResourceUrl(String resourceUrl) 
    {
        this.resourceUrl = resourceUrl;
    }

    public String getResourceUrl() 
    {
        return resourceUrl;
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
            .append("fileName", getFileName())
            .append("categoryName", getCategoryName())
            .append("resourceUrl", getResourceUrl())
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
