package com.fastbee.platform.utils;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Base64;

import cn.hutool.core.collection.ListUtil;
import com.fastbee.platform.vo.ApiRequestDetails;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Base64;
import java.util.stream.Collectors;

public class HikvisionEncryptionUtil{

    /**
     * 核心方法：生成带签名的请求详情对象
     * @param requestDetails 原始请求详情（需包含path、query、header、body等参数）
     * @param appKey         应用Key（AK）
     * @param appSecret      应用密钥（SK）
     * @return 自动填充签名和系统级Header的请求详情对象
     */
    public static ApiRequestDetails processWithSignature(ApiRequestDetails requestDetails, String appKey, String appSecret)
            throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {

        // 1. 初始化必填参数
        Map<String, Object> headers = initHeaders(requestDetails, appKey);
        String httpMethod = requestDetails.getMethod().toUpperCase();
        String path = requestDetails.getApiUrl().replaceFirst("^https?://[^/]+", ""); // 提取URI路径
        Map<String, String> queryParams = convertToMap(requestDetails.getQuery());
        Map<String, String> bodyFormParams = convertToMap(requestDetails.getBody());

        // 2. 生成Content-MD5（仅当Body非Form表单时计算，此处假设body为JSON字符串）
        String contentMd5 = "";
        if (requestDetails.getRequestFormat() != null && !"form".equals(requestDetails.getRequestFormat())) {
            String bodyJson = requestDetails.getBody() != null ? requestDetails.getBody().toString() : "";
            contentMd5 = calculateContentMD5(bodyJson);
            headers.put("Content-MD5", contentMd5);
        }

        // 3. 构建签名字符串
        String stringToSign = buildStringToSign(httpMethod, headers, path, queryParams, bodyFormParams);

        // 4. 生成签名
        String signature = generateHmacSha256Signature(stringToSign, appSecret);

        // 5. 装配系统级Header
        headers.put("X-Ca-Key", appKey);
        headers.put("X-Ca-Signature", signature);
        headers.put("X-Ca-Signature-Headers", generateSignatureHeaders(headers));

        // 6. 更新请求详情对象（自动处理URL和Header）
        requestDetails.setApiUrl(buildFullUrl(path, queryParams, bodyFormParams));
        requestDetails.setHeader(headers);
        return requestDetails;
    }

    // ======================== 内部工具方法 ========================

    /**
     * 初始化基础请求头
     */
    private static Map<String, Object> initHeaders(ApiRequestDetails requestDetails, String appKey) {
        Map<String, Object> headers = new LinkedHashMap<>();
        headers.put("Accept", requestDetails.getHeader() != null ?
                requestDetails.getHeader().get("Accept").toString() : "*/*");
        headers.put("Content-Type", requestDetails.getHeader() != null ?
                requestDetails.getHeader().get("Content-Type").toString() : "application/json;charset=UTF-8");
        headers.put("Date", new Date().toGMTString()); // 自动填充当前时间
        headers.put("X-Ca-Timestamp", String.valueOf(System.currentTimeMillis())); // 自动填充时间戳
        return headers;
    }

    /**
     * 转换Map参数（处理Object类型为String）
     */
    private static Map<String, String> convertToMap(Map<String, Object> source) {
        if (source == null) return new HashMap<>();
        Map<String, String> result = new HashMap<>();
        source.forEach((k, v) -> result.put(k, v != null ? v.toString() : ""));
        return result;
    }

    /**
     * 构建完整URL（含查询参数和Form参数）
     */
    private static String buildFullUrl(String path, Map<String, String> queryParams, Map<String, String> bodyFormParams) {
        Map<String, String> allParams = new TreeMap<>(queryParams);
        allParams.putAll(bodyFormParams);

        if (allParams.isEmpty()) return "https://" + path; // 假设域名已在apiUrl中处理

        StringBuilder paramBuilder = new StringBuilder();
        allParams.forEach((k, v) -> {
            try {
                paramBuilder.append("&").append(k).append("=").append(urlEncode(v));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        });
        return "https://" + path + "?" + paramBuilder.substring(1); // 拼接完整URL
    }

    /**
     * 计算Content-MD5（MD5+Base64）
     */
    private static String calculateContentMD5(String body) throws NoSuchAlgorithmException {
        if (body == null || body.isEmpty()) return "";
        byte[] md5Bytes = java.security.MessageDigest.getInstance("MD5").digest(body.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(md5Bytes);
    }

    /**
     * 构建签名字符串（完全遵循海康规则）
     */
    private static String buildStringToSign(String httpMethod, Map<String, Object> headers,
                                            String path, Map<String, String> queryParams, Map<String, String> bodyFormParams) {
        StringBuilder sb = new StringBuilder(httpMethod).append("\n");

        // 添加特殊Header（Accept/Content-MD5/Content-Type/Date）
        addSpecialHeader(sb, headers, "Accept");
        addSpecialHeader(sb, headers, "Content-MD5");
        addSpecialHeader(sb, headers, "Content-Type");
        addSpecialHeader(sb, headers, "Date");

        // 添加参与签名的普通Header（排除系统级Header）
        List<String> signHeaders = getSignableHeaders(headers);
        signHeaders.forEach(key -> sb.append(key).append(":").append(headers.get(key)).append("\n"));

        // 添加URL参数（Path+Query+BodyForm）
        sb.append(buildUrlPath(path, queryParams, bodyFormParams));
        return sb.toString();
    }

    /**
     * 添加特殊Header（处理空值和换行符）
     */
    private static void addSpecialHeader(StringBuilder sb, Map<String, Object> headers, String headerName) {
        Object value = headers.getOrDefault(headerName, "");
        if (headers.containsKey(headerName)) { // 存在则添加值+换行符，空值保留换行符
            sb.append(value).append("\n");
        }
    }

    /**
     * 获取参与签名的Header列表（排除系统级和不参与签名的Header）
     */
    private static List<String> getSignableHeaders(Map<String, Object> headers) {
        List<String> signKeys = new ArrayList<>();
        headers.forEach((k, v) -> {
            String lowerKey = k.toLowerCase();
            if (!isExcludedHeader(lowerKey) && !lowerKey.startsWith("x-ca-signature")) {
                signKeys.add(lowerKey);
            }
        });
        signKeys.sort(String::compareTo); // 字典排序
        return signKeys;
    }

    /**
     * 排除不参与签名的Header
     */
    private static boolean isExcludedHeader(String key) {
        return ListUtil.of("accept", "content-md5", "content-type", "date", "content-length", "host", "connection")
                .contains(key);
    }

    /**
     * 构建URL参数部分（仅Path+Query+BodyForm，不包含域名）
     */
    private static String buildUrlPath(String path, Map<String, String> queryParams, Map<String, String> bodyFormParams) {
        Map<String, String> mergedParams = new TreeMap<>();
        mergedParams.putAll(queryParams);
        mergedParams.putAll(bodyFormParams);

        if (mergedParams.isEmpty()) return path;

        StringBuilder paramBuilder = new StringBuilder();
        mergedParams.forEach((k, v) -> {
            if (v != null && !v.isEmpty()) {
                try {
                    paramBuilder.append("&").append(k).append("=").append(urlEncode(v));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            } else {
                paramBuilder.append("&").append(k); // 值为空时仅保留键
            }
        });
        return path + "?" + paramBuilder.substring(1); // 去除首个&
    }

    /**
     * URL编码（符合RFC 3986规范）
     */
    private static String urlEncode(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, "UTF-8")
                .replaceAll("\\+", "%20")
                .replaceAll("\\*", "%2A")
                .replaceAll("%7E", "~");
    }

    /**
     * HmacSHA256签名计算
     */
    private static String generateHmacSha256Signature(String data, String secret)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(keySpec);
        byte[] digest = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(digest);
    }

    /**
     * 生成签名头列表（x-ca-signature-headers）
     */
    private static String generateSignatureHeaders(Map<String, Object> headers) {
        return getSignableHeaders(headers).stream()
                .collect(Collectors.joining(","));
    }
}