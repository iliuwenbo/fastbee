package com.fastbee.platform.vo;

import com.fastbee.platform.domain.ApiDefinition;
import com.fastbee.platform.domain.ApiParamDetail;
import com.fastbee.platform.domain.ApiRequestExample;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApiDefinitionVo extends ApiDefinition {
    private List<ApiParamDetail> apiParamDetailList;
}
