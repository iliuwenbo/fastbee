package com.fastbee.scada.domain;

import com.fastbee.common.annotation.Excel;
import com.fastbee.common.core.domain.BaseEntity;
import com.fastbee.scada.vo.ScadaBindDeviceSimVO;
import com.fastbee.scada.vo.ScadaDeviceBindVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 组态中心对象 scada
 * 
 * @author kerwincui
 * @date 2023-11-10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class Scada extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id唯一标识 */
    private Long id;

    /** 组态id */
    @Excel(name = "组态id")
    private String guid;

    /** 组态信息 */
    @Excel(name = "组态信息")
    private String scadaData;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String serialNumbers;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 是否主界面 */
    @Excel(name = "是否主界面")
    private Integer isMainPage;

    /** 页面名称 */
    @Excel(name = "页面名称")
    private String pageName;

    /** 页面大小 */
    @Excel(name = "页面大小")
    private String pageResolution;

    /** 是否分享 */
    @Excel(name = "是否分享")
    private Integer isShare;

    /** 分享链接 */
    @Excel(name = "分享链接")
    private String shareUrl;

    /** 分享密码 */
    @Excel(name = "分享密码")
    private String sharePass;

    /** 页面图片 */
    @Excel(name = "页面图片")
    private String pageImage;

    /** 租户id */
    @Excel(name = "租户id")
    private Long tenantId;

    /** 租户名称 */
    @Excel(name = "租户名称")
    private String tenantName;

    /** 逻辑删除标识 */
    private Integer delFlag;

    private String base64;

    private List<ScadaBindDeviceSimVO> bindDeviceList;

}
