package com.fastbee.platform.service;

import java.util.List;
import com.fastbee.platform.domain.ApiRequestExample;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * API 请求示例Service接口
 *
 * @author lwb
 * @date 2025-04-27
 */
public interface IApiRequestExampleService extends IService<ApiRequestExample>
{

    /**
     * 查询API 请求示例列表
     *
     * @param apiRequestExample API 请求示例
     * @return API 请求示例集合
     */
     List<ApiRequestExample> selectApiRequestExampleList(ApiRequestExample apiRequestExample);

    /**
     * 查询API 请求示例
     *
     * @param id 主键
     * @return API 请求示例
     */
     ApiRequestExample selectApiRequestExampleById(Long id);

    /**
     * 查询API 请求示例
     *
     * @param id 主键
     * @return API 请求示例
     */
    ApiRequestExample queryByIdWithCache(Long id);

    /**
     * 新增API 请求示例
     *
     * @param apiRequestExample API 请求示例
     * @return 是否新增成功
     */
    Boolean insertWithCache(ApiRequestExample apiRequestExample);

    /**
     * 修改API 请求示例
     *
     * @param apiRequestExample API 请求示例
     * @return 是否修改成功
     */
    Boolean updateWithCache(ApiRequestExample apiRequestExample);

    /**
     * 校验并批量删除API 请求示例信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithCacheByIds(Long[] ids, Boolean isValid);

}
