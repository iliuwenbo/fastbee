package com.fastbee.platform.controller;

import com.fastbee.http.stream.dto.StreamDTO;
import com.fastbee.http.stream.factory.StreamServiceFactory;
import com.fastbee.http.stream.result.StreamVo;
import com.fastbee.http.stream.service.StreamService;
import org.springframework.web.bind.annotation.*;

/**
 * 视频流服务控制器
 */
@RestController
@RequestMapping("/api/stream")
public class StreamController {

    private final StreamServiceFactory streamServiceFactory;

    public StreamController(StreamServiceFactory streamServiceFactory) {
        this.streamServiceFactory = streamServiceFactory;
    }

    /**
     * 获取视频流地址
     * @param requestDTO 流请求参数
     * @return 流响应结果
     */
    @PostMapping("/getStream")
    public StreamVo getStream(@RequestBody StreamDTO requestDTO) {
        // 参数校验
        if (requestDTO == null) {
            throw new IllegalArgumentException("请求参数不能为空");
        }

        // 获取对应的流服务
        StreamService service = streamServiceFactory.getServiceByManufacturer(requestDTO.getManufacturerType());

        // 调用服务获取流信息
        return service.getStreamInfo(requestDTO.getDeviceId(), requestDTO.getParams());
    }
}