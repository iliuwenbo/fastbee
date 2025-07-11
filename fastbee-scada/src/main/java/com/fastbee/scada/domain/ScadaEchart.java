package com.fastbee.scada.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fastbee.common.annotation.Excel;
import com.fastbee.common.core.domain.BaseEntity;

/**
 * 图表管理对象 scada_echart
 * 
 * @author kerwincui
 * @date 2023-11-10
 */
public class ScadaEchart extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String guid;

    /** 图表名称 */
    @Excel(name = "图表名称")
    private String echartName;

    /** 图表类别 */
    @Excel(name = "图表类别")
    private String echartType;

    /** 图表内容 */
    @Excel(name = "图表内容")
    private String echartData;

    /** 图表图片 */
    @Excel(name = "图表图片")
    private String echartImgae;

    /** 租户id */
    @Excel(name = "租户id")
    private Long tenantId;

    /** 租户名称 */
    @Excel(name = "租户名称")
    private String tenantName;

    /** 逻辑删除标识 */
    private Integer delFlag;

    private String base64;

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
    public void setGuid(String guid) 
    {
        this.guid = guid;
    }

    public String getGuid() 
    {
        return guid;
    }
    public void setEchartName(String echartName) 
    {
        this.echartName = echartName;
    }

    public String getEchartName() 
    {
        return echartName;
    }
    public void setEchartType(String echartType) 
    {
        this.echartType = echartType;
    }

    public String getEchartType() 
    {
        return echartType;
    }
    public void setEchartData(String echartData) 
    {
        this.echartData = echartData;
    }

    public String getEchartData() 
    {
        return echartData;
    }
    public void setEchartImgae(String echartImgae) 
    {
        this.echartImgae = echartImgae;
    }

    public String getEchartImgae() 
    {
        return echartImgae;
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
            .append("guid", getGuid())
            .append("echartName", getEchartName())
            .append("echartType", getEchartType())
            .append("echartData", getEchartData())
            .append("echartImgae", getEchartImgae())
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
