package com.fastbee.web.controller.system;

import java.util.List;

import com.fastbee.common.utils.MessageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fastbee.common.annotation.Log;
import com.fastbee.common.constant.UserConstants;
import com.fastbee.common.core.controller.BaseController;
import com.fastbee.common.core.domain.AjaxResult;
import com.fastbee.common.core.domain.entity.SysMenu;
import com.fastbee.common.enums.BusinessType;
import com.fastbee.common.utils.StringUtils;
import com.fastbee.system.service.ISysMenuService;

/**
 * 菜单信息
 *
 * @author ruoyi
 */
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController
{
    @Autowired
    private ISysMenuService menuService;

    /**
     * 获取菜单列表
     */
    @ApiOperation("获取菜单列表")
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("/list")
    public AjaxResult list(SysMenu menu)
    {
        List<SysMenu> menus = menuService.selectMenuList(menu, getUserId());
        return success(menus);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @ApiOperation("根据菜单编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @GetMapping(value = "/{menuId}")
    public AjaxResult getInfo(@PathVariable Long menuId)
    {
        return success(menuService.selectMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     */
    @ApiOperation("获取菜单下拉树列表")
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysMenu menu)
    {
        List<SysMenu> menus = menuService.selectMenuList(menu, getUserId());
        return success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @ApiOperation("加载对应角色菜单列表树")
    @GetMapping(value = "/roleMenuTreeselect")
    public AjaxResult roleMenuTreeselect(@RequestParam Long roleId, @RequestParam Long deptId)
    {
        List<SysMenu> menus = menuService.deptRoleMenuTreeselect(deptId, roleId);
//        List<SysMenu> menus = menuService.selectMenuList(getUserId(), request.getHeader(LANGUAGE));
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return ajax;
    }

    /**
     * 新增菜单
     */
    @ApiOperation("新增菜单")
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysMenu menu)
    {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu)))
        {
            return error(StringUtils.format(MessageUtils.message("menu.add.failed.name.exists"), menu.getMenuName()));
        }
        else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath()))
        {
            return error(StringUtils.format(MessageUtils.message("menu.add.failed.path.not.valid"), menu.getMenuName()));
        }
        menu.setCreateBy(getUsername());
        return toAjax(menuService.insertMenu(menu));
    }

    /**
     * 修改菜单
     */
    @ApiOperation("修改菜单")
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysMenu menu)
    {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu)))
        {
            return error(StringUtils.format(MessageUtils.message("menu.update.failed.name.exists"), menu.getMenuName()));
        }
        else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath()))
        {
            return error(StringUtils.format(MessageUtils.message("menu.update.failed.path.not.valid"), menu.getMenuName()));
        }
        else if (menu.getMenuId().equals(menu.getParentId()))
        {
            return error(StringUtils.format(MessageUtils.message("menu.update.failed.parent.not.valid"), menu.getMenuName()));
        }
        menu.setUpdateBy(getUsername());
        return toAjax(menuService.updateMenu(menu));
    }

    /**
     * 删除菜单
     */
    @ApiOperation("删除菜单")
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public AjaxResult remove(@PathVariable("menuId") Long menuId)
    {
        if (menuService.hasChildByMenuId(menuId))
        {
            return warn(MessageUtils.message("menu.delete.failed.child.exists"));
        }
        if (menuService.checkMenuExistRole(menuId))
        {
            return warn(MessageUtils.message("menu.delete.failed.role.exists"));
        }
        return toAjax(menuService.deleteMenuById(menuId));
    }

    /**
     * 加载对应部门菜单列表树
     */
    @ApiOperation("加载对应部门菜单列表树")
    @GetMapping(value = "/deptMenuTreeselect/{deptId}")
    public AjaxResult deptMenuTreeselect(@PathVariable("deptId") Long deptId)
    {
        List<SysMenu> menus = menuService.deptMenuTreeselect(deptId);
        return success(menuService.buildMenuTreeSelect(menus));
    }
}
