package com.fastbee.scada.service.impl;

import com.fastbee.common.config.RuoYiConfig;
import com.fastbee.common.exception.ServiceException;
import com.fastbee.common.exception.file.InvalidExtensionException;
import com.fastbee.common.utils.DateUtils;
import com.fastbee.common.utils.StringUtils;
import com.fastbee.common.utils.file.MimeTypeUtils;
import com.fastbee.scada.domain.ScadaEchart;
import com.fastbee.scada.mapper.ScadaEchartMapper;
import com.fastbee.scada.service.IScadaEchartService;
import com.fastbee.scada.utils.ScadaFileUploadUtils;
import com.fastbee.scada.utils.ScadaFileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * 图表管理Service业务层处理
 *
 * @author kerwincui
 * @date 2023-11-10
 */
@Service
public class ScadaEchartServiceImpl implements IScadaEchartService
{
    @Resource
    private ScadaEchartMapper scadaEchartMapper;

    /**
     * 查询图表管理
     *
     * @param id 图表管理主键
     * @return 图表管理
     */
    @Override
    public ScadaEchart selectScadaEchartById(Long id)
    {
        return scadaEchartMapper.selectScadaEchartById(id);
    }

    /**
     * 查询图表管理列表
     *
     * @param scadaEchart 图表管理
     * @return 图表管理
     */
    @Override
    public List<ScadaEchart> selectScadaEchartList(ScadaEchart scadaEchart)
    {
        return scadaEchartMapper.selectScadaEchartList(scadaEchart);
    }

    /**
     * 新增图表管理
     *
     * @param scadaEchart 图表管理
     * @return 结果
     */
    @Override
    public int insertScadaEchart(ScadaEchart scadaEchart) {
        scadaEchart.setCreateTime(DateUtils.getNowDate());
        if (StringUtils.isNotEmpty(scadaEchart.getBase64())) {
            MultipartFile multipartFile = ScadaFileUtils.base64toMultipartFile(scadaEchart.getBase64());
            String url;
            try {
                url = ScadaFileUploadUtils.upload(RuoYiConfig.getUploadPath(), multipartFile, MimeTypeUtils.IMAGE_EXTENSION);
            } catch (IOException | InvalidExtensionException e) {
                throw new ServiceException("新增图表base64转图片异常" + e.getMessage());
            }
            scadaEchart.setEchartImgae(url);
        }
        return scadaEchartMapper.insertScadaEchart(scadaEchart);
    }

    /**
     * 修改图表管理
     *
     * @param scadaEchart 图表管理
     * @return 结果
     */
    @Override
    public int updateScadaEchart(ScadaEchart scadaEchart) {
        scadaEchart.setUpdateTime(DateUtils.getNowDate());
        if (StringUtils.isNotEmpty(scadaEchart.getBase64())) {
            MultipartFile multipartFile = ScadaFileUtils.base64toMultipartFile(scadaEchart.getBase64());
            String url;
            try {
                url = ScadaFileUploadUtils.upload(RuoYiConfig.getUploadPath(), multipartFile, MimeTypeUtils.IMAGE_EXTENSION);
            } catch (IOException | InvalidExtensionException e) {
                throw new ServiceException("新增图表base64转图片异常" + e.getMessage());
            }
            scadaEchart.setEchartImgae(url);
        }
        return scadaEchartMapper.updateScadaEchart(scadaEchart);
    }

    /**
     * 批量删除图表管理
     *
     * @param ids 需要删除的图表管理主键
     * @return 结果
     */
    @Override
    public int deleteScadaEchartByIds(Long[] ids)
    {
        return scadaEchartMapper.deleteScadaEchartByIds(ids);
    }

    /**
     * 删除图表管理信息
     *
     * @param id 图表管理主键
     * @return 结果
     */
    @Override
    public int deleteScadaEchartById(Long id)
    {
        return scadaEchartMapper.deleteScadaEchartById(id);
    }
}
