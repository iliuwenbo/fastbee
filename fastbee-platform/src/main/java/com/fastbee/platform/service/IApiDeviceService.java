package com.fastbee.platform.service;

import java.util.List;
import com.fastbee.platform.domain.ApiDevice;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 平台接入设备中间Service接口
 *
 * @author lwb
 * @date 2025-04-27
 */
public interface IApiDeviceService extends IService<ApiDevice>
{

    /**
     * 查询平台接入设备中间列表
     *
     * @param apiDevice 平台接入设备中间
     * @return 平台接入设备中间集合
     */
     List<ApiDevice> selectApiDeviceList(ApiDevice apiDevice);

    /**
     * 查询平台接入设备中间
     *
     * @param id 主键
     * @return 平台接入设备中间
     */
     ApiDevice selectApiDeviceById(String id);

    /**
     * 查询平台接入设备中间
     *
     * @param id 主键
     * @return 平台接入设备中间
     */
    ApiDevice queryByIdWithCache(String id);

    /**
     * 新增平台接入设备中间
     *
     * @param apiDevice 平台接入设备中间
     * @return 是否新增成功
     */
    Boolean insertWithCache(ApiDevice apiDevice);

    /**
     * 修改平台接入设备中间
     *
     * @param apiDevice 平台接入设备中间
     * @return 是否修改成功
     */
    Boolean updateWithCache(ApiDevice apiDevice);

    /**
     * 校验并批量删除平台接入设备中间信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithCacheByIds(String[] ids, Boolean isValid);

}
