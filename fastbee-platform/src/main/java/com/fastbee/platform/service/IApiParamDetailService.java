package com.fastbee.platform.service;

import java.util.List;
import com.fastbee.platform.domain.ApiParamDetail;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * API 参数详情Service接口
 *
 * @author lwb
 * @date 2025-04-27
 */
public interface IApiParamDetailService extends IService<ApiParamDetail>
{

    /**
     * 查询API 参数详情列表
     *
     * @param apiParamDetail API 参数详情
     * @return API 参数详情集合
     */
     List<ApiParamDetail> selectApiParamDetailList(ApiParamDetail apiParamDetail);

    /**
     * 查询API 参数详情
     *
     * @param id 主键
     * @return API 参数详情
     */
     ApiParamDetail selectApiParamDetailById(Long id);

    /**
     * 查询API 参数详情
     *
     * @param id 主键
     * @return API 参数详情
     */
    ApiParamDetail queryByIdWithCache(Long id);

    /**
     * 新增API 参数详情
     *
     * @param apiParamDetail API 参数详情
     * @return 是否新增成功
     */
    Boolean insertWithCache(ApiParamDetail apiParamDetail);

    /**
     * 修改API 参数详情
     *
     * @param apiParamDetail API 参数详情
     * @return 是否修改成功
     */
    Boolean updateWithCache(ApiParamDetail apiParamDetail);

    /**
     * 校验并批量删除API 参数详情信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithCacheByIds(Long[] ids, Boolean isValid);

}
