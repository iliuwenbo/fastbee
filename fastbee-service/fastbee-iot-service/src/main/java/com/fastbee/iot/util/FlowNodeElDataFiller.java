package com.fastbee.iot.util;

import com.fastbee.iot.domain.Scene;
import com.fastbee.iot.util.LiteFlowJsonParser.FlowNode;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FlowNodeElDataFiller {

    /**
     * 递归填充 FlowNode 的 elData 和 name（包含子节点和分支节点）
     */
    public static void fillDataBySceneMap(List<FlowNode> nodes,
                                          Map<Long, Scene> sceneMap) {
        if (nodes == null || nodes.isEmpty() || sceneMap == null || sceneMap.isEmpty()) return;

        for (FlowNode node : nodes) {
            fillNodeRecursively(node, sceneMap);
        }
    }

    /**
     * 递归收集所有有效节点 id（用于前置加载 scene 数据）
     * 过滤 null 和 0 的id
     */
    public static void collectIdsRecursively(FlowNode node, Set<Long> idSet) {
        if (node == null) return;

        Long nodeId = node.getId();
        if (nodeId != null && nodeId != 0L) {
            idSet.add(nodeId);
        }

        // 递归收集 childNode
        if (node.getChildNode() != null) {
            collectIdsRecursively(node.getChildNode(), idSet);
        }

        // 递归收集 conditionNodes
        if (node.getConditionNodes() != null) {
            for (FlowNode cond : node.getConditionNodes()) {
                collectIdsRecursively(cond, idSet);
            }
        }
    }

    /**
     * 递归填充单个节点
     * 跳过无效ID节点
     */
    private static void fillNodeRecursively(FlowNode node,
                                            Map<Long, Scene> sceneMap) {
        if (node == null) return;

        Long nodeId = node.getId();
        if (nodeId != null && nodeId != 0L) {
            Scene scene = sceneMap.get(nodeId);
            if (scene != null) {
                node.setElData(scene.getElData());
                node.setName(scene.getSceneName());
            } else {
                // 调试输出：没有找到对应scene
                System.out.println("[FlowNodeElDataFiller] 未找到对应Scene，nodeId=" + nodeId);
            }
        } else {
            // 调试输出：节点ID无效
            System.out.println("[FlowNodeElDataFiller] 节点ID为空或0，elementId=");
        }

        // 递归填充 childNode
        if (node.getChildNode() != null) {
            fillNodeRecursively(node.getChildNode(), sceneMap);
        }

        // 递归填充 conditionNodes
        if (node.getConditionNodes() != null) {
            for (FlowNode cond : node.getConditionNodes()) {
                fillNodeRecursively(cond, sceneMap);
            }
        }
    }
}
