package com.fastbee.script.controller;

import com.fastbee.common.annotation.Log;
import com.fastbee.common.core.controller.BaseController;
import com.fastbee.common.core.domain.AjaxResult;
import com.fastbee.common.core.domain.entity.SysUser;
import com.fastbee.common.core.page.TableDataInfo;
import com.fastbee.common.enums.BusinessType;
import com.fastbee.script.domain.ApiScript;
import com.fastbee.script.service.IApiScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 规则引擎脚本Controller
 *
 * @author lizhuangpeng
 * @date 2023-07-01
 */
@RestController
@RequestMapping("/api/script")
public class ApiScriptController extends BaseController
{
    @Autowired
    private IApiScriptService apiScriptService;

    /**
     * 查询规则引擎脚本列表
     */
    @PreAuthorize("@ss.hasPermi('iot:script:list')")
    @GetMapping("/list")
    public TableDataInfo list(ApiScript ruleScript)
    {
//        SysUser sysUser = getLoginUser().getUser();
//        List<SysRole> roles = sysUser.getRoles();
//        for (SysRole role : roles) {
//            if (role.getRoleKey().equals("general") || role.getRoleKey().equals("tenant")) {
//                // 用户和租户只能查看自己的规则脚本
//                ruleScript.setUserId(sysUser.getUserId());
//                break;
//            }
//        }
        // 只能查看所属机构
//        if (null != sysUser.getDeptId()) {
//            ruleScript.setUserId(sysUser.getDept().getDeptUserId());
//        } else {
//            ruleScript.setUserId(sysUser.getUserId());
//        }
        startPage();
        List<ApiScript> list = apiScriptService.selectRuleScriptList(ruleScript);
        return getDataTable(list);
    }

    /**
     * 获取规则引擎脚本详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:script:query')")
    @GetMapping(value = "/{scriptId}")
    public AjaxResult getInfo(@PathVariable("scriptId") String scriptId)
    {
        return success(apiScriptService.selectRuleScriptById(scriptId));
    }

    /**
     * 新增规则引擎脚本
     */
    @PreAuthorize("@ss.hasPermi('iot:script:add')")
    @Log(title = "规则引擎脚本", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ApiScript ruleScript)
    {
        return toAjax(apiScriptService.insertRuleScript(ruleScript));
    }

    /**
     * 修改规则引擎脚本
     */
    @PreAuthorize("@ss.hasPermi('iot:script:edit')")
    @Log(title = "规则引擎脚本", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ApiScript ruleScript)
    {
        return toAjax(apiScriptService.updateRuleScript(ruleScript));
    }

    /**
     * 获取规则引擎脚本日志
     */
    @PreAuthorize("@ss.hasPermi('iot:script:query')")
    @GetMapping(value = "/log/{scriptId}")
    public AjaxResult getScriptLog(@PathVariable("scriptId") String scriptId)
    {
        return success(apiScriptService.selectRuleScriptLog(scriptId));
    }

    @PreAuthorize("@ss.hasPermi('iot:script:query')")
    @GetMapping(value = "/log/open/{scriptId}")
    public AjaxResult openPublishLog(@PathVariable("scriptId") String scriptId)
    {
        apiScriptService.openScriptLog(scriptId);
        return success();
    }

    @PreAuthorize("@ss.hasPermi('iot:script:query')")
    @GetMapping(value = "/log/close/{scriptId}")
    public AjaxResult closePublishLog(@PathVariable("scriptId") String scriptId)
    {
        apiScriptService.closeScriptLog(scriptId);
        return success();
    }

    /**
     * 删除规则引擎脚本
     */
    @PreAuthorize("@ss.hasPermi('iot:script:remove')")
    @Log(title = "规则引擎脚本", businessType = BusinessType.DELETE)
    @DeleteMapping("/{scriptIds}")
    public AjaxResult remove(@PathVariable String[] scriptIds)
    {
        return toAjax(apiScriptService.deleteRuleScriptByIds(scriptIds));
    }

    /**
     * 删除规则引擎脚本
     */
    @PostMapping("/validate")
    public AjaxResult validateScript(@RequestBody ApiScript ruleScript)
    {
        return apiScriptService.validateScript(ruleScript);
    }
}
