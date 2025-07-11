package com.fastbee.iot.model;

import com.fastbee.common.annotation.Excel;
import com.fastbee.iot.model.ThingsModelItem.*;
import com.fastbee.iot.model.ThingsModels.ThingsModelValueItem;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 设备对象 iot_device
 *
 * @author kerwincui
 * @date 2021-12-16
 */
@Data
public class SocialDeviceShortOutput
{


    /** 产品分类ID */
    private Long deviceId;

    /** 产品分类名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 产品ID */
    @Excel(name = "产品ID")
    private Long productId;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String productName;

    /** 设备类型（1-直连设备、2-网关子设备、3-网关设备） */
    private Integer deviceType;

    /** 租户ID */
    @Excel(name = "租户ID")
    private Long tenantId;

    /** 租户名称 */
    @Excel(name = "租户名称")
    private String tenantName;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String serialNumber;

    /** 固件版本 */
    @Excel(name = "固件版本")
    private BigDecimal firmwareVersion;

    /** 设备状态（1-未激活，2-禁用，3-在线，4-离线） */
    @Excel(name = "设备状态")
    private Integer status;

    /** 设备影子 */
    private Integer isShadow;

    private Integer isSimulate;

    /** wifi信号强度（信号极好4格[-55— 0]，信号好3格[-70— -55]，信号一般2格[-85— -70]，信号差1格[-100— -85]） */
    @Excel(name = "wifi信号强度")
    private Integer rssi;

    @Excel(name = "物模型")
    private String thingsModelValue;

    /** 激活时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "激活时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date activeTime;

    /** 激活时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createTime;

    @Excel(name = "网关设备编号(子设备使用)")
    private String gwDevCode;

    /** 是否自定义位置 **/
    private Integer locationWay;

    /** 图片地址 */
    private String imgUrl;

    /** 是否设备所有者，用于查询 **/
    private Integer isOwner;


    /**子设备数量*/
    private Integer subDeviceCount;

    /**子设备地址*/
    private Integer slaveId;
    /*传输协议*/
    private String transport;
    /*设备通讯协议*/
    private String protocolCode;

    private DeviceAlertCount alertCount;
    /**
     * 产品guid
     */
    private String guid;



    private List<ThingsModelValueItem> thingsModels;
    private Map<String,Object> uriEntity;

//
//    private List<StringModelOutput> stringList;
//    private List<IntegerModelOutput> integerList;
//    private List<DecimalModelOutput> decimalList;
//    private List<EnumModelOutput> enumList;
//    private List<ArrayModelOutput> arrayList;
//    private List<BoolModelOutput> boolList;
//    private List<ReadOnlyModelOutput> readOnlyList;


}
