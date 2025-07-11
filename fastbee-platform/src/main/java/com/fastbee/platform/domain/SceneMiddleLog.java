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
 * 中包执行日志对象 iot_scene_middle_log
 *
 * @author lwb
 * @date 2025-06-04
 */

@ApiModel(value = "SceneMiddleLog", description = "中包执行日志 iot_scene_middle_log")
@Data
@TableName("iot_scene_middle_log" )
public class SceneMiddleLog implements Serializable{
    private static final long serialVersionUID=1L;

    /** 中包执行日志ID */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("中包执行日志ID")
    private String id;

    /** $column.columnComment */
    @ApiModelProperty("中包id")
    private Long middleId;
    @ApiModelProperty("中包名称")
    private String middleName;

    /** $column.columnComment */
    @ApiModelProperty("小包id")
    private Long sceneId;
    @ApiModelProperty("小包名称")
    private String sceneName;
    private Long previousId;

    /** 标识符 */
    @ApiModelProperty("标识符")
    private String identify;

    /** 消息id */
    @ApiModelProperty("消息id")
    private String messageId;

    /**
     * 消息类型：1=中包；2=小包
     */
    @ApiModelProperty("消息类型：1=中包；2=小包")
    private String messageType;

    /** $column.columnComment */
    @ApiModelProperty("消息内容")
    private String messageValue;

    /** 设备名称 */
    @ApiModelProperty("设备名称")
    private String deviceName;

    /** 设备编号 */
    @ApiModelProperty("设备编号")
    private String serialNumber;

    /** 模式(1=影子模式，2=在线模式，3=其他) */
    @ApiModelProperty("模式(1=影子模式，2=在线模式，3=其他)")
    private Integer mode;

    private String requestId;

    /**
     * 触发状态 1：未触发；2=触发
     */
    @ApiModelProperty("触发状态 1：未触发；2=触发")
    private String status;

    /** 创建者 */
    @ApiModelProperty("创建者")
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /** 备注 */
    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("上报类型")
    private Integer reportType;
    @ApiModelProperty("触发执行源 1=设备触发/执行 2=定时触发 3=产品触发/执行 4=告警执行 5=告警恢复")
    private Integer source;

    /** 脚本用途，1=数据流，2=触发器，3=执行动作 */
    private Integer purpose;
    private String identifyName;
    private String identifyBuffer;

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
