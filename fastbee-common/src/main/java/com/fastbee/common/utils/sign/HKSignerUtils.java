package com.fastbee.common.utils.sign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.*;

/**
 * 海康威视API签名工具类（返回Map结构）
 */
public class HKSignerUtils {
    private static final Logger logger = LoggerFactory.getLogger(HKSignerUtils.class);

    // 不参与签名的Header（小写格式）
    private static final Set<String> EXCLUDED_HEADERS = new HashSet<>(Arrays.asList(
            "accept", "content-md5", "content-type", "date",
            "content-length", "host", "connection",
            "x-ca-signature", "x-ca-signature-headers",
            "server", "transfer-encoding", "x-application-context", "content-encoding"
    ));

    // GMT时间格式化器
    private static final SimpleDateFormat GMT_DATE_FORMAT = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    static {
        GMT_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    /**
     * 生成带签名的请求头Map
     * @param httpMethod 请求方法（GET/POST等）
     * @param requestUrl 完整请求URL
     * @param requestBody 请求体内容
     * @param appKey 应用Key
     * @param appSecret 应用密钥
     * @return 包含所有必要Header的Map
     */
    public static Map<String, String> generateSignedHeaders(
            String httpMethod,
            String requestUrl,
            String requestBody,
            String appKey,
            String appSecret) {
        return generateSignedHeaders(httpMethod,requestUrl,null,null,requestBody,null,appKey,appSecret);
    }

    /**
     * 生成带签名的请求头Map
     * @param httpMethod 请求方法（GET/POST等）
     * @param requestUrl 完整请求URL
     * @param queryParams 查询参数
     * @param formParams 表单参数
     * @param requestBody 请求体内容
     * @param originalHeaders 原始请求头
     * @param appKey 应用Key
     * @param appSecret 应用密钥
     * @return 包含所有必要Header的Map
     */
    public static Map<String, String> generateSignedHeaders(
            String httpMethod,
            String requestUrl,
            Map<String, String> queryParams,
            Map<String, String> formParams,
            String requestBody,
            Map<String, String> originalHeaders,
            String appKey,
            String appSecret) {

        Map<String, String> signedHeaders = new LinkedHashMap<>();

        try {
            logger.debug("开始生成签名Header，方法: {}, URL: {}", httpMethod, requestUrl);

            // 1. 准备系统级Header
            prepareSystemHeaders(signedHeaders, appKey);

            // 2. 处理特殊Header
            processSpecialHeaders(signedHeaders, originalHeaders, requestBody);

            // 3. 添加自定义Header
            addCustomHeaders(signedHeaders, originalHeaders);

            // 4. 构建签名字符串
            String stringToSign = buildStringToSign(
                    httpMethod.toUpperCase(),
                    signedHeaders,
                    extractPath(requestUrl),
                    mergeParams(queryParams, formParams)
            );

            logger.debug("待签名字符串:\n{}", stringToSign);

            // 5. 计算签名并添加
            signedHeaders.put("X-Ca-Signature", calculateSignature(stringToSign, appSecret));

            // 6. 添加签名头列表
            signedHeaders.put("X-Ca-Signature-Headers", getSignatureHeaders(signedHeaders));

            logger.info("签名Header生成成功");
            return signedHeaders;

        } catch (Exception e) {
            logger.error("生成签名Header失败", e);
            throw new RuntimeException("生成签名Header失败: " + e.getMessage(), e);
        }
    }

    // ====================== 核心私有方法 ======================

    /**
     * 准备系统级Header
     */
    private static void prepareSystemHeaders(Map<String, String> headers, String appKey) {
        // 必选Header
        headers.put("X-Ca-Key", appKey);
        headers.put("X-Ca-Timestamp", String.valueOf(System.currentTimeMillis()));
        headers.put("X-Ca-Nonce", UUID.randomUUID().toString());

        // 默认Header
        headers.put("Accept", "*/*");
        headers.put("Content-Type", "application/json");
        headers.put("Date", GMT_DATE_FORMAT.format(new Date()));

        logger.debug("系统级Header准备完成: {}", headers);
    }

    /**
     * 处理特殊Header（Accept/Content-MD5/Content-Type/Date）
     */
    private static void processSpecialHeaders(
            Map<String, String> signedHeaders,
            Map<String, String> originalHeaders,
            String requestBody) throws NoSuchAlgorithmException {

        // 覆盖默认值（如果原始Header中存在）
        if (originalHeaders != null) {
            if (originalHeaders.containsKey("Accept")) {
                signedHeaders.put("Accept", originalHeaders.get("Accept"));
            }
            if (originalHeaders.containsKey("Content-Type")) {
                signedHeaders.put("Content-Type", originalHeaders.get("Content-Type"));
            }
            if (originalHeaders.containsKey("Date")) {
                signedHeaders.put("Date", originalHeaders.get("Date"));
            }
        }

        // 仅当非表单请求时计算Content-MD5
        boolean isFormRequest = signedHeaders.get("Content-Type").contains("x-www-form-urlencoded");
        if (!isFormRequest && requestBody != null && !requestBody.isEmpty()) {
            String contentMd5 = calculateContentMd5(requestBody);
            signedHeaders.put("Content-MD5", contentMd5);
            logger.debug("计算Content-MD5: {}", contentMd5);
        }
    }

    /**
     * 添加自定义Header
     */
    private static void addCustomHeaders(
            Map<String, String> signedHeaders,
            Map<String, String> originalHeaders) {
        if (originalHeaders == null) return;

        originalHeaders.forEach((key, value) -> {
            if (!EXCLUDED_HEADERS.contains(key.toLowerCase())) {
                signedHeaders.put(key, value);
            }
        });

        logger.debug("所有参与签名的Header: {}", signedHeaders);
    }

    /**
     * 构建待签名字符串
     */
    private static String buildStringToSign(
            String httpMethod,
            Map<String, String> signedHeaders,
            String requestPath,
            Map<String, String> allParams) {

        StringBuilder sb = new StringBuilder();

        // 1. HTTP方法
        sb.append(httpMethod).append("\n");

        // 2. 特殊Header（固定顺序）
        appendHeaderIfExists(sb, signedHeaders, "Accept");
        appendHeaderIfExists(sb, signedHeaders, "Content-MD5");
        appendHeaderIfExists(sb, signedHeaders, "Content-Type");
        appendHeaderIfExists(sb, signedHeaders, "Date");

        // 3. 其他参与签名的Header
        getSortedHeaders(signedHeaders).forEach(header ->
                sb.append(header.getKey().toLowerCase())
                        .append(":")
                        .append(header.getValue().trim())
                        .append("\n")
        );

        // 4. URL路径和参数
        sb.append(buildRequestPathWithParams(requestPath, allParams));

        return sb.toString();
    }

    /**
     * 构建带参数的请求路径
     */
    private static String buildRequestPathWithParams(String path, Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return path;
        }

        StringBuilder sb = new StringBuilder(path).append("?");
        boolean isFirstParam = true;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!isFirstParam) sb.append("&");
            isFirstParam = false;

            sb.append(entry.getKey());
            if (!entry.getValue().isEmpty()) {
                try {
                    sb.append("=").append(urlEncode(entry.getValue()));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("URL编码失败: " + entry.getValue(), e);
                }
            }
        }

        logger.debug("构建的请求路径: {}", sb.toString());
        return sb.toString();
    }

    /**
     * 获取签名头列表（X-Ca-Signature-Headers）
     */
    private static String getSignatureHeaders(Map<String, String> signedHeaders) {
        List<String> signatureHeaders = new ArrayList<>();

        for (String key : signedHeaders.keySet()) {
            if (!EXCLUDED_HEADERS.contains(key.toLowerCase())) {
                signatureHeaders.add(key.toLowerCase());
            }
        }

        Collections.sort(signatureHeaders);
        return String.join(",", signatureHeaders);
    }

    // ====================== 工具方法 ======================

    /**
     * 追加Header内容（如果存在）
     */
    private static void appendHeaderIfExists(
            StringBuilder builder,
            Map<String, String> headers,
            String headerName) {
        if (headers.containsKey(headerName)) {
            builder.append(headers.get(headerName).trim()).append("\n");
        }
    }

    /**
     * 获取排序后的Header列表
     */
    private static List<Map.Entry<String, String>> getSortedHeaders(Map<String, String> headers) {
        List<Map.Entry<String, String>> headerList = new ArrayList<>();

        headers.forEach((key, value) -> {
            if (!EXCLUDED_HEADERS.contains(key.toLowerCase())) {
                headerList.add(new AbstractMap.SimpleEntry<>(key, value));
            }
        });

        headerList.sort(Comparator.comparing(
                entry -> entry.getKey().toLowerCase(),
                String.CASE_INSENSITIVE_ORDER
        ));

        return headerList;
    }

    /**
     * 计算HMAC-SHA256签名
     */
    private static String calculateSignature(String data, String secretKey)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac = Mac.getInstance("HmacSHA256");
        hmac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signatureBytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return java.util.Base64.getEncoder().encodeToString(signatureBytes);
    }

    /**
     * 计算Content-MD5
     */
    private static String calculateContentMd5(String content) throws NoSuchAlgorithmException {
        byte[] md5Bytes = java.security.MessageDigest.getInstance("MD5")
                .digest(content.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(md5Bytes);
    }

    /**
     * 从URL中提取路径部分
     */
    private static String extractPath(String url) {
        return url.replaceFirst("^https?://[^/]+", "");
    }

    /**
     * 合并查询参数和表单参数
     */
    private static Map<String, String> mergeParams(
            Map<String, String> queryParams,
            Map<String, String> formParams) {
        Map<String, String> mergedParams = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        if (queryParams != null) mergedParams.putAll(queryParams);
        if (formParams != null) mergedParams.putAll(formParams);
        return mergedParams;
    }

    /**
     * URL编码（RFC 3986规范）
     */
    private static String urlEncode(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, "UTF-8")
                .replace("+", "%20")
                .replace("*", "%2A")
                .replace("%7E", "~");
    }

    // ====================== 测试方法 ======================

    public static void main(String[] args) {
        try {
            // 测试数据准备
            String httpMethod = "POST";
            String apiUrl = "https://example.com/artemis/api/resource";
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("page", "1");
            Map<String, String> formParams = new HashMap<>();
            formParams.put("name", "测试数据");
            Map<String, String> customHeaders = new HashMap<>();
            customHeaders.put("X-Custom-Header", "custom-value");

            // 生成签名Header
            Map<String, String> signedHeaders = generateSignedHeaders(
                    httpMethod,
                    apiUrl,
                    queryParams,
                    formParams,
                    "",
                    customHeaders,
                    "TEST_APP_KEY",
                    "TEST_APP_SECRET");

            // 输出结果
            System.out.println("========== 签名Header ==========");
            signedHeaders.forEach((k, v) -> System.out.println(k + ": " + v));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}