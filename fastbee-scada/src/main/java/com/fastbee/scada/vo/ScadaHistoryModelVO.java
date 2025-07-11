package com.fastbee.scada.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author bill
 */
@Data
public class ScadaHistoryModelVO {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    private String value;

    private String identity;
}
