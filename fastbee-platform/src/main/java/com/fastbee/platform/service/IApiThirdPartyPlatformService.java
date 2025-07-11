package com.fastbee.platform.service;

import java.util.List;
import com.fastbee.platform.domain.ApiThirdPartyPlatform;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 第三方平台信息Service接口
 *
 * @author lwb
 * @date 2025-06-04
 */
public interface IApiThirdPartyPlatformService extends IService<ApiThirdPartyPlatform>
{

    /**
     * 查询第三方平台信息列表
     *
     * @param apiThirdPartyPlatform 第三方平台信息
     * @return 第三方平台信息集合
     */
     List<ApiThirdPartyPlatform> selectApiThirdPartyPlatformList(ApiThirdPartyPlatform apiThirdPartyPlatform);

    /**
     * 查询第三方平台信息
     *
     * @param id 主键
     * @return 第三方平台信息
     */
     ApiThirdPartyPlatform selectApiThirdPartyPlatformById(Long id);

    /**
     * 查询第三方平台信息
     *
     * @param id 主键
     * @return 第三方平台信息
     */
    ApiThirdPartyPlatform queryByIdWithCache(Long id);

    /**
     * 新增第三方平台信息
     *
     * @param apiThirdPartyPlatform 第三方平台信息
     * @return 是否新增成功
     */
    Boolean insertWithCache(ApiThirdPartyPlatform apiThirdPartyPlatform);

    /**
     * 修改第三方平台信息
     *
     * @param apiThirdPartyPlatform 第三方平台信息
     * @return 是否修改成功
     */
    Boolean updateWithCache(ApiThirdPartyPlatform apiThirdPartyPlatform);

    /**
     * 校验并批量删除第三方平台信息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithCacheByIds(Long[] ids, Boolean isValid);

}
