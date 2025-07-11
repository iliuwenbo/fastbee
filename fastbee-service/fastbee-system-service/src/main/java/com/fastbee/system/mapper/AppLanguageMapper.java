package com.fastbee.system.mapper;


import com.fastbee.system.domain.AppLanguage;

import java.util.List;

/**
 * app语言Mapper接口
 */
public interface AppLanguageMapper
{
    /**
     * 查询app语言
     *
     * @param id app语言主键
     * @return app语言
     */
    public AppLanguage selectAppLanguageById(Long id);

    /**
     * 查询app语言列表
     *
     * @param appLanguage app语言
     * @return app语言集合
     */
    public List<AppLanguage> selectAppLanguageList(AppLanguage appLanguage);

    /**
     * 新增app语言
     *
     * @param appLanguage app语言
     * @return 结果
     */
    public int insertAppLanguage(AppLanguage appLanguage);

    /**
     * 修改app语言
     *
     * @param appLanguage app语言
     * @return 结果
     */
    public int updateAppLanguage(AppLanguage appLanguage);

    /**
     * 删除app语言
     *
     * @param id app语言主键
     * @return 结果
     */
    public int deleteAppLanguageById(Long id);

    /**
     * 批量删除app语言
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAppLanguageByIds(Long[] ids);
}
