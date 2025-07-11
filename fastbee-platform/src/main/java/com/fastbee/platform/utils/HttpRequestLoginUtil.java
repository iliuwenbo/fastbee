package com.fastbee.platform.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestLoginUtil {
    private static String accessToken;

    public static Map<String, Object> getAccessToken(String baseUrl, String loginUsername, String loginPassword, String tenantName){
        try {
            sendLoginRequest(baseUrl, loginUsername, loginPassword, tenantName);

            // 发送其他需要认证的请求
            Map<String, Object> headers = new HashMap<>();
            setAccessToken(headers);
            // 这里可以使用headers发送其他请求
            System.out.println("请求头: " + headers);
            return headers;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置访问令牌到请求头
     * @param headers 请求头Map
     */
    public static void setAccessToken(Map<String, Object> headers) {
        if (accessToken != null && !accessToken.isEmpty()) {
            headers.put("authorization", "Bearer " + accessToken);
        }
    }

    /**
     * 发送登录请求并获取访问令牌
     * @param baseUrl 基础URL
     * @param loginUsername 登录用户名
     * @param loginPassword 登录密码
     * @param tenantName 租户名称
     * @throws IOException 如果发送请求过程中发生I/O错误
     */
    public static void sendLoginRequest(String baseUrl, String loginUsername, String loginPassword, String tenantName) throws IOException {
        String loginUrl = baseUrl + "/admin-api/system/auth/login";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        String requestBody = String.format("{\"username\": \"%s\", \"password\": \"%s\", \"tenantName\": \"%s\"}",
                loginUsername, loginPassword, tenantName);

        HttpResponse response = sendPostRequest(loginUrl, headers, requestBody);
        if (response.getStatusCode() == 200) {
            // 解析JSON响应获取accessToken
            String jsonResponse = response.getBody();
            // 简单的JSON解析，实际项目中建议使用JSON库如Jackson或Gson
            int startIndex = jsonResponse.indexOf("\"accessToken\":\"") + "\"accessToken\":\"".length();
            int endIndex = jsonResponse.indexOf("\"", startIndex);
            if (startIndex > "\"accessToken\":\"".length() && endIndex > startIndex) {
                accessToken = jsonResponse.substring(startIndex, endIndex);
                System.out.println("获取到访问令牌: " + accessToken);
            } else {
                System.err.println("无法从响应中获取访问令牌");
                System.err.println("响应内容: " + jsonResponse);
            }
        } else {
            System.err.println("登录请求失败，状态码: " + response.getStatusCode());
            System.err.println("响应内容: " + response.getBody());
        }
    }

    /**
     * 发送POST请求
     * @param url 请求URL
     * @param headers 请求头
     * @param body 请求体
     * @return 响应对象
     * @throws IOException 如果发送请求过程中发生I/O错误
     */
    private static HttpResponse sendPostRequest(String url, Map<String, String> headers, String body) throws IOException {
        HttpURLConnection connection = null;
        try {
            URL requestUrl = new URL(url);
            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // 设置请求头
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            // 写入请求体
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = body.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // 读取响应
            int statusCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();

            try (BufferedReader br = statusCode == 200 ?
                    new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8")) :
                    new BufferedReader(new InputStreamReader(connection.getErrorStream(), "utf-8"))) {

                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            return new HttpResponse(statusCode, response.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 获取当前的访问令牌
     * @return 访问令牌
     */
    public static String getAccessToken() {
        return accessToken;
    }

    /**
     * 内部类，表示HTTP响应
     */
    static class HttpResponse {
        private final int statusCode;
        private final String body;

        public HttpResponse(int statusCode, String body) {
            this.statusCode = statusCode;
            this.body = body;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getBody() {
            return body;
        }
    }

    public static void main(String[] args) {
        // 使用示例
        String baseUrl = "http://192.168.0.201:8081";
        String username = "admin";
        String password = "admin123";
        String tenantName = "芋道源码";

        try {
            sendLoginRequest(baseUrl, username, password, tenantName);

            // 发送其他需要认证的请求
            Map<String, Object> headers = new HashMap<>();
            setAccessToken(headers);
            // 这里可以使用headers发送其他请求
            System.out.println("请求头: " + headers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}