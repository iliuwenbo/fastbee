package com.fastbee.common.core.redis;

import com.fastbee.common.constant.CacheConstants;
import com.fastbee.common.constant.FastBeeConstant;

/**
 * 缓存key生成器
 *
 * @author bill
 */
public class RedisKeyBuilder {

    /**设备在线列表缓存key*/
    public static String buildDeviceOnlineListKey(){
       return FastBeeConstant.REDIS.DEVICE_ONLINE_LIST;
    }

    /**设备实时数据key*/
    public static String buildDeviceRtCacheKey(String serialNumber){
        return FastBeeConstant.REDIS.DEVICE_RUNTIME_DATA + serialNumber;
    }

    /**
     * 设备通讯协议参数
     */
    public static String buildDeviceRtParamsKey(String serialNumber){
        return FastBeeConstant.REDIS.DEVICE_PROTOCOL_PARAM + serialNumber;
    }

    /**固件版本缓存key*/
    public static String buildFirmwareCachedKey(Long firmwareId){
       return FastBeeConstant.REDIS.FIRMWARE_VERSION + firmwareId;
    }

    /**属性读取回调缓存key*/
    public static String buildPropReadCacheKey(String serialNumber){
        return FastBeeConstant.REDIS.PROP_READ_STORE + serialNumber;
    }

    /**
     * 物模型值命名缓存key
     * Key：TSLV:{productId}_{deviceNumber}   HKey:{identity#V/identity#S/identity#M/identity#N}
     */
    public static String buildTSLVCacheKey(Long productId,String serialNumber){
        return FastBeeConstant.REDIS.DEVICE_PRE_KEY + productId + "_" + serialNumber.toUpperCase();
    }

    /**
     * 物模型缓存key
     * 物模型命名空间：Key:TSL:{productId}  hkey: identity  value: thingsModel
     */
    public static String buildTSLCacheKey(Long productId){
        return FastBeeConstant.REDIS.TSL_PRE_KEY + productId;
    }

    public static String buildModbusKey(Long productId){
        return FastBeeConstant.REDIS.MODBUS_PRE_KEY + productId;
    }

    /**录像缓存key*/
    public static String buildSipRecordinfoCacheKey(String recordKey){
        return FastBeeConstant.REDIS.RECORDINFO_KEY + recordKey;
    }

    /**设备id缓存key*/
    public static String buildSipDeviceidCacheKey(String id){
        return FastBeeConstant.REDIS.DEVICEID_KEY + id;
    }
    /**ipCSEQ缓存key*/
    public static String buildStreamCacheKey(String steamId){
        return FastBeeConstant.REDIS.STREAM_KEY + steamId;
    }

    public static String buildStreamCacheKey(String deviceId, String channelId, String stream, String ssrc){
        return FastBeeConstant.REDIS.STREAM_KEY + deviceId + ":" + channelId + ":" + stream + ":" + ssrc;
    }

    public static String buildInviteCacheKey(String type, String deviceId, String channelId, String stream, String ssrc){
        return FastBeeConstant.REDIS.INVITE_KEY + type + ":"+ deviceId + ":" + channelId + ":" + stream + ":" + ssrc;
    }

    /**ipCSEQ缓存key*/
    public static String buildSipCSEQCacheKey(String CSEQ){
        return FastBeeConstant.REDIS.SIP_CSEQ_PREFIX + CSEQ;
    }

    /**rule静默时间缓存key*/
    public static String buildSilentTimeacheKey(String key){
        return FastBeeConstant.REDIS.RULE_SILENT_TIME + key;
    }

    /**modbus指令缓存key*/
    public static String buildModbusPollCacheKey(String serialNumebr){
        return FastBeeConstant.REDIS.POLL_MODBUS_KEY + serialNumebr;
    }
    /*缓存设备下发指令消息ID*/
    public static String buildDownMessageIdCacheKey(String serialNumber){
        return FastBeeConstant.REDIS.DEVICE_MESSAGE_ID + serialNumber;
    }

    /**
     * 缓存产品id，设备编号，协议编号
     */
    public static String buildDeviceMsgCacheKey(String serialNumber){
        return FastBeeConstant.REDIS.DEVICE_MSG + serialNumber;
    }

    /**
     * 缓存产品id，设备编号，协议编号
     */
    public static String buildSceneModelTagCacheKey(Long sceneModelId){
        return FastBeeConstant.REDIS.SCENE_MODEL_TAG_ID + sceneModelId;
    }

    public static String buildModbusRuntimeCacheKey(String serialNumber){
        return FastBeeConstant.REDIS.MODBUS_RUNTIME + serialNumber;
    }

    public static String buildModbusLockCacheKey(String serialNumber){
        return FastBeeConstant.REDIS.MODBUS_LOCK + serialNumber;
    }

    public static String buildModbusTcpCacheKey(String serialNumber){
        return FastBeeConstant.REDIS.MODBUS_TCP + serialNumber;
    }

    public static String buildModbusTcpRuntimeCacheKey(String serialNumber){
        return FastBeeConstant.REDIS.MODBUS_TCP_RUNTIME + serialNumber;
    }


    /**设备OTA升级实时数据*/
    public static String buildDeviceOtaKey(String serialNumber){
        return CacheConstants.DEVICE_OTA_DATA + serialNumber;
    }


}
