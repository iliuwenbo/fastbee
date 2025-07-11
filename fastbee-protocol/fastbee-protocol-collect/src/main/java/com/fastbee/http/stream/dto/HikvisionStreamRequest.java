package com.fastbee.http.stream.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HikvisionStreamRequest{

    /**
     * 监控点唯一标识
     * 通过分页获取监控点资源接口获取
     */
    @JSONField(name = "cameraIndexCode")  // 序列化成 JSON 时字段名为 cameraIndexCode
    private String deviceId;

    /**
     * 码流类型
     * 0:主码流 1:子码流 2:第三码流
     */
    private String streamType = "0";
    /**
     * 取流协议（应用层协议）
     * "hik":HIK私有协议
     * "rtsp":RTSP协议
     * "rtmp":RTMP协议
     * "hls":HLS协议
     * "ws":Websocket协议
     */
    private String protocol = "rtsp";
    /**
     * 传输协议（传输层协议）
     * 0:UDP 1:TCP
     */
    private String transmode = "UDP";
    /**
     * 扩展内容
     * 格式：key=value
     */
    private String expand;
    /**
     * 输出码流转封装格式
     * "ps":PS封装格式
     * "rtp":RTP封装协议
     */
    private String streamform = "rtp";

    public HikvisionStreamRequest(String deviceId) {
        this.deviceId = deviceId;
    }
}
