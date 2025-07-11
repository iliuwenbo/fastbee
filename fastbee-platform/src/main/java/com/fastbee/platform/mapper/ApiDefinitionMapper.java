package com.fastbee.platform.mapper;

import com.fastbee.framework.mybatis.mapper.BaseMapperX;
import com.fastbee.platform.domain.ApiDefinition;
import com.fastbee.platform.vo.ApiDefinitionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 接口定义Mapper接口
 *
 * @author lwb
 * @date 2025-04-27
 */
@Mapper
public interface ApiDefinitionMapper extends BaseMapperX<ApiDefinition>
{
    ApiDefinitionVo selectDetailById(@Param("id") Long id);
}
