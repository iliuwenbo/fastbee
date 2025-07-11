package com.fastbee.platform.handler.strategy;

import com.fastbee.platform.enums.EncryptionType;
import com.fastbee.platform.vo.ApiRequestDetails;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;


@Component("hikvision")
public class HikvisionEncryptionStrategy implements EncryptionType.EncryptionStrategy {

    @Override
    public ApiRequestDetails processWithSignature(ApiRequestDetails requestDetails, String appKey, String appSecret) throws Exception {
        // 1. 初始化必填参数
        Map<String, Object> headers = initHeaders(requestDetails, appKey);
        String httpMethod = requestDetails.getMethod().toUpperCase();
        // 提取URI路径, 移除协议和域名部分
        String path = requestDetails.getApiUrl().replaceFirst("^https?://[^/]+", "");
        Map<String, String> queryParams = convertToMap(requestDetails.getQuery());
        Map<String, String> bodyFormParams = convertToMap(requestDetails.getBody());

        // 2. 生成Content-MD5
        String contentMd5 = "";
        if (requestDetails.getBody() != null && !requestDetails.getBody().isEmpty()) {
            String bodyJson = requestDetails.getBody().toString(); // 假设body是JSON字符串
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

        // 6. 更新请求详情对象
        requestDetails.setHeader(headers);
        return requestDetails;
    }


    private Map<String, Object> initHeaders(ApiRequestDetails requestDetails, String appKey) {
        Map<String, Object> headers = new LinkedHashMap<>(requestDetails.getHeader());
        headers.putIfAbsent("Accept", "*/*");
        headers.putIfAbsent("Content-Type", "application/json;charset=UTF-8");
        headers.put("Date", new Date().toGMTString());
        headers.put("X-Ca-Timestamp", String.valueOf(System.currentTimeMillis()));
        return headers;
    }

    private Map<String, String> convertToMap(Map<String, Object> source) {
        if (source == null) return new HashMap<>();
        Map<String, String> result = new HashMap<>();
        source.forEach((k, v) -> result.put(k, v != null ? v.toString() : ""));
        return result;
    }

    private String calculateContentMD5(String body) throws NoSuchAlgorithmException {
        if (body == null || body.isEmpty()) return "";
        byte[] md5Bytes = java.security.MessageDigest.getInstance("MD5").digest(body.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(md5Bytes);
    }

    private String buildStringToSign(String httpMethod, Map<String, Object> headers,
                                     String path, Map<String, String> queryParams, Map<String, String> bodyFormParams) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder(httpMethod).append("\n");

        addSpecialHeader(sb, headers, "Accept");
        addSpecialHeader(sb, headers, "Content-MD5");
        addSpecialHeader(sb, headers, "Content-Type");
        addSpecialHeader(sb, headers, "Date");

        List<String> signHeaders = getSignableHeaders(headers);
        signHeaders.forEach(key -> sb.append(key).append(":").append(headers.get(key)).append("\n"));

        sb.append(buildUrlPath(path, queryParams, bodyFormParams));
        return sb.toString();
    }

    private void addSpecialHeader(StringBuilder sb, Map<String, Object> headers, String headerName) {
        Object value = headers.get(headerName);
        if (value != null) {
            sb.append(value).append("\n");
        } else {
            sb.append("\n");
        }
    }

    private List<String> getSignableHeaders(Map<String, Object> headers) {
        return headers.keySet().stream()
                .map(String::toLowerCase)
                .filter(k -> k.startsWith("x-ca-"))
                .sorted()
                .collect(Collectors.toList());
    }

    private String buildUrlPath(String path, Map<String, String> queryParams, Map<String, String> bodyFormParams) throws UnsupportedEncodingException {
        Map<String, String> mergedParams = new TreeMap<>(queryParams);
        if (bodyFormParams != null) {
            mergedParams.putAll(bodyFormParams);
        }

        if (mergedParams.isEmpty()) {
            return path;
        }

        StringBuilder paramBuilder = new StringBuilder(path);
        if (!mergedParams.isEmpty()) {
            paramBuilder.append("?");
        }

        paramBuilder.append(
                mergedParams.entrySet().stream()
                        .map(entry -> {
                            try {
                                String key = entry.getKey();
                                String value = entry.getValue();
                                if (value == null) {
                                    return urlEncode(key);
                                }
                                return urlEncode(key) + "=" + urlEncode(value);
                            } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.joining("&"))
        );
        return paramBuilder.toString();
    }

    private String urlEncode(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, "UTF-8")
                .replaceAll("\\+", "%20")
                .replaceAll("\\*", "%2A")
                .replaceAll("%7E", "~");
    }

    private String generateHmacSha256Signature(String data, String secret)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(keySpec);
        byte[] digest = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(digest);
    }

    private String generateSignatureHeaders(Map<String, Object> headers) {
        return getSignableHeaders(headers).stream()
                .collect(Collectors.joining(","));
    }
}
