package com.fastbee.iot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fastbee.iot.domain.Scene;
import com.fastbee.iot.domain.SceneMiddle;
import com.fastbee.iot.mapper.SceneMapper;
import com.fastbee.iot.util.FlowNodeElDataFiller;
import com.fastbee.iot.util.LiteFlowJsonParser.FlowNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FlowNodeElDataService {

    @Autowired
    private SceneMapper sceneMapper;

    /**
     * 根据 ID 批量加载 Scene 实体
     */
    public Map<Long, Scene> loadSceneMapByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) return Collections.emptyMap();

        // 调试输出收集的ID
        System.out.println("[FlowNodeElDataService] 需要加载的Scene Ids: " + ids);

        LambdaQueryWrapper<Scene> lqw = Wrappers.lambdaQuery();
        lqw.in(Scene::getSceneId, ids);
        List<Scene> scenes = sceneMapper.selectList(lqw);

        // 调试输出加载的Scene个数
        System.out.println("[FlowNodeElDataService] 查询到的Scene数量: " + (scenes == null ? 0 : scenes.size()));

        if (scenes == null || scenes.isEmpty()) {
            return Collections.emptyMap();
        }

        return scenes.stream()
                .collect(Collectors.toMap(Scene::getSceneId, Function.identity(), (a, b) -> a));
    }

    /**
     * 填充 FlowNode 的 elData、name（使用 Scene 数据）
     */
    public void fillFlowNodeData(SceneMiddle middle) {
        if (middle == null || middle.getFlowNode() == null) return;

        FlowNode node = middle.getFlowNode();

        Set<Long> ids = new HashSet<>();
        FlowNodeElDataFiller.collectIdsRecursively(node, ids);

        Long next = ids.iterator().next();
        // 转换为逗号分隔的字符串
        String joinedIds = ids.stream()
                .map(Object::toString) // 将 Long 转换为 String
                .collect(Collectors.joining(",")); // 拼接为逗号分隔的字符串
        middle.setIotSceneId(next); // 获取迭代器的第一个元素
        middle.setIotSceneIds(joinedIds);
        Map<Long, Scene> sceneMap = loadSceneMapByIds(ids);
        FlowNodeElDataFiller.fillDataBySceneMap(Collections.singletonList(node), sceneMap);
    }

    /**
     * 填充 FlowNode 的 elData、name（使用 Scene 数据）
     */
    public void fillFlowNodeData(List<FlowNode> nodes) {
        if (CollectionUtils.isEmpty(nodes)) return;

        Set<Long> ids = new HashSet<>();
        for (FlowNode node : nodes) {
            FlowNodeElDataFiller.collectIdsRecursively(node, ids);
        }

        Map<Long, Scene> sceneMap = loadSceneMapByIds(ids);
        FlowNodeElDataFiller.fillDataBySceneMap(nodes, sceneMap);
    }

    /**
     * 外部调用：填充单个 FlowNode
     */
    public void fillFlowNodeData(FlowNode node) {
        if (node == null) return;

        Set<Long> ids = new HashSet<>();
        FlowNodeElDataFiller.collectIdsRecursively(node, ids);

        Map<Long, Scene> sceneMap = loadSceneMapByIds(ids);
        FlowNodeElDataFiller.fillDataBySceneMap(Collections.singletonList(node), sceneMap);
    }
}
