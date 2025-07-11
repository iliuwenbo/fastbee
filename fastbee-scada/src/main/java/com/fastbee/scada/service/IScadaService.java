package com.fastbee.scada.service;

import com.fastbee.common.core.domain.AjaxResult;
import com.fastbee.scada.domain.Scada;
import com.fastbee.scada.domain.ScadaGallery;
import com.fastbee.scada.vo.FavoritesVO;
import com.fastbee.scada.vo.ScadaHistoryModelVO;
import com.fastbee.scada.vo.ScadaStatisticVO;
import com.fastbee.scada.vo.ThingsModelHistoryParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 组态中心Service接口
 *
 * @author kerwincui
 * @date 2023-11-10
 */
public interface IScadaService
{
    /**
     * 查询组态中心
     *
     * @param id 组态中心主键
     * @return 组态中心
     */
    public Scada selectScadaById(Long id);

    /**
     * 查询组态中心列表
     *
     * @param scada 组态中心
     * @return 组态中心集合
     */
    public List<Scada> selectScadaList(Scada scada);

    /**
     * 新增组态中心
     *
     * @param scada 组态中心
     * @return 结果
     */
    public AjaxResult insertScada(Scada scada);

    /**
     * 修改组态中心
     *
     * @param scada 组态中心
     * @return 结果
     */
    public int updateScada(Scada scada);

    /**
     * 批量删除组态中心
     *
     * @param ids 需要删除的组态中心主键集合
     * @return 结果
     */
    public int deleteScadaByIds(Long[] ids);

    /**
     * 删除组态中心信息
     *
     * @param id 组态中心主键
     * @return 结果
     */
    public int deleteScadaById(Long id);

    /**
     * 根据guid获取组态详情
     * @param guid 组态id
     * @return
     */
    Scada selectScadaByGuid(String guid);

    /**
     * 图库收藏上传
     * @return com.fastbee.common.core.domain.AjaxResult
     */
    AjaxResult uploadGalleryFavorites(MultipartFile file, String categoryName);

    /**
     * 个人图库收藏
     * @param favoritesVO 收藏vo类
     * @return com.fastbee.common.core.domain.AjaxResult
     */
    AjaxResult saveGalleryFavorites(FavoritesVO favoritesVO);

    /**
     * 个人删除收藏图库
     * @return com.fastbee.common.core.domain.AjaxResult
     */
    AjaxResult deleteGalleryFavorites(Long[] ids);

    /**
     * 查询个人收藏图库
     * @param scadaGallery 图库类
     * @return java.util.List<com.fastbee.scada.domain.ScadaGallery>
     */
    List<ScadaGallery> listGalleryFavorites(ScadaGallery scadaGallery);

    /**
     * 查询变量历史数据
     * @param param 查询条件
     * @return com.fastbee.common.core.domain.AjaxResult
     */
    Map<String, List<ScadaHistoryModelVO>> listThingsModelHistory(ThingsModelHistoryParam param);

    /**
     * 查询设备运行状态
     * @param serialNumber 设备编号
     * @return java.lang.String
     */
    Integer getStatusBySerialNumber(String serialNumber);

    /**
     * 获取系统相关统计信息
     * @return com.fastbee.common.core.domain.AjaxResult
     */
    ScadaStatisticVO selectStatistic();
}
