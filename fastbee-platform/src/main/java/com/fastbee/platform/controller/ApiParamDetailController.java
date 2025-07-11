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
import com.fastbee.platform.domain.ApiParamDetail;
import com.fastbee.platform.service.IApiParamDetailService;
import com.fastbee.common.core.page.TableDataInfo;

/**
 * API 参数详情Controller
 *
 * @author lwb
 * @date 2025-04-27
 */
@RestController
@RequestMapping("/api/detail")
@Api(tags = "API 参数详情")
public class ApiParamDetailController extends BaseController {
    @Autowired
    private IApiParamDetailService apiParamDetailService;

    /**
     * 查询API 参数详情列表
     */
    @PreAuthorize("@ss.hasPermi('api:detail:list')")
    @GetMapping("/list")
    @ApiOperation("查询API 参数详情列表")
    public TableDataInfo list(ApiParamDetail apiParamDetail) {
        startPage();
        List<ApiParamDetail> list = apiParamDetailService.selectApiParamDetailList(apiParamDetail);
        return getDataTable(list);
    }

    /**
     * 导出API 参数详情列表
     */
    @ApiOperation("导出API 参数详情列表")
    @PreAuthorize("@ss.hasPermi('api:detail:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, ApiParamDetail apiParamDetail) {
        List<ApiParamDetail> list = apiParamDetailService.selectApiParamDetailList(apiParamDetail);
        ExcelUtil<ApiParamDetail> util = new ExcelUtil<ApiParamDetail>(ApiParamDetail. class);
        util.exportExcel(response, list, "API 参数详情数据");
    }

    /**
     * 获取API 参数详情详细信息
     */
    @PreAuthorize("@ss.hasPermi('api:detail:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取API 参数详情详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(apiParamDetailService.queryByIdWithCache(id));
    }

    /**
     * 新增API 参数详情
     */
    @PreAuthorize("@ss.hasPermi('api:detail:add')")
    @PostMapping
    @ApiOperation("新增API 参数详情")
    public AjaxResult add(@RequestBody ApiParamDetail apiParamDetail) {
        return toAjax(apiParamDetailService.insertWithCache(apiParamDetail));
    }

    /**
     * 修改API 参数详情
     */
    @PreAuthorize("@ss.hasPermi('api:detail:edit')")
    @PutMapping
    @ApiOperation("修改API 参数详情")
    public AjaxResult edit(@RequestBody ApiParamDetail apiParamDetail) {
        return toAjax(apiParamDetailService.updateWithCache(apiParamDetail));
    }

    /**
     * 删除API 参数详情
     */
    @PreAuthorize("@ss.hasPermi('api:detail:remove')")
    @DeleteMapping("/{ids}")
    @ApiOperation("删除API 参数详情")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(apiParamDetailService.deleteWithCacheByIds(ids, true));
    }
}
