package com.fastbee.http.hk.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 视频丢失事件数据模型
 * 继承自CommonEventData，包含视频丢失事件的特定属性
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VideoLossEventData extends CommonEventData {
    // 目前与公共基类属性一致，无需额外属性
    // 如需添加视频丢失特有的属性，可在此处扩展
}