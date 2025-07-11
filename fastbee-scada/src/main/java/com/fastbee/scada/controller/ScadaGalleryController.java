package com.fastbee.scada.controller;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.fastbee.common.annotation.Anonymous;
import com.fastbee.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fastbee.common.annotation.Log;
import com.fastbee.common.core.controller.BaseController;
import com.fastbee.common.core.domain.AjaxResult;
import com.fastbee.common.enums.BusinessType;
import com.fastbee.scada.domain.ScadaGallery;
import com.fastbee.scada.service.IScadaGalleryService;
import com.fastbee.common.utils.poi.ExcelUtil;
import com.fastbee.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图库管理Controller
 *
 * @author kerwincui
 * @date 2023-11-10
 */
@Api(tags = "图库管理")
@RestController
@RequestMapping("/scada/gallery")
public class ScadaGalleryController extends BaseController
{
    @Resource
    private IScadaGalleryService scadaGalleryService;

    /**
     * 查询图库管理列表
     */
    @ApiOperation("查询图库管理")
    @PreAuthorize("@ss.hasPermi('scada:gallery:list')")
    @GetMapping("/list")
    public TableDataInfo list(ScadaGallery scadaGallery)
    {
        startPage();
        List<ScadaGallery> list = scadaGalleryService.selectScadaGalleryList(scadaGallery);
        return getDataTable(list);
    }

    /**
     * 导出图库管理列表
     */
    @ApiOperation("导出图库管理列表")
    @PreAuthorize("@ss.hasPermi('scada:gallery:export')")
    @Log(title = "图库管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ScadaGallery scadaGallery)
    {
        List<ScadaGallery> list = scadaGalleryService.selectScadaGalleryList(scadaGallery);
        ExcelUtil<ScadaGallery> util = new ExcelUtil<ScadaGallery>(ScadaGallery.class);
        util.exportExcel(response, list, "图库管理数据");
    }

    /**
     * 获取图库管理详细信息
     */
    @ApiOperation("获取图库详细信息")
    @PreAuthorize("@ss.hasPermi('scada:gallery:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(scadaGalleryService.selectScadaGalleryById(id));
    }

    /**
     * 新增图库管理
     */
    @ApiOperation("新增图库")
    @PreAuthorize("@ss.hasPermi('scada:gallery:add')")
    @Log(title = "图库管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ScadaGallery scadaGallery)
    {
        return toAjax(scadaGalleryService.insertScadaGallery(scadaGallery));
    }

    /**
     * 修改图库管理
     */
    @ApiOperation("修改图库")
    @PreAuthorize("@ss.hasPermi('scada:gallery:edit')")
    @Log(title = "图库管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ScadaGallery scadaGallery)
    {
        return toAjax(scadaGalleryService.updateScadaGallery(scadaGallery));
    }

    /**
     * 删除图库管理
     */
    @ApiOperation("删除图库")
    @PreAuthorize("@ss.hasPermi('scada:gallery:remove')")
    @Log(title = "图库管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(scadaGalleryService.deleteScadaGalleryByIds(ids));
    }

    /**
     * 上传文件
     * @param file 文件
     * @param categoryName 分类名称
     * @return
     */
    @ApiOperation("上传文件")
    @PreAuthorize("@ss.hasPermi('scada:gallery:add')")
    @PostMapping("/uploadFile")
    public AjaxResult uploadFile(MultipartFile file, String categoryName) {
        if (file == null) {
            return error("上传文件为空");
        }
        if (StringUtils.isEmpty(categoryName)) {
            return error("上传文件分类为空");
        }
        return scadaGalleryService.uploadFile(file, categoryName);
    }
}
