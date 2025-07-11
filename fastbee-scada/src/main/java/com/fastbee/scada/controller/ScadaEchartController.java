package com.fastbee.scada.controller;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.RestController;
import com.fastbee.common.annotation.Log;
import com.fastbee.common.core.controller.BaseController;
import com.fastbee.common.core.domain.AjaxResult;
import com.fastbee.common.enums.BusinessType;
import com.fastbee.scada.domain.ScadaEchart;
import com.fastbee.scada.service.IScadaEchartService;
import com.fastbee.common.utils.poi.ExcelUtil;
import com.fastbee.common.core.page.TableDataInfo;

/**
 * 图表管理Controller
 * 
 * @author kerwincui
 * @date 2023-11-10
 */
@Api(tags = "图表管理")
@RestController
@RequestMapping("/scada/echart")
public class ScadaEchartController extends BaseController
{
    @Resource
    private IScadaEchartService scadaEchartService;

    /**
     * 查询图表管理列表
     */
    @ApiOperation("查询图表管理列表")
    @PreAuthorize("@ss.hasPermi('scada:echart:list')")
    @GetMapping("/list")
    public TableDataInfo list(ScadaEchart scadaEchart)
    {
        startPage();
        List<ScadaEchart> list = scadaEchartService.selectScadaEchartList(scadaEchart);
        return getDataTable(list);
    }

    /**
     * 导出图表管理列表
     */
    @ApiOperation("导出图表管理列表")
    @PreAuthorize("@ss.hasPermi('scada:echart:export')")
    @Log(title = "图表管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ScadaEchart scadaEchart)
    {
        List<ScadaEchart> list = scadaEchartService.selectScadaEchartList(scadaEchart);
        ExcelUtil<ScadaEchart> util = new ExcelUtil<ScadaEchart>(ScadaEchart.class);
        util.exportExcel(response, list, "图表管理数据");
    }

    /**
     * 获取图表管理详细信息
     */
    @ApiOperation("获取图表管理详细信息")
    @PreAuthorize("@ss.hasPermi('scada:echart:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(scadaEchartService.selectScadaEchartById(id));
    }

    /**
     * 新增图表管理
     */
    @ApiOperation("新增图表")
    @PreAuthorize("@ss.hasPermi('scada:echart:add')")
    @Log(title = "图表管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ScadaEchart scadaEchart)
    {
        return toAjax(scadaEchartService.insertScadaEchart(scadaEchart));
    }

    /**
     * 修改图表管理
     */
    @ApiOperation("修改图表")
    @PreAuthorize("@ss.hasPermi('scada:echart:edit')")
    @Log(title = "图表管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ScadaEchart scadaEchart)
    {
        return toAjax(scadaEchartService.updateScadaEchart(scadaEchart));
    }

    /**
     * 删除图表管理
     */
    @ApiOperation("删除图表")
    @PreAuthorize("@ss.hasPermi('scada:echart:remove')")
    @Log(title = "图表管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(scadaEchartService.deleteScadaEchartByIds(ids));
    }
}
