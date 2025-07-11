package com.fastbee.iot.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fastbee.iot.util.LiteFlowJsonParser;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 场景中包对象 iot_scene_middle
 *
 * @author lwb
 * @date 2025-05-22
 */

@ApiModel(value = "SceneMiddle", description = "场景中包 iot_scene_middle")
@Data
@TableName("iot_scene_middle" )
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SceneMiddle implements Serializable{
    private static final long serialVersionUID=1L;

    /** $column.columnComment */
    @TableId(value = "middle_id", type = IdType.AUTO)
    @ApiModelProperty("${comment}")
    private Long middleId;

    /** 中包名称 */
    @ApiModelProperty("中包名称")
    private String middleName;

    /**
     * 规则名称
     */
    private String chainName;

    /** 场景状态（1-启动，2-停止） */
    @ApiModelProperty("场景状态")
    private Integer enable;
    /**
     * 关联小包ID
     */
    @ApiModelProperty("关联小包ID")
    private Long iotSceneId;

    /** 关联小包IDS */
    @ApiModelProperty("关联小包IDS")
    private String iotSceneIds;

    /** 规则数据 */
    @ApiModelProperty("规则数据")
    private String elData;

    /** 创建者 */
    @ApiModelProperty("创建者")
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /** 更新者 */
    @ApiModelProperty("更新者")
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /** 备注 */
    @ApiModelProperty("备注")
    private String remark;

    /** 前端回显的json */
    @ApiModelProperty("前端回显的json")
    private String json;

    /**
     * 前端传入的流程json
     */
    @TableField(exist = false)
    private LiteFlowJsonParser.FlowNode flowNode;

    @TableField(exist = false)
    private String iotSceneNames;

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
