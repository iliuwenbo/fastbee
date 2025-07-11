package com.fastbee.scada.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fastbee.common.annotation.Log;
import com.fastbee.common.core.controller.BaseController;
import com.fastbee.common.core.domain.AjaxResult;
import com.fastbee.common.enums.BusinessType;
import com.fastbee.scada.domain.ScadaModel;
import com.fastbee.scada.service.IScadaModelService;
import com.fastbee.common.utils.poi.ExcelUtil;
import com.fastbee.common.core.page.TableDataInfo;

/**
 * 模型管理Controller
 * 
 * @author kerwincui
 * @date 2023-11-10
 */
@RestController
@RequestMapping("/scada/model")
public class ScadaModelController extends BaseController
{
    @Autowired
    private IScadaModelService scadaModelService;

    /**
     * 查询模型管理列表
     */
    @PreAuthorize("@ss.hasPermi('scada:model:list')")
    @GetMapping("/list")
    public TableDataInfo list(ScadaModel scadaModel)
    {
        startPage();
        List<ScadaModel> list = scadaModelService.selectScadaModelList(scadaModel);
        return getDataTable(list);
    }

    /**
     * 导出模型管理列表
     */
    @PreAuthorize("@ss.hasPermi('scada:model:export')")
    @Log(title = "模型管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ScadaModel scadaModel)
    {
        List<ScadaModel> list = scadaModelService.selectScadaModelList(scadaModel);
        ExcelUtil<ScadaModel> util = new ExcelUtil<ScadaModel>(ScadaModel.class);
        util.exportExcel(response, list, "模型管理数据");
    }

    /**
     * 获取模型管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('scada:model:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(scadaModelService.selectScadaModelById(id));
    }

    /**
     * 新增模型管理
     */
    @PreAuthorize("@ss.hasPermi('scada:model:add')")
    @Log(title = "模型管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ScadaModel scadaModel)
    {
        return toAjax(scadaModelService.insertScadaModel(scadaModel));
    }

    /**
     * 修改模型管理
     */
    @PreAuthorize("@ss.hasPermi('scada:model:edit')")
    @Log(title = "模型管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ScadaModel scadaModel)
    {
        return toAjax(scadaModelService.updateScadaModel(scadaModel));
    }

    /**
     * 删除模型管理
     */
    @PreAuthorize("@ss.hasPermi('scada:model:remove')")
    @Log(title = "模型管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(scadaModelService.deleteScadaModelByIds(ids));
    }
}
