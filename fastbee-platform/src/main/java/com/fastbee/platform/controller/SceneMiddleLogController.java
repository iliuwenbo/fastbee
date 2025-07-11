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
import com.fastbee.platform.domain.SceneMiddleLog;
import com.fastbee.platform.service.ISceneMiddleLogService;
import com.fastbee.common.core.page.TableDataInfo;

/**
 * 中包执行日志Controller
 *
 * @author lwb
 * @date 2025-06-04
 */
@RestController
@RequestMapping("/platform/log")
@Api(tags = "中包执行日志")
public class SceneMiddleLogController extends BaseController {
    @Autowired
    private ISceneMiddleLogService sceneMiddleLogService;

    /**
     * 查询中包执行日志列表
     */
    @PreAuthorize("@ss.hasPermi('platform:log:list')")
    @GetMapping("/list")
    @ApiOperation("查询中包执行日志列表")
    public TableDataInfo list(SceneMiddleLog sceneMiddleLog) {
        startPage();
        List<SceneMiddleLog> list = sceneMiddleLogService.selectSceneMiddleLogList(sceneMiddleLog);
        return getDataTable(list);
    }

    /**
     * 导出中包执行日志列表
     */
    @ApiOperation("导出中包执行日志列表")
    @PreAuthorize("@ss.hasPermi('platform:log:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SceneMiddleLog sceneMiddleLog) {
        List<SceneMiddleLog> list = sceneMiddleLogService.selectSceneMiddleLogList(sceneMiddleLog);
        ExcelUtil<SceneMiddleLog> util = new ExcelUtil<SceneMiddleLog>(SceneMiddleLog. class);
        util.exportExcel(response, list, "中包执行日志数据");
    }

    /**
     * 获取中包执行日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('platform:log:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取中包执行日志详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(sceneMiddleLogService.queryByIdWithCache(id));
    }

    /**
     * 新增中包执行日志
     */
    @PreAuthorize("@ss.hasPermi('platform:log:add')")
    @PostMapping
    @ApiOperation("新增中包执行日志")
    public AjaxResult add(@RequestBody SceneMiddleLog sceneMiddleLog) {
        return toAjax(sceneMiddleLogService.insertWithCache(sceneMiddleLog));
    }

    /**
     * 修改中包执行日志
     */
    @PreAuthorize("@ss.hasPermi('platform:log:edit')")
    @PutMapping
    @ApiOperation("修改中包执行日志")
    public AjaxResult edit(@RequestBody SceneMiddleLog sceneMiddleLog) {
        return toAjax(sceneMiddleLogService.updateWithCache(sceneMiddleLog));
    }

    /**
     * 删除中包执行日志
     */
    @PreAuthorize("@ss.hasPermi('platform:log:remove')")
    @DeleteMapping("/{ids}")
    @ApiOperation("删除中包执行日志")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sceneMiddleLogService.deleteWithCacheByIds(ids, true));
    }
}
