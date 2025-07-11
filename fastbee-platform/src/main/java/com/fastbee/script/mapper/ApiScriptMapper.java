package com.fastbee.script.mapper;

import com.fastbee.iot.domain.Script;
import com.fastbee.iot.model.ScriptCondition;
import com.fastbee.script.domain.ApiScript;
import com.fastbee.script.model.ApiScriptCondition;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 规则引擎脚本Mapper接口
 *
 * @author lizhuangpeng
 * @date 2023-07-01
 */
@Repository
public interface ApiScriptMapper
{
    /**
     * 查询规则引擎脚本
     *
     * @param scriptId 规则引擎脚本主键
     * @return 规则引擎脚本
     */
    public ApiScript selectRuleScriptById(String scriptId);

    /**
     * 查询规则引擎脚本列表
     *
     * @param ruleScript 规则引擎脚本
     * @return 规则引擎脚本集合
     */
    public List<ApiScript> selectRuleScriptList(ApiScript ruleScript);
    public List<ApiScript> selectExecRuleScriptList(ApiScriptCondition ruleScript);

    /**
     * 查询规则引擎脚本标识数组
     *
     * @return 规则引擎脚本数组
     */
    public String[] selectRuleScriptIdArray(ApiScriptCondition scriptCondition);

    /***
     * 查询scriptId的数量
     * @param scriptId
     * @return
     */
    public int selectRuleScriptIdCount(String scriptId);

    /**
     * 新增规则引擎脚本
     *
     * @param ruleScript 规则引擎脚本
     * @return 结果
     */
    public int insertRuleScript(ApiScript ruleScript);

    /**
     * 修改规则引擎脚本
     *
     * @param ruleScript 规则引擎脚本
     * @return 结果
     */
    public int updateRuleScript(ApiScript ruleScript);

    /**
     * 删除规则引擎脚本
     *
     * @param scriptId 规则引擎脚本主键
     * @return 结果
     */
    public int deleteRuleScriptById(String scriptId);

    /**
     * 批量删除规则引擎脚本
     *
     * @param scriptIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRuleScriptByIds(String[] scriptIds);

    /**
     * 批量删除规则脚本
     *
     * @param sceneIds 需要删除的数据场景ID集合
     * @return 结果
     */
    public int deleteRuleScriptBySceneIds(Long[] sceneIds);

    /**
     * 批量新增场景脚本
     *
     * @param ruleScriptList 场景联动触发器集合
     * @return 结果
     */
    public int insertRuleScriptList(List<ApiScript> ruleScriptList);


    public int insertScriptBridge(@Param("scriptId")String scriptId, @Param("bridgeId")Long bridgeId);
    public int updateScriptBridge(@Param("scriptId")String scriptId, @Param("bridgeId")Long bridgeId);
    public int deleteScriptBridge(String scriptId);
}
