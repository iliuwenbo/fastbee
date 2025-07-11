package com.fastbee.platform.controller;

import cn.hutool.core.map.MapUtil;
import com.fastbee.common.core.domain.AjaxResult;
import com.fastbee.platform.enums.EncryptionType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 前端常量数据获取
 *
 * @author 编码助手
 */
@RestController
@RequestMapping("/api/enum")
@Api(tags = "枚举常量获取")
public class ApiEnumController {

    /**
     * 获取所有加密策略类型
     */
    @GetMapping("/encryption-types")
    @ApiOperation("获取所有加密策略类型")
    public AjaxResult getEncryptionTypes() {
        // 使用 Stream 流将枚举转换为类型安全的 Map 列表
        List<Map<String, String>> typeList = Arrays.stream(EncryptionType.values())
                .map(e -> MapUtil.<String, String>builder()
                        .put("code", e.getCode())
                        .put("title", e.getDescription())
                        .build())
                .collect(Collectors.toList());
        return AjaxResult.success(typeList);
    }
}