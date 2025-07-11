package com.fastbee.platform.mapper;

import java.util.List;

import com.fastbee.framework.mybatis.mapper.BaseMapperX;
import com.fastbee.platform.domain.ApiParamDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * API 参数详情Mapper接口
 *
 * @author lwb
 * @date 2025-04-27
 */
@Mapper
public interface ApiParamDetailMapper extends BaseMapperX<ApiParamDetail>
{

    int deleteApiDetailId(Long id);
}
