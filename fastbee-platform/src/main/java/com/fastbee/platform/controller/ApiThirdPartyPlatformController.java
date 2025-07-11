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
import com.fastbee.platform.domain.ApiThirdPartyPlatform;
import com.fastbee.platform.service.IApiThirdPartyPlatformService;
import com.fastbee.common.core.page.TableDataInfo;

/**
 * 第三方平台信息Controller
 *
 * @author lwb
 * @date 2025-06-04
 */
@RestController
@RequestMapping("/api/platform")
@Api(tags = "第三方平台信息")
public class ApiThirdPartyPlatformController extends BaseController {
    @Autowired
    private IApiThirdPartyPlatformService apiThirdPartyPlatformService;

    /**
     * 查询第三方平台信息列表
     */
    @PreAuthorize("@ss.hasPermi('platform:platform:list')")
    @GetMapping("/list")
    @ApiOperation("查询第三方平台信息列表")
    public TableDataInfo list(ApiThirdPartyPlatform apiThirdPartyPlatform) {
        startPage();
        List<ApiThirdPartyPlatform> list = apiThirdPartyPlatformService.selectApiThirdPartyPlatformList(apiThirdPartyPlatform);
        return getDataTable(list);
    }

    /**
     * 导出第三方平台信息列表
     */
    @ApiOperation("导出第三方平台信息列表")
    @PreAuthorize("@ss.hasPermi('platform:platform:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, ApiThirdPartyPlatform apiThirdPartyPlatform) {
        List<ApiThirdPartyPlatform> list = apiThirdPartyPlatformService.selectApiThirdPartyPlatformList(apiThirdPartyPlatform);
        ExcelUtil<ApiThirdPartyPlatform> util = new ExcelUtil<ApiThirdPartyPlatform>(ApiThirdPartyPlatform. class);
        util.exportExcel(response, list, "第三方平台信息数据");
    }

    /**
     * 获取第三方平台信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('platform:platform:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取第三方平台信息详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(apiThirdPartyPlatformService.queryByIdWithCache(id));
    }

    /**
     * 新增第三方平台信息
     */
    @PreAuthorize("@ss.hasPermi('platform:platform:add')")
    @PostMapping
    @ApiOperation("新增第三方平台信息")
    public AjaxResult add(@RequestBody ApiThirdPartyPlatform apiThirdPartyPlatform) {
        return toAjax(apiThirdPartyPlatformService.insertWithCache(apiThirdPartyPlatform));
    }

    /**
     * 修改第三方平台信息
     */
    @PreAuthorize("@ss.hasPermi('platform:platform:edit')")
    @PutMapping
    @ApiOperation("修改第三方平台信息")
    public AjaxResult edit(@RequestBody ApiThirdPartyPlatform apiThirdPartyPlatform) {
        return toAjax(apiThirdPartyPlatformService.updateWithCache(apiThirdPartyPlatform));
    }

    /**
     * 删除第三方平台信息
     */
    @PreAuthorize("@ss.hasPermi('platform:platform:remove')")
    @DeleteMapping("/{ids}")
    @ApiOperation("删除第三方平台信息")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(apiThirdPartyPlatformService.deleteWithCacheByIds(ids, true));
    }
}
