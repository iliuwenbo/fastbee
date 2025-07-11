package com.fastbee.iot.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.fastbee.iot.domain.SceneMiddle;
import com.fastbee.iot.service.ISceneMiddleService;
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
import com.fastbee.common.core.page.TableDataInfo;

/**
 * 场景中包Controller
 *
 * @author lwb
 * @date 2025-05-22
 */
@RestController
@RequestMapping("/platform/middle")
@Api(tags = "场景中包")
public class SceneMiddleController extends BaseController {
    @Autowired
    private ISceneMiddleService sceneMiddleService;

    /**
     * 查询场景中包列表
     */
    @PreAuthorize("@ss.hasPermi('platform:middle:list')")
    @GetMapping("/list")
    @ApiOperation("查询场景中包列表")
    public TableDataInfo list(SceneMiddle sceneMiddle) {
        startPage();
        List<SceneMiddle> list = sceneMiddleService.selectSceneMiddleList(sceneMiddle);
        return getDataTable(list);
    }

    /**
     * 导出场景中包列表
     */
    @ApiOperation("导出场景中包列表")
    @PreAuthorize("@ss.hasPermi('platform:middle:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SceneMiddle sceneMiddle) {
        List<SceneMiddle> list = sceneMiddleService.selectSceneMiddleList(sceneMiddle);
        ExcelUtil<SceneMiddle> util = new ExcelUtil<SceneMiddle>(SceneMiddle. class);
        util.exportExcel(response, list, "场景中包数据");
    }

    /**
     * 获取场景中包详细信息
     */
    @PreAuthorize("@ss.hasPermi('platform:middle:query')")
    @GetMapping(value = "/{middleId}")
    @ApiOperation("获取场景中包详细信息")
    public AjaxResult getInfo(@PathVariable("middleId") Long middleId) {
        return success(sceneMiddleService.queryByIdWithCache(middleId));
    }

    /**
     * 新增场景中包
     */
    @PreAuthorize("@ss.hasPermi('platform:middle:add')")
    @PostMapping
    @ApiOperation("新增场景中包")
    public AjaxResult add(@RequestBody SceneMiddle sceneMiddle) {
        return toAjax(sceneMiddleService.insertWithCache(sceneMiddle));
    }

    /**
     * 修改场景中包
     */
    @PreAuthorize("@ss.hasPermi('platform:middle:edit')")
    @PutMapping
    @ApiOperation("修改场景中包")
    public AjaxResult edit(@RequestBody SceneMiddle sceneMiddle) {
        return toAjax(sceneMiddleService.updateWithCache(sceneMiddle));
    }

    /**
     * 删除场景中包
     */
    @PreAuthorize("@ss.hasPermi('platform:middle:remove')")
    @DeleteMapping("/{middleIds}")
    @ApiOperation("删除场景中包")
    public AjaxResult remove(@PathVariable Long[] middleIds) {
        return toAjax(sceneMiddleService.deleteWithCacheByIds(middleIds, true));
    }
}
