package com.fastbee.platform.service;

import java.util.List;

import cn.hutool.json.JSONObject;
import com.fastbee.iot.domain.Bridge;
import com.fastbee.iot.domain.Device;
import com.fastbee.platform.domain.ApiDefinition;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fastbee.platform.domain.ApiDevice;

/**
 * 接口定义Service接口
 *
 * @author lwb
 * @date 2025-04-27
 */
public interface IApiDefinitionService extends IService<ApiDefinition>
{

    /**
     * 查询接口定义列表
     *
     * @param apiDefinition 接口定义
     * @return 接口定义集合
     */
     List<ApiDefinition> selectApiDefinitionList(ApiDefinition apiDefinition);

    /**
     * 查询接口定义
     *
     * @param id 主键
     * @return 接口定义
     */
     ApiDefinition selectApiDefinitionById(Long id);

    /**
     * 查询接口定义
     *
     * @param id 主键
     * @return 接口定义
     */
    ApiDefinition queryByIdWithCache(Long id);

    /**
     * 新增接口定义
     *
     * @param apiDefinition 接口定义
     * @return 是否新增成功
     */
    Boolean insertWithCache(ApiDefinition apiDefinition);


    /**
     * 新增接口定义
     *
     * @param apiDefinitions 接口定义
     * @return 是否新增成功
     */
    Boolean insertWithCache(List<ApiDefinition> apiDefinitions);

    /**
     * 修改接口定义
     *
     * @param apiDefinition 接口定义
     * @return 是否修改成功
     */
    Boolean updateWithCache(ApiDefinition apiDefinition);

    /**
     * 校验并批量删除接口定义信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithCacheByIds(Long[] ids, Boolean isValid);

    JSONObject connect(Long id);
    List<ApiDevice> connectDevice(Long apiDefinitionId);

}
