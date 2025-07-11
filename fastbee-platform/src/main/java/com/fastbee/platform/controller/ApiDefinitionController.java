package com.fastbee.platform.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import com.fastbee.common.exception.base.BaseException;
import com.fastbee.common.utils.SecurityUtils;
import com.fastbee.iot.domain.Bridge;
import com.fastbee.iot.domain.Device;
import com.fastbee.iot.domain.Product;
import com.fastbee.iot.domain.ThingsModel;
import com.fastbee.iot.service.IProductService;
import com.fastbee.iot.service.IThingsModelService;
import com.fastbee.platform.domain.ApiDevice;
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
import com.fastbee.platform.domain.ApiDefinition;
import com.fastbee.platform.service.IApiDefinitionService;
import com.fastbee.common.core.page.TableDataInfo;

/**
 * 接口定义Controller
 *
 * @author lwb
 * @date 2025-04-27
 */
@RestController
@RequestMapping("/api/definition")
@Api(tags = "API 接口定义")
public class ApiDefinitionController extends BaseController {
    @Autowired
    private IApiDefinitionService apiDefinitionService;
    @Autowired
    private IThingsModelService thingsModelService;

    /**
     * 查询接口定义列表
     */
    @PreAuthorize("@ss.hasPermi('api:definition:list')")
    @GetMapping("/list")
    @ApiOperation("查询接口定义列表")
    public TableDataInfo list(ApiDefinition apiDefinition) {
        startPage();
        List<ApiDefinition> list = apiDefinitionService.selectApiDefinitionList(apiDefinition);
        return getDataTable(list);
    }

    /**
     * 导出接口定义列表
     */
    @ApiOperation("导出接口定义列表")
    @PreAuthorize("@ss.hasPermi('api:definition:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, ApiDefinition apiDefinition) {
        List<ApiDefinition> list = apiDefinitionService.selectApiDefinitionList(apiDefinition);
        ExcelUtil<ApiDefinition> util = new ExcelUtil<ApiDefinition>(ApiDefinition. class);
        util.exportExcel(response, list, "接口定义数据");
    }

    /**
     * 获取接口定义详细信息
     */
    @PreAuthorize("@ss.hasPermi('api:definition:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取接口定义详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(apiDefinitionService.queryByIdWithCache(id));
    }

    /**
     * 新增接口定义
     */
    @PreAuthorize("@ss.hasPermi('api:definition:add')")
    @PostMapping
    @ApiOperation("新增接口定义")
    public AjaxResult add(@RequestBody ApiDefinition apiDefinition) {
        return toAjax(apiDefinitionService.insertWithCache(apiDefinition));
    }


    /**
     * 新增接口定义
     */
    @PreAuthorize("@ss.hasPermi('api:definition:add')")
    @PostMapping("/add")
    @ApiOperation("新增接口定义")
    public AjaxResult adds(@RequestBody List<ApiDefinition> apiDefinitions) {
        return toAjax(apiDefinitionService.insertWithCache(apiDefinitions));
    }

    /**
     * 修改接口定义
     */
    @PreAuthorize("@ss.hasPermi('api:definition:edit')")
    @PutMapping
    @ApiOperation("修改接口定义")
    public AjaxResult edit(@RequestBody ApiDefinition apiDefinition) {
        return toAjax(apiDefinitionService.updateWithCache(apiDefinition));
    }

    /**
     * 删除接口定义
     */
    @PreAuthorize("@ss.hasPermi('api:definition:remove')")
    @DeleteMapping("/{ids}")
    @ApiOperation("删除接口定义")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(apiDefinitionService.deleteWithCacheByIds(ids, true));
    }



    /**
     * 关联模型
     */
    @PreAuthorize("@ss.hasPermi('iot:device:remove')")
    @PutMapping("/bindList/{id}")
    @ApiOperation("关联模型")
    public AjaxResult bindList(@PathVariable("id") Long modelId,Long apiDefinitionId) {
        ThingsModel thingsModel = thingsModelService.selectThingsModelByModelId(modelId);
        thingsModel.setApiDefinitionId(apiDefinitionId);
        return toAjax(thingsModelService.updateThingsModel(thingsModel));
    }



    /**
     * 关联规则引擎
     */
    @PreAuthorize("@ss.hasPermi('iot:device:remove')")
    @PutMapping("/bind/ApiScript/{id}")
    @ApiOperation("关联规则引擎")
    public AjaxResult bindApiScript(@PathVariable("id") Long apiDefinitionId,String apiScriptId) {
        ApiDefinition apiDefinition = new ApiDefinition();
        apiDefinition.setId(apiDefinitionId);
        apiDefinition.setApiScriptId(apiScriptId);
        return toAjax(apiDefinitionService.updateById(apiDefinition));
    }

    /**
     * 接口关联脚本
     */
    @PreAuthorize("@ss.hasPermi('iot:device:remove')")
    @PutMapping("/bind/ApiScript2/{id}")
    @ApiOperation("接口关联脚本")
    public AjaxResult bindApiScript2(@PathVariable("id") Long apiDefinitionId,String apiScriptId) {
        ApiDefinition apiDefinition = new ApiDefinition();
        apiDefinition.setId(apiDefinitionId);
        apiDefinition.setApiScriptId(apiScriptId);
        return toAjax(apiDefinitionService.updateById(apiDefinition));
    }


    /**
     * 请求接口
     * @param id 接口id
     * @return 返回设备列表
     */
    @PostMapping("/connect")
    @ApiOperation("请求接口")
    public Object connect(Long id)
    {
        return apiDefinitionService.connect(id);
    }


    /**
     * 请求接口并返回设备列表
     * @param id 接口id
     * @return 返回设备列表
     */
    @PostMapping("/connect/device")
    @ApiOperation("请求接口并返回设备列表")
    public AjaxResult connectDevice(Long id)
    {
        List<ApiDevice> connect = apiDefinitionService.connectDevice(id);
        return toAjax(connect);
    }

}
