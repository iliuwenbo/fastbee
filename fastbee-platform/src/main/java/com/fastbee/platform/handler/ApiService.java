package com.fastbee.platform.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.dtflys.forest.Forest;
import com.dtflys.forest.http.*;
import com.fastbee.common.exception.base.BaseException;
import com.fastbee.platform.vo.ApiRequestDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ApiService {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final XmlMapper xmlMapper = new XmlMapper();

    /**
     * 动态发送 Forest 请求
     *
     * @param apiRequestDetails 入参对象
     * @return 响应内容（JSONObject）
     */
    public JSONObject sendRequest(ApiRequestDetails apiRequestDetails) {
        String url = apiRequestDetails.getApiUrl();
        String method = apiRequestDetails.getMethod();
        String requestFormat = apiRequestDetails.getRequestFormat();
        Map<String, Object> headers = apiRequestDetails.getHeader();
        Map<String, Object> path = apiRequestDetails.getPath();
        Map<String, Object> query = apiRequestDetails.getQuery();
        Map<String,Object> body = apiRequestDetails.getBody();
        Map<String, Object> returnData = apiRequestDetails.getReturnData();
        // 1. 校验请求方法
        ForestRequestType requestType = ForestRequestType.findType(method);
        if (requestType == null) {
            throw new BaseException("不支持的请求方法类型: " + method);
        }

        url = processPathParams(url, path);


        // 2. 构建 ForestRequest
        ForestRequest<?> request = Forest.request()
                .url(url)
                .setType(requestType);

        // 3. 设置请求头
        if (CollUtil.isNotEmpty(headers)) {
            headers.forEach(request::addHeader);
        }

        // 4. 设置查询参数
        if (CollUtil.isNotEmpty(query)) {
            request.addQuery(query);
        }

        // 5. 设置请求体
        if (ObjUtil.isNotEmpty(body)) {
            request.addBody(body);
        }

        // 6. 执行请求并获取响应
        ForestResponse response = request.execute(ForestResponse.class);

        // 7. 处理响应
        if (response.getStatusCode() == -1) {
            throw new BaseException("未接收到服务端的响应信息");
        }

        if (!response.statusOk()) {
            throw new BaseException(StrUtil.format("响应失败，状态码: {}, 原因: {}", response.getStatusCode(), response.getReasonPhrase()));
        }

        // 8. 返回处理后的响应结果
        return processResponse(response,requestFormat,returnData);
    }


    /**
     * 处理 URL 中的路径参数
     *
     * @param url 请求 URL
     * @param params 请求参数
     * @return 替换路径参数后的 URL
     */
    private String processPathParams(String url, Map<String, Object> params) {
        // 使用正则表达式匹配路径中的占位符（如：{id}）
        Pattern pattern = Pattern.compile("\\{(\\w+)\\}");
        Matcher matcher = pattern.matcher(url);

        // 遍历找到的路径参数，替换为 params 中对应的值
        while (matcher.find()) {
            String pathParam = matcher.group(1);
            if (params.containsKey(pathParam)) {
                // 替换路径中的参数
                url = url.replace("{" + pathParam + "}", String.valueOf(params.get(pathParam)));
            } else {
                // 如果没有对应的参数，抛出异常
                throw new BaseException("缺少路径参数: " + pathParam);
            }
        }

        return url;
    }


    /**
     * 将 XML 字符串转换为 JSON
     * @param xml XML 数据
     * @return JSON 节点
     * @throws Exception 转换异常
     */
    private JsonNode xmlToJson(String xml) throws Exception {
        return xmlMapper.readTree(xml);
    }

    /**
     * 处理响应并将其转换为 JSONObject
     * @param response ForestResponse
     * @param requestFormat 请求格式
     * @param returnData 反馈数据（包含返回字段）
     * @return JSONObject
     */
    private JSONObject processResponse(ForestResponse response, String requestFormat, Map<String, Object> returnData) {
        // 获取响应内容
        String responseBody = (String) response.getResult();

        // 判断 Content-Type 来决定如何处理响应
        String contentType = response.getHeaders().get("Content-Type");
        JsonNode jsonNode = null;

        try {
            if (contentType != null && contentType.contains("application/xml")) {
                // 如果是 XML 格式，转换为 JSON
                jsonNode = xmlToJson(responseBody);
            } else if (contentType != null && contentType.contains("application/json")) {
                // 如果是 JSON 格式，直接解析为 JSON
                jsonNode = objectMapper.readTree(responseBody);
            } else {
                throw new BaseException("不支持的响应格式: " + contentType);
            }
            if (CollUtil.isNotEmpty(returnData)) {
                // 动态获取 ALLOWED_FIELDS，这里通过 returnData 中的字段来进行动态处理
                Set<String> allowedFields = getAllowedFieldsFromReturnData(returnData);

                // 过滤多余字段
                filterFields(jsonNode, allowedFields);
            }

            // 将 JsonNode 转换为 JSONObject 并返回
            return new JSONObject(objectMapper.writeValueAsString(jsonNode));

        } catch (Exception e) {
            throw new BaseException("处理响应时出错: " + e.getMessage());
        }
    }

    /**
     * 过滤响应中的字段，仅保留返回数据中指定的字段
     *
     * @param jsonNode 原始 JSON 数据
     * @param allowedFields 允许的字段
     * @return 过滤后的 JSON 数据
     */
    private void filterFields(JsonNode jsonNode, Set<String> allowedFields) {
        if (jsonNode.isObject()) {
            // 遍历并移除不在允许字段集合中的字段
            jsonNode.fieldNames().forEachRemaining(fieldName -> {
                if (!allowedFields.contains(fieldName)) {
                    ((ObjectNode) jsonNode).remove(fieldName);
                }
            });
        }
    }

    /**
     * 从 returnData 获取允许的字段列表
     *
     * @param returnData 返回数据
     * @return 允许的字段集合
     */
    private Set<String> getAllowedFieldsFromReturnData(Map<String, Object> returnData) {
        Set<String> allowedFields = new HashSet<>();
        if (returnData != null && returnData.containsKey("allowedFields")) {
            Object allowedFieldsObj = returnData.get("allowedFields");
            if (allowedFieldsObj instanceof Set) {
                allowedFields = (Set<String>) allowedFieldsObj;
            } else if (allowedFieldsObj instanceof String) {
                // 如果是字符串，按逗号分隔
                String[] fields = ((String) allowedFieldsObj).split(",");
                for (String field : fields) {
                    allowedFields.add(field.trim());
                }
            }
        }
        return allowedFields;
    }
}
