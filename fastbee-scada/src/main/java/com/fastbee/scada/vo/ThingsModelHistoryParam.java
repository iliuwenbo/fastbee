package com.fastbee.scada.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author fastb
 * @version 1.0
 * @description: TODO
 * @date 2024-02-23 16:47
 */
@Data
public class ThingsModelHistoryParam {

    private String serialNumber;

    /** 创建时间 */
    private String beginTime;
    /** 创建时间 */
    private String endTime;

    private List<ThingsModelSim> thingsModelList;

    @Data
    public static class ThingsModelSim{

        private String identifier;

        private Integer type;
    }

}
