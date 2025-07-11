package com.fastbee.http.stream.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.dtflys.forest.annotation.BindingVar;
import com.fastbee.common.exception.base.BaseException;
import com.fastbee.common.utils.sign.HKSignerUtils;
import com.fastbee.http.stream.api.HikvisionApiClient;
import com.fastbee.http.stream.dto.HikvisionStreamRequest;
import com.fastbee.http.stream.result.HikvisionStreamVo;
import com.fastbee.http.stream.result.StreamVo;
import com.fastbee.http.stream.service.StreamService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("hikvisionStreamService")
@Log4j2
public class HikvisionStreamService implements StreamService{
    @Autowired
    private HikvisionApiClient apiClient;

    @Value("${hikvision.apiUrl}")
    private String apiUrl;

    @Value("${hikvision.appKey}")
    private String appKey;

    @Value("${hikvision.appSecret}")
    private String appSecret;

    @BindingVar("hkBaseUrl")
    public String getBaseUrl() {
        return apiUrl;
    }

    @Override
    public StreamVo getStreamInfo(String deviceId, Map<String, Object> params) {
        // 1. 参数校验
        if (StrUtil.isBlank(deviceId)) {
            throw new IllegalArgumentException("设备ID不能为空");
        }

        try {
            log.info("开始获取海康设备流信息, deviceId: {}", deviceId);

            // 2. 构建请求对象（简洁版）
            HikvisionStreamRequest request = params != null ?
                    BeanUtil.toBean(params,HikvisionStreamRequest.class):
                    new HikvisionStreamRequest(deviceId);

            // 3. 生成签名头
            String requestBody = JSONUtil.toJsonStr(request);
            Map<String, String> headers = HKSignerUtils.generateSignedHeaders(
                    "POST",
                    apiUrl,
                    requestBody,
                    appKey,
                    appSecret
            );

            // 4. 调用API获取流信息
            HikvisionStreamVo streamResponse = apiClient.getStreamUrl(request, headers);
            log.info("海康流信息响应: {}", JSONUtil.toJsonStr(streamResponse));

            // 5. 验证响应数据
            if (streamResponse == null) {
                log.error("海康流信息接口返回空响应, deviceId: {}", deviceId);
                throw new BaseException("海康设备流信息获取失败: 接口返回空响应");
            }

            if (StrUtil.isBlank(streamResponse.getData().getUrl())) {
                log.error("海康流信息URL为空, deviceId: {}, 响应: {}", deviceId, JSONUtil.toJsonStr(streamResponse));
                throw new BaseException("海康设备流信息获取失败: 流URL为空");
            }

            // 6. 构建返回结果
            StreamVo result = new StreamVo();
            result.setDeviceId(deviceId);
            result.setUrl(streamResponse.getUrl());

            log.info("成功获取海康设备流信息, deviceId: {}, url: {}", deviceId, result.getUrl());
            return result;

        } catch (BaseException e) {
            throw e; // 已处理的业务异常直接抛出
        } catch (Exception e) {
            log.error("获取海康设备流信息异常, deviceId: {}", deviceId, e);
            throw new BaseException("海康设备流信息获取失败: " + e.getMessage());
        }
    }
}
