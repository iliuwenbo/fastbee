package com.fastbee.iot.service.impl;

import com.fastbee.common.core.domain.AjaxResult;
import com.fastbee.common.core.domain.entity.SysRole;
import com.fastbee.common.core.domain.entity.SysUser;
import com.fastbee.common.utils.DateUtils;
import com.fastbee.iot.domain.Category;
import com.fastbee.iot.mapper.CategoryMapper;
import com.fastbee.iot.model.IdAndName;
import com.fastbee.iot.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.fastbee.common.utils.SecurityUtils.getLoginUser;
import static com.fastbee.common.utils.SecurityUtils.isAdmin;

/**
 * 产品分类Service业务层处理
 *
 * @author kerwincui
 * @date 2021-12-16
 */
@Service
public class CategoryServiceImpl implements ICategoryService
{
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 查询产品分类
     *
     * @param categoryId 产品分类主键
     * @return 产品分类
     */
    @Override
    public Category selectCategoryByCategoryId(Long categoryId)
    {
        return categoryMapper.selectCategoryByCategoryId(categoryId);
    }

    /**
     * 查询产品分类列表
     *
     * @param category 产品分类
     * @return 产品分类
     */
    @Override
    public List<Category> selectCategoryList(Category category)
    {
        return categoryMapper.selectCategoryList(category);
    }

    /**
     * 查询产品分简短类列表
     *
     * @return 产品分类
     */
    @Override
    public List<IdAndName> selectCategoryShortList(Category category)
    {
        return categoryMapper.selectCategoryShortList(category);
    }

    /**
     * 新增产品分类
     *
     * @param category 产品分类
     * @return 结果
     */
    @Override
    public int insertCategory(Category category)
    {
        // 判断是否为管理员
        category.setIsSys(1);
        SysUser user = getLoginUser().getUser();
//        List<SysRole> roles=user.getRoles();
//        for(int i=0;i<roles.size();i++){
//            if(roles.get(i).getRoleKey().equals("tenant") || roles.get(i).getRoleKey().equals("general")){
//                category.setIsSys(0);
//                break;
//            }
//        }
        if (null != user.getDeptId()) {
            category.setTenantId(user.getDept().getDeptUserId());
            category.setTenantName(user.getDept().getDeptUserName());
        } else {
            category.setTenantId(user.getUserId());
            category.setTenantName(user.getUserName());
        }
        if (!isAdmin(user.getUserId())) {
            category.setIsSys(0);
        }
        category.setCreateTime(DateUtils.getNowDate());
        return categoryMapper.insertCategory(category);
    }

    /**
     * 修改产品分类
     *
     * @param category 产品分类
     * @return 结果
     */
    @Override
    public int updateCategory(Category category)
    {
        category.setUpdateTime(DateUtils.getNowDate());
        return categoryMapper.updateCategory(category);
    }

    /**
     * 批量删除产品分类
     *
     * @param categoryIds 需要删除的产品分类主键
     * @return 结果
     */
    @Override
    public AjaxResult deleteCategoryByCategoryIds(Long[] categoryIds)
    {
        int productCount=categoryMapper.productCountInCategorys(categoryIds);
        if(productCount>0){
            return AjaxResult.error("删除失败，请先删除对应分类下的产品");
        }
        if(categoryMapper.deleteCategoryByCategoryIds(categoryIds)>0){
            return AjaxResult.success("删除成功");
        }
        return AjaxResult.error("删除失败");
    }

    /**
     * 删除产品分类信息
     *
     * @param categoryId 产品分类主键
     * @return 结果
     */
    @Override
    public int deleteCategoryByCategoryId(Long categoryId)
    {
        return categoryMapper.deleteCategoryByCategoryId(categoryId);
    }
}
