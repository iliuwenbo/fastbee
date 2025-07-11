package com.fastbee.scada.utils;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author fastb
 * @version 1.0
 * @description: 组态文件工具类
 * @date 2024-03-29 14:55
 */
public class ScadaFileUtils {

    public static MultipartFile base64toMultipartFile(String base64) {
        final String[] base64Array = base64.split(",");
        String dataUir, data;
        if (base64Array.length > 1) {
            dataUir = base64Array[0];
            data = base64Array[1];
        } else {
            //根据你base64代表的具体文件构建
            dataUir = "data:image/png;base64";
            data = base64Array[0];
        }
        return new ScadaBase64ToMultipartFile(data, dataUir);
    }
}
