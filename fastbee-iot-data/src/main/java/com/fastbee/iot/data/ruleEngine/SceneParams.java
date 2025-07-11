package com.fastbee.iot.data.ruleEngine;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fastbee.platform.domain.SceneMiddleLog;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SceneParams{
    private static final long serialVersionUID=1L;

    /** 中包执行日志ID */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("中包执行日志ID")
    private Long id;

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
    @ApiModelProperty("消息类型")
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

    /** 备注 */
    @ApiModelProperty("备注")
    private String remark;

    private int reportType;
    private Boolean identifyStatus = true;

}
