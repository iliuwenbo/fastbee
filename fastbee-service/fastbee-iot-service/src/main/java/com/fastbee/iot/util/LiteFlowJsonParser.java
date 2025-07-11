package com.fastbee.iot.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * LiteFlow EL 规则解析工具类
 * 支持将流程 JSON 字符串解析为 EL 表达式字符串
 */
public class LiteFlowJsonParser {

    /**
     * 入口方法：根据 JSON 字符串解析为 EL 表达式
     * @param json 流程节点 JSON 字符串
     * @return EL 表达式字符串
     */
    public static String parseNode(String json) {
        if (StrUtil.isBlank(json)) {
            return "";
        }
        FlowNode rootNode = JSONUtil.toBean(json, FlowNode.class);
        return parseNodeRecursively(rootNode);
    }

    /**
     * 入口方法：根据流程节点对象解析为 EL 表达式
     * @param rootNode 流程节点对象
     * @return EL 表达式字符串
     */
    public static String parseNode(FlowNode rootNode) {
        System.out.println("rootNode = " + JSONUtil.toJsonStr(rootNode));
        String s = parseNodeRecursively(rootNode);
        System.out.println("s = " + s);
        return s;
    }

    /**
     * 递归解析流程节点，构造 EL 表达式
     * @param node 当前节点
     * @return 对应 EL 表达式
     */
    private static String parseNodeRecursively(FlowNode node) {
        if (node == null) {
            return "";
        }

        // 如果是 type=0 的特殊节点，跳过自身 EL，只处理条件分支和子节点
        if (Integer.valueOf(0).equals(node.getType())) {
            String branchesEl = parseConditionNodes(node);
            String childEl = parseNodeRecursively(node.getChildNode());
            return mergeExpressions(branchesEl, childEl);
        }

        // 普通节点：先清理自身 EL 前缀
        String selfEl = cleanElPrefix(node.getElData());

        // 递归解析条件分支（conditionNodes）和子节点（childNode）
        String branchesEl = parseConditionNodes(node);
        String childEl = parseNodeRecursively(node.getChildNode());

        // 先合并条件分支和子节点，再和自身 EL 合并
        String branchesAndChildEl = mergeExpressions(branchesEl, childEl);
        return mergeExpressions(selfEl, branchesAndChildEl);
    }

    /**
     * 解析 conditionNodes 条件分支列表，合并成一个 EL 表达式
     * @param node 当前节点
     * @return 条件分支 EL 表达式，格式为 WHEN(...) 或 THEN(...)
     */
    private static String parseConditionNodes(FlowNode node) {
        List<FlowNode> conditionNodes = node.getConditionNodes();
        if (conditionNodes == null || conditionNodes.isEmpty()) {
            return "";
        }

        // 节点类型为 52 表示并行分支，使用 WHEN 连接，其他情况用 THEN 连接
        String connector = Integer.valueOf(52).equals(node.getType()) ? "WHEN" : "THEN";

        List<String> branchExpressions = new ArrayList<>();
        for (FlowNode branchNode : conditionNodes) {
            String branchEl = cleanElPrefix(branchNode.getElData());

            // 分支节点的子节点也递归解析
            String childEl = parseNodeRecursively(branchNode.getChildNode());

            // 合并当前分支节点 EL 和其子节点 EL
            String combined = mergeExpressions(branchEl, childEl);
            branchExpressions.add(combined);
        }

        return String.format("%s(%s)", connector, String.join(",", branchExpressions));
    }

    /**
     * 清理 EL 表达式字符串的外层包装，如 EL(...)，只保留括号内内容
     * @param el EL 表达式字符串
     * @return 纯 EL 内容
     */
    private static String cleanElPrefix(String el) {
        if (StrUtil.isBlank(el)) {
            return "";
        }
        return el.replaceAll("^EL\\((.+)\\)$", "$1");
    }

    /**
     * 判断给定的 EL 表达式是否是复杂表达式（以 IF/WHEN/THEN 开头）
     * @param el EL 表达式
     * @return true 表示是复杂表达式
     */
    private static boolean isCompoundEl(String el) {
        return el != null && (el.startsWith("IF(") || el.startsWith("WHEN(") || el.startsWith("THEN("));
    }

    /**
     * 合并父 EL 表达式和子 EL 表达式
     * 规则：
     * - 如果父表达式是 IF，则把子表达式追加到 THEN 部分
     * - 如果父表达式是 WHEN 或 THEN，则把子表达式追加到括号内
     * - 否则用 THEN(父,子) 包裹
     * @param parentEl 父 EL 表达式
     * @param childEl 子 EL 表达式
     * @return 合并后的 EL 表达式
     */
    private static String mergeExpressions(String parentEl, String childEl) {
        if (StrUtil.isBlank(parentEl)) {
            return childEl;
        }
        if (StrUtil.isBlank(childEl)) {
            return parentEl;
        }

        if (isCompoundEl(parentEl)) {
            if (parentEl.startsWith("IF(")) {
                // 找到 THEN(...) 部分，追加子表达式
                int thenIndex = parentEl.indexOf("THEN(");
                if (thenIndex != -1) {
                    int openParenIndex = thenIndex + "THEN".length();
                    int closeParenIndex = findMatchingParenthesis(parentEl, openParenIndex);
                    if (closeParenIndex != -1) {
                        String existingThenContent = parentEl.substring(openParenIndex + 1, closeParenIndex);
                        String mergedThenContent = existingThenContent + "," + childEl;
                        return parentEl.substring(0, openParenIndex + 1) + mergedThenContent + parentEl.substring(closeParenIndex);
                    }
                }
            } else {
                // WHEN(...) 或 THEN(...) 直接追加子表达式
                int openParenIndex = parentEl.indexOf('(');
                int closeParenIndex = findMatchingParenthesis(parentEl, openParenIndex);
                if (openParenIndex != -1 && closeParenIndex != -1) {
                    String innerContent = parentEl.substring(openParenIndex + 1, closeParenIndex);
                    String mergedContent = innerContent + "," + childEl;
                    return parentEl.substring(0, openParenIndex + 1) + mergedContent + parentEl.substring(closeParenIndex);
                }
            }
        }

        // 普通情况：用 THEN(父表达式, 子表达式) 包裹
        return String.format("THEN(%s,%s)", parentEl, childEl);
    }

    /**
     * 找到字符串中从 openIndex 位置开始的括号匹配位置
     * @param str 字符串
     * @param openIndex '(' 的位置
     * @return 对应 ')' 的位置，未找到返回 -1
     */
    private static int findMatchingParenthesis(String str, int openIndex) {
        if (str == null || openIndex < 0 || openIndex >= str.length() || str.charAt(openIndex) != '(') {
            return -1;
        }
        int count = 0;
        for (int i = openIndex; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '(') count++;
            else if (c == ')') count--;

            if (count == 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 流程节点实体类
     */
    @Data
    public static class FlowNode {
        private Long id;
        private String name;
        private Integer type;
        private String elData;
        private FlowNode childNode;
        private List<FlowNode> conditionNodes;
    }

    public static void main(String[] args) {
        String json = " {\"id\":0,\"name\":\"开始\",\"type\":0,\"childNode\":{\"id\":23,\"name\":\"小包-触发器\",\"type\":11,\"elData\":\"IF(T1927621784543301632,THEN(A1927621784543301633))\",\"childNode\":{\"name\":\"\",\"type\":52,\"childNode\":{\"id\":0,\"name\":\"结束\",\"type\":0},\"conditionNodes\":[{\"id\":13,\"name\":\"ESP8266告警通知\",\"type\":50,\"elData\":\"IF(T1925744575209672704,THEN(A1925744578871300096,A1925744578871300097))\"},{\"id\":12,\"name\":\"海康开放平台产品事件报警\",\"type\":50,\"elData\":\"IF(T1925467736402694144,THEN(A1925467736402694145))\"}]}}}\n";
        String s = parseNode(json);
        System.out.println(s);
        json = "{\"elementId\":\"1\",\"name\":\"开始\",\"id\":0,\"type\":0,\"childNode\":{\"elementId\":\"mbamcxila4bf5\",\"name\":\"小包-触发器\",\"type\":\"11\",\"childNode\":{\"elementId\":\"mbamd1ol6yb4y\",\"name\":\"ESP8266告警通知\",\"type\":\"11\",\"childNode\":{\"elementId\":\"mbamcmp48y080\",\"name\":\"结束\",\"type\":0,\"id\":0,\"childNode\":null},\"id\":13},\"id\":23}}\n";
        s = parseNode(json);
        System.out.println(s);
    }
}
