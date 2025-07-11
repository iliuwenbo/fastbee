package com.fastbee.scada.mapper;

import java.util.List;
import java.util.Set;

import com.fastbee.scada.domain.ScadaGallery;
import org.apache.ibatis.annotations.Param;

/**
 * 图库管理Mapper接口
 * 
 * @author kerwincui
 * @date 2023-11-10
 */
public interface ScadaGalleryMapper 
{
    /**
     * 查询图库管理
     * 
     * @param id 图库管理主键
     * @return 图库管理
     */
    public ScadaGallery selectScadaGalleryById(Long id);

    /**
     * 查询图库管理列表
     * 
     * @param scadaGallery 图库管理
     * @return 图库管理集合
     */
    public List<ScadaGallery> selectScadaGalleryList(ScadaGallery scadaGallery);

    /**
     * 新增图库管理
     * 
     * @param scadaGallery 图库管理
     * @return 结果
     */
    public int insertScadaGallery(ScadaGallery scadaGallery);

    /**
     * 修改图库管理
     * 
     * @param scadaGallery 图库管理
     * @return 结果
     */
    public int updateScadaGallery(ScadaGallery scadaGallery);

    /**
     * 删除图库管理
     * 
     * @param id 图库管理主键
     * @return 结果
     */
    public int deleteScadaGalleryById(Long id);

    /**
     * 批量删除图库管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteScadaGalleryByIds(Long[] ids);

    /**
     * 查询图库
     * @param idSet 主键集合
     * @return java.util.List<com.fastbee.scada.domain.ScadaGallery>
     */
    List<ScadaGallery> selectScadaGalleryByIdSet(Set<Long> idSet);
}
