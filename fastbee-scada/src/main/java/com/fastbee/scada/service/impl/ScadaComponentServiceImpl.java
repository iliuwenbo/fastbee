package com.fastbee.scada.service.impl;

import com.fastbee.common.config.RuoYiConfig;
import com.fastbee.common.constant.ScadaConstant;
import com.fastbee.common.core.domain.model.LoginUser;
import com.fastbee.common.exception.ServiceException;
import com.fastbee.common.exception.file.InvalidExtensionException;
import com.fastbee.common.utils.DateUtils;
import com.fastbee.common.utils.StringUtils;
import com.fastbee.common.utils.file.MimeTypeUtils;
import com.fastbee.scada.domain.ScadaComponent;
import com.fastbee.scada.mapper.ScadaComponentMapper;
import com.fastbee.scada.service.IScadaComponentService;
import com.fastbee.scada.utils.ScadaFileUploadUtils;
import com.fastbee.scada.utils.ScadaFileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

import static com.fastbee.common.utils.SecurityUtils.getLoginUser;

/**
 * 组件管理Service业务层处理
 *
 * @author kerwincui
 * @date 2023-11-10
 */
@Service
public class ScadaComponentServiceImpl implements IScadaComponentService
{
    @Resource
    private ScadaComponentMapper scadaComponentMapper;

    /**
     * 查询组件管理
     *
     * @param id 组件管理主键
     * @return 组件管理
     */
    @Override
    public ScadaComponent selectScadaComponentById(Long id)
    {
        return scadaComponentMapper.selectScadaComponentById(id);
    }

    /**
     * 查询组件管理列表
     *
     * @param scadaComponent 组件管理
     * @return 组件管理
     */
    @Override
    public List<ScadaComponent> selectScadaComponentList(ScadaComponent scadaComponent)
    {
        return scadaComponentMapper.selectScadaComponentList(scadaComponent);
    }

    /**
     * 新增组件管理
     *
     * @param scadaComponent 组件管理
     * @return 结果
     */
    @Override
    public int insertScadaComponent(ScadaComponent scadaComponent)
    {
        LoginUser loginUser = getLoginUser();
        scadaComponent.setUserId(loginUser.getUserId());
        if (StringUtils.isEmpty(scadaComponent.getComponentTemplate())) {
            scadaComponent.setComponentTemplate(ScadaConstant.COMPONENT_TEMPLATE_DEFAULT);
        }
        if (StringUtils.isEmpty(scadaComponent.getComponentStyle())) {
            scadaComponent.setComponentStyle(ScadaConstant.COMPONENT_STYLE_DEFAULT);
        }
        if (StringUtils.isEmpty(scadaComponent.getComponentScript())) {
            scadaComponent.setComponentScript(ScadaConstant.COMPONENT_SCRIPT_DEFAULT);
        }
        return scadaComponentMapper.insertScadaComponent(scadaComponent);
    }

    /**
     * 修改组件管理
     *
     * @param scadaComponent 组件管理
     * @return 结果
     */
    @Override
    public int updateScadaComponent(ScadaComponent scadaComponent)
    {
        scadaComponent.setUpdateTime(DateUtils.getNowDate());
        if (StringUtils.isNotEmpty(scadaComponent.getBase64())) {
            MultipartFile multipartFile = ScadaFileUtils.base64toMultipartFile(scadaComponent.getBase64());
            String url;
            try {
                url = ScadaFileUploadUtils.upload(RuoYiConfig.getUploadPath(), multipartFile, MimeTypeUtils.IMAGE_EXTENSION);
            } catch (IOException | InvalidExtensionException e) {
                throw new ServiceException("修改组件base64转图片异常" + e.getMessage());
            }
            scadaComponent.setComponentImage(url);
        }
        return scadaComponentMapper.updateScadaComponent(scadaComponent);
    }

    /**
     * 批量删除组件管理
     *
     * @param ids 需要删除的组件管理主键
     * @return 结果
     */
    @Override
    public int deleteScadaComponentByIds(Long[] ids)
    {
        return scadaComponentMapper.deleteScadaComponentByIds(ids);
    }

    /**
     * 删除组件管理信息
     *
     * @param id 组件管理主键
     * @return 结果
     */
    @Override
    public int deleteScadaComponentById(Long id)
    {
        return scadaComponentMapper.deleteScadaComponentById(id);
    }
}
