package com.fastbee.parse.domain;

import lombok.Data;
import java.util.List;

/**
 * AI解析服务请求/响应的根对象模型。
 * 用于封装AI解析服务的API调用结果，包含响应码、消息和具体数据体。
 */
@Data
public class AiRequest {
    /**
     * 响应状态码，通常用于表示请求处理的结果 (例如，0表示成功)。
     */
    private long code;

    /**
     * API响应的具体数据负载。
     * 包含AI解析生成的详细结果、产品信息等。
     */
    private AiData data;

    /**
     * 响应消息，用于描述请求处理结果的文本信息 (例如，"成功", "错误原因")。
     */
    private String message;

    /**
     * AI解析响应的具体数据体。
     * 包含解析文件信息、生成的API列表、产品信息等详细内容。
     */
    @Data
    public static class AiData {
        /**
         * 解析的文件名称。
         */
        private String fileName;

        /**
         * 解析的文件大小，单位为KB。
         */
        private String fileSizeInKB;

        /**
         * AI解析或生成的相关API列表。
         * 这些API可能用于进一步交互或集成。
         */
        private List<GeneratedApiList> generatedApiList;

        /**
         * 本次AI解析任务的唯一标识符。
         */
        private String id;

        /**
         * 文件导入过程的结果。
         * 类型为Object，可能包含详细的导入状态或错误信息。
         */
        private Object importResult; // 如果有具体结构，建议替换 Object

        /**
         * AI解析识别出的产品信息列表。
         * 包含产品的描述、属性等。
         */
        private List<ProductInfo> productInfos;

        /**
         * 提供给AI的原始提示（Prompt）文本。
         */
        private String prompt;

        /**
         * AI对提示文本的处理结果或摘要。
         */
        private String promptResult;

        /**
         * AI解析处理的状态码。
         * 例如，处理中、完成、失败等。
         */
        private long status;

        /**
         * AI解析处理状态的文字描述。
         */
        private String statusName;

        /**
         * 对整个文件或某些部分的验证结果。
         * 类型为ValidationResult，包含验证的状态和信息。
         */
        private ValidationResult validationResult;
    }

    /**
     * AI解析或生成的一个具体的API信息项。
     */
    @Data
    public static class GeneratedApiList {
        /**
         * 生成的API名称。
         */
        private String apiName;

        /**
         * 该API的参数详细列表。
         */
        private List<GeneratedApiParameterDetail> apiParamDetailList;

        /**
         * API的类型。
         * 类型为Object，可能表示RESTful, MQTT等。
         */
        private Object apiType; // 如果有具体类型，建议替换 Object

        /**
         * API的访问URL或地址。
         */
        private String apiUrl;

        /**
         * API所需的认证信息。
         * 类型为AuthenticationInfo。
         */
        private AuthenticationInfo authenticationInfo;

        /**
         * API的请求方法 (例如，GET, POST)。
         */
        private String method;

        /**
         * API相关的备注或说明。
         * 类型为Object。
         */
        private Object remark; // 如果有具体结构，建议替换 Object

        /**
         * API请求的数据格式。
         * 类型为Object，可能表示JSON, XML等。
         */
        private Object requestFormat; // 如果有具体格式，建议替换 Object
    }

    /**
     * AI 生成的API参数的详细信息。
     */
    @Data
    public static class GeneratedApiParameterDetail { // 规范化类名
        /**
         * 参数是否应自动追加。
         * 使用Long类型，可能表示布尔值 (0/1) 或其他状态。建议考虑Boolean类型。
         */
        private Long autoAppend; // 考虑改为 Boolean

        /**
         * 参数的名称。
         */
        private String paramName;

        /**
         * 参数的数据类型 (例如，string, int)。
         */
        private String paramType;

        /**
         * 参数的示例值或默认值。
         */
        private String paramValue;

        /**
         * 参数相关的备注或说明。
         */
        private String remark;

        /**
         * 参数是否是必需的。
         * 使用Long类型，可能表示布尔值 (0/1) 或其他状态。建议考虑Boolean类型。
         */
        private Long required; // 考虑改为 Boolean
    }

    /**
     * AI解析识别出的具体产品信息。
     */
    @Data
    public static class ProductInfo {
        /**
         * 与该产品相关的设备信息列表。
         */
        private List<DeviceInfo> deviceInfos;

        /**
         * 与该产品相关的解析协议信息。
         */
        private ParsingProtocol parsingProtocol;

        /**
         * 产品的详细描述。
         */
        private String productDescription;

        /**
         * 产品的名称。
         */
        private String productName;

        /**
         * 产品的属性列表。
         */
        private List<Property> properties;

        /**
         * 该产品信息的验证结果。
         * 类型为ValidationResult。
         */
        private ValidationResult validationResult;
    }

    /**
     * 具体设备的信息。
     * 通常与产品关联。
     */
    @Data
    public static class DeviceInfo {
        /**
         * 设备的唯一编码。
         */
        private String deviceCode;

        /**
         * 设备的名称。
         */
        private String deviceName;
    }

    /**
     * 用于描述数据解析协议的详细信息。
     * 例如，脚本、输入输出参数等。
     */
    @Data
    public static class ParsingProtocol {
        /**
         * 协议预期输出的数据格式或内容描述。
         */
        private String expectedOutputs;

        /**
         * 协议所需的输入参数描述。
         */
        private String inputParams;

        /**
         * 解析协议的名称。
         */
        private String name;

        /**
         * 解析协议的脚本或逻辑代码。
         */
        private String script;
    }

    /**
     * 产品的一个具体属性信息。
     * 例如，温度、湿度等。
     */
    @Data
    public static class Property {
        /**
         * 与该属性关联的API信息。
         * 用于获取或设置属性值。
         */
        private API api; // 引用内部的 API 类

        /**
         * 在设备信息中标识设备编码的字段名称。
         * 用于关联属性到具体设备。
         */
        private String deviceCodeFieldName;

        /**
         * 属性的详细描述。
         */
        private String propertyDescription;

        /**
         * 属性的名称。
         */
        private String propertyName;
    }

    /**
     * 用于描述与产品属性关联的API信息。
     * 注意与顶层 GeneratedApiList 的区分，后者是AI生成结果中的API列表。
     */
    @Data
    public static class API {
        /**
         * 关联API的名称。
         */
        private String apiName;

        /**
         * 该关联API的参数详细列表。
         */
        private List<ApiParameterDetail> apiParamDetailList; // 使用规范化后的类名

        /**
         * 关联API的类型。
         * 类型为Object。
         */
        private Object apiType; // 如果有具体类型，建议替换 Object

        /**
         * 关联API的访问URL或地址。
         */
        private String apiUrl;

        /**
         * 关联API所需的认证信息。
         * 类型为AuthenticationInfo。
         */
        private AuthenticationInfo authenticationInfo;

        /**
         * 关联API的请求方法。
         */
        private String method;

        /**
         * 关联API相关的备注或说明。
         * 类型为Object。
         */
        private String remark;

        /**
         * 关联API请求的数据格式。
         * 类型为Object。
         */
        private Object requestFormat; // 如果有具体格式，建议替换 Object
    }

    /**
     * 产品属性关联的API参数的详细信息。
     */
    @Data
    public static class ApiParameterDetail { // 规范化类名
        /**
         * 参数是否应自动追加。
         * 使用Long类型，可能表示布尔值 (0/1) 或其他状态。建议考虑Boolean类型。
         */
        private Long autoAppend; // 考虑改为 Boolean

        /**
         * 参数的名称。
         */
        private String paramName;

        /**
         * 参数的数据类型。
         */
        private String paramType;

        /**
         * 参数的示例值或默认值。
         */
        private String paramValue;

        /**
         * 参数相关的备注或说明。
         */
        private String remark;

        /**
         * 参数是否是必需的。
         * 使用Long类型，可能表示布尔值 (0/1) 或其他状态。建议考虑Boolean类型。
         */
        private Long required; // 考虑改为 Boolean
    }

    /**
     * API或服务所需的认证信息。
     */
    @Data
    public static class AuthenticationInfo {
        /**
         * 认证类型 (例如，apiKey, OAuth)。
         */
        private String authenticationType;

        /**
         * 平台ID或应用ID。
         */
        private String platformId;
    }

    /**
     * 验证过程的结果信息。
     * 用于表示数据或结构的验证状态。
     */
    @Data
    public static class ValidationResult {
        /**
         * 验证的详细结果描述。
         * 例如，"通过", "失败原因"。
         */
        private String result;

        /**
         * 验证的状态码。
         * 例如，0表示成功，非0表示失败。
         */
        private long status;

        /**
         * 验证状态的文字标签。
         */
        private String statusLabel;
    }
}