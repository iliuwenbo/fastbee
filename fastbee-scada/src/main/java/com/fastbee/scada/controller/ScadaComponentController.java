package com.fastbee.scada.controller;

import com.fastbee.common.annotation.Anonymous;
import com.fastbee.common.annotation.Log;
import com.fastbee.common.core.controller.BaseController;
import com.fastbee.common.core.domain.AjaxResult;
import com.fastbee.common.core.page.TableDataInfo;
import com.fastbee.common.enums.BusinessType;
import com.fastbee.common.utils.poi.ExcelUtil;
import com.fastbee.scada.domain.ScadaComponent;
import com.fastbee.scada.service.IScadaComponentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 组件管理Controller
 *
 * @author kerwincui
 * @date 2023-11-10
 */
@Api(tags = "组态组件管理")
@RestController
@RequestMapping("/scada/component")
public class ScadaComponentController extends BaseController
{
    @Resource
    private IScadaComponentService scadaComponentService;

    /**
     * 查询组件管理列表
     */
    @ApiOperation("查询组件管理列表")
    @PreAuthorize("@ss.hasPermi('scada:component:list')")
    @GetMapping("/list")
    public TableDataInfo list(ScadaComponent scadaComponent)
    {
        startPage();
        List<ScadaComponent> list = scadaComponentService.selectScadaComponentList(scadaComponent);
        return getDataTable(list);
    }

    /**
     * 导出组件管理列表
     */
    @ApiOperation("导出组件管理列表")
    @PreAuthorize("@ss.hasPermi('scada:component:export')")
    @Log(title = "组件管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ScadaComponent scadaComponent)
    {
        List<ScadaComponent> list = scadaComponentService.selectScadaComponentList(scadaComponent);
        ExcelUtil<ScadaComponent> util = new ExcelUtil<ScadaComponent>(ScadaComponent.class);
        util.exportExcel(response, list, "组件管理数据");
    }

    /**
     * 获取组件管理详细信息
     */
    @ApiOperation("获取组件管理详细信息")
    @PreAuthorize("@ss.hasPermi('scada:component:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(scadaComponentService.selectScadaComponentById(id));
    }

    /**
     * 新增组件管理
     */
    @ApiOperation("新增组件")
    @PreAuthorize("@ss.hasPermi('scada:component:add')")
    @Log(title = "组件管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ScadaComponent scadaComponent)
    {
        return toAjax(scadaComponentService.insertScadaComponent(scadaComponent));
    }

    /**
     * 修改组件管理
     */
    @ApiOperation("修改组件")
    @PreAuthorize("@ss.hasPermi('scada:component:edit')")
    @Log(title = "组件管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ScadaComponent scadaComponent)
    {
        return toAjax(scadaComponentService.updateScadaComponent(scadaComponent));
    }

    /**
     * 删除组件管理
     */
    @ApiOperation("删除组件")
    @PreAuthorize("@ss.hasPermi('scada:component:remove')")
    @Log(title = "组件管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(scadaComponentService.deleteScadaComponentByIds(ids));
    }
}
