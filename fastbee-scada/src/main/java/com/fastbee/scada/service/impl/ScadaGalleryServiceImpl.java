package com.fastbee.scada.service.impl;

import com.fastbee.common.config.RuoYiConfig;
import com.fastbee.common.core.domain.AjaxResult;
import com.fastbee.common.core.domain.model.LoginUser;
import com.fastbee.common.utils.DateUtils;
import com.fastbee.common.utils.file.FileUploadUtils;
import com.fastbee.common.utils.file.FileUtils;
import com.fastbee.scada.domain.ScadaGallery;
import com.fastbee.scada.mapper.ScadaGalleryMapper;
import com.fastbee.scada.service.IScadaGalleryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

import static com.fastbee.common.utils.SecurityUtils.getLoginUser;

/**
 * 图库管理Service业务层处理
 *
 * @author kerwincui
 * @date 2023-11-10
 */
@Service
public class ScadaGalleryServiceImpl implements IScadaGalleryService
{
    @Resource
    private ScadaGalleryMapper scadaGalleryMapper;

    /**
     * 查询图库管理
     *
     * @param id 图库管理主键
     * @return 图库管理
     */
    @Override
    public ScadaGallery selectScadaGalleryById(Long id)
    {
        return scadaGalleryMapper.selectScadaGalleryById(id);
    }

    /**
     * 查询图库管理列表
     *
     * @param scadaGallery 图库管理
     * @return 图库管理
     */
    @Override
    public List<ScadaGallery> selectScadaGalleryList(ScadaGallery scadaGallery)
    {
        return scadaGalleryMapper.selectScadaGalleryList(scadaGallery);
    }

    /**
     * 新增图库管理
     *
     * @param scadaGallery 图库管理
     * @return 结果
     */
    @Override
    public int insertScadaGallery(ScadaGallery scadaGallery)
    {
        scadaGallery.setCreateTime(DateUtils.getNowDate());
        return scadaGalleryMapper.insertScadaGallery(scadaGallery);
    }

    /**
     * 修改图库管理
     *
     * @param scadaGallery 图库管理
     * @return 结果
     */
    @Override
    public int updateScadaGallery(ScadaGallery scadaGallery)
    {
        scadaGallery.setUpdateTime(DateUtils.getNowDate());
        return scadaGalleryMapper.updateScadaGallery(scadaGallery);
    }

    /**
     * 批量删除图库管理
     *
     * @param ids 需要删除的图库管理主键
     * @return 结果
     */
    @Override
    public int deleteScadaGalleryByIds(Long[] ids)
    {
        return scadaGalleryMapper.deleteScadaGalleryByIds(ids);
    }

    /**
     * 删除图库管理信息
     *
     * @param id 图库管理主键
     * @return 结果
     */
    @Override
    public int deleteScadaGalleryById(Long id)
    {
        return scadaGalleryMapper.deleteScadaGalleryById(id);
    }

    @Override
    public AjaxResult uploadFile(MultipartFile file, String categoryName) {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return AjaxResult.error("请重新登录");
        }
        // 上传文件路径
        String filePath = "";
        // 上传并返回新文件名称
        try{
            filePath = FileUploadUtils.upload(RuoYiConfig.getUploadPath(), file);
        }catch (Exception e){
            return AjaxResult.error(500,"上传图库文件异常，"+ e);
        }
        String fileName = file.getOriginalFilename();
        ScadaGallery scadaGallery = new ScadaGallery();
        scadaGallery.setFileName(fileName);
        scadaGallery.setCategoryName(categoryName);
        scadaGallery.setResourceUrl(filePath);
        return scadaGalleryMapper.insertScadaGallery(scadaGallery) > 0 ? AjaxResult.success("上传成功") : AjaxResult.error("上传失败");
    }
}
