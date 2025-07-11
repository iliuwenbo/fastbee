package com.fastbee.platform.controller;

import java.util.List;
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
import com.fastbee.common.core.controller.BaseController;
import com.fastbee.common.core.domain.AjaxResult;
import com.fastbee.common.utils.poi.ExcelUtil;
import com.fastbee.platform.domain.ApiRequestExample;
import com.fastbee.platform.service.IApiRequestExampleService;
import com.fastbee.common.core.page.TableDataInfo;

/**
 * API 请求示例Controller
 *
 * @author lwb
 * @date 2025-04-27
 */
@RestController
@RequestMapping("/api/example")
@Api(tags = "API 请求示例")
public class ApiRequestExampleController extends BaseController {
    @Autowired
    private IApiRequestExampleService apiRequestExampleService;

    /**
     * 查询API 请求示例列表
     */
    @PreAuthorize("@ss.hasPermi('api:example:list')")
    @GetMapping("/list")
    @ApiOperation("查询API 请求示例列表")
    public TableDataInfo list(ApiRequestExample apiRequestExample) {
        startPage();
        List<ApiRequestExample> list = apiRequestExampleService.selectApiRequestExampleList(apiRequestExample);
        return getDataTable(list);
    }

    /**
     * 导出API 请求示例列表
     */
    @ApiOperation("导出API 请求示例列表")
    @PreAuthorize("@ss.hasPermi('api:example:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, ApiRequestExample apiRequestExample) {
        List<ApiRequestExample> list = apiRequestExampleService.selectApiRequestExampleList(apiRequestExample);
        ExcelUtil<ApiRequestExample> util = new ExcelUtil<ApiRequestExample>(ApiRequestExample. class);
        util.exportExcel(response, list, "API 请求示例数据");
    }

    /**
     * 获取API 请求示例详细信息
     */
    @PreAuthorize("@ss.hasPermi('api:example:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取API 请求示例详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(apiRequestExampleService.queryByIdWithCache(id));
    }

    /**
     * 新增API 请求示例
     */
    @PreAuthorize("@ss.hasPermi('api:example:add')")
    @PostMapping
    @ApiOperation("新增API 请求示例")
    public AjaxResult add(@RequestBody ApiRequestExample apiRequestExample) {
        return toAjax(apiRequestExampleService.insertWithCache(apiRequestExample));
    }

    /**
     * 修改API 请求示例
     */
    @PreAuthorize("@ss.hasPermi('api:example:edit')")
    @PutMapping
    @ApiOperation("修改API 请求示例")
    public AjaxResult edit(@RequestBody ApiRequestExample apiRequestExample) {
        return toAjax(apiRequestExampleService.updateWithCache(apiRequestExample));
    }

    /**
     * 删除API 请求示例
     */
    @PreAuthorize("@ss.hasPermi('api:example:remove')")
    @DeleteMapping("/{ids}")
    @ApiOperation("删除API 请求示例")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(apiRequestExampleService.deleteWithCacheByIds(ids, true));
    }
}
