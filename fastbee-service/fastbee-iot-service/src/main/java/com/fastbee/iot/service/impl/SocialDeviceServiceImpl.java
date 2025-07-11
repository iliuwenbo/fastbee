package com.fastbee.iot.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fastbee.common.utils.StringUtils;
import com.fastbee.common.utils.json.JsonUtils;
import com.fastbee.iot.cache.ITSLCache;
import com.fastbee.iot.cache.ITSLValueCache;
import com.fastbee.iot.domain.Device;
import com.fastbee.iot.domain.ThingsModel;
import com.fastbee.iot.domain.bo.DeviceBo;
import com.fastbee.iot.domain.bo.ThingsModelBo;
import com.fastbee.iot.mapper.*;
import com.fastbee.iot.model.*;
import com.fastbee.iot.model.ThingsModelItem.Datatype;
import com.fastbee.iot.model.ThingsModelItem.EnumItem;
import com.fastbee.iot.model.ThingsModels.ThingsModelValueItem;
import com.fastbee.iot.model.ThingsModels.ValueItem;
import com.fastbee.iot.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备Service业务层处理
 *
 * @author kerwincui
 * @date 2021-12-16
 */
@Service
public class SocialDeviceServiceImpl implements ISocialDeviceService {

    private static final Logger log = LoggerFactory.getLogger(SocialDeviceServiceImpl.class);
    @Autowired
    private SocialDeviceMapper socialDeviceMapper;
    @Autowired
    private ITSLCache itslCache;
    @Resource
    private ITSLValueCache itslValueCache;
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private ThingsModelMapper thingsModelMapper;

    /**
     * 查询设备分页简短列表
     *
     * @param device 设备
     * @return 设备
     */
    @Override
    public List<SocialDeviceShortOutput> selectDeviceShortList(Device device) {
        // 查询设备列表（iot_device单表）
        List<SocialDeviceShortOutput> list = socialDeviceMapper.selectDeviceRunningStatusByDeviceList(device);
        // 循环设备列表
        list.stream().forEach(deviceShortOutput -> {
            // 通过产品ID查询物模型（属性、功能、事件）三类
            JSONObject thingsModelObject = JSONObject.parseObject(itslCache.getCacheThingsModelByProductId(deviceShortOutput.getProductId()));
            JSONArray properties = thingsModelObject.getJSONArray("properties");
            JSONArray functions = thingsModelObject.getJSONArray("functions");
            List<ValueItem> thingsModelValueItems = itslValueCache.getCacheDeviceStatus(deviceShortOutput.getProductId(), deviceShortOutput.getSerialNumber());
            // 物模型转换赋值
            List<ThingsModelValueItem> thingsList = new ArrayList<>();
            //判断一下properties 和 functions是否为空, 否则报空指针
            if (!CollectionUtils.isEmpty(properties)) {
                thingsList.addAll(convertJsonToThingsList(properties, thingsModelValueItems, 1));
            }
            if (!CollectionUtils.isEmpty(functions)) {
                thingsList.addAll(convertJsonToThingsList(functions, thingsModelValueItems, 2));
            }
            deviceShortOutput.setThingsModels(thingsList);
            Map<String,Object> uriEntity = new HashMap<>();
            uriEntity.put("uri","/iot"+"/iot/platform/device/service/invoke");
            uriEntity.put("method","POST");
            deviceShortOutput.setUriEntity(uriEntity);
        });
        return list;
    }

    /**
     * 查询所有功能
     */
    @Override
    public List<ThingsModel> selectAll(ThingsModelBo thingsModel) {
        LambdaQueryWrapper<ThingsModel> thingsModelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!Objects.isNull(thingsModel)) {
            thingsModelLambdaQueryWrapper.like(ObjUtil.isNotEmpty(thingsModel.getModelId()),ThingsModel::getModelId, thingsModel.getModelId());
            thingsModelLambdaQueryWrapper.like(StrUtil.isNotBlank(thingsModel.getModelName()),ThingsModel::getModelName, thingsModel.getModelName());
            thingsModelLambdaQueryWrapper.like(ObjUtil.isNotEmpty(thingsModel.getProductId()),ThingsModel::getProductId, thingsModel.getProductId());
            thingsModelLambdaQueryWrapper.like(StrUtil.isNotBlank(thingsModel.getProductName()),ThingsModel::getProductName, thingsModel.getProductName());
            thingsModelLambdaQueryWrapper.like(StrUtil.isNotBlank(thingsModel.getIdentifier()),ThingsModel::getIdentifier, thingsModel.getIdentifier());
            thingsModelLambdaQueryWrapper.like(ObjUtil.isNotEmpty(thingsModel.getType()),ThingsModel::getType, thingsModel.getType());
        }
        if (Objects.isNull(thingsModel) || ObjUtil.isNull(thingsModel.getType())) {
            thingsModelLambdaQueryWrapper.eq(ThingsModel::getType, "2");
        }
        thingsModelLambdaQueryWrapper.select(ThingsModel::getProductId, ThingsModel::getProductName, ThingsModel::getModelName, ThingsModel::getIdentifier);
        List<ThingsModel> thingsModels = thingsModelMapper.selectList(thingsModelLambdaQueryWrapper);
        return thingsModels;
    }

    /**
     * 查询所有设备
     * @param device
     * @return
     */
    @Override
    public List<Device> deviceSelectList(DeviceBo device) {
        QueryWrapper<Device> wrapper = Wrappers.query();
        if (!Objects.isNull(device)) {
            wrapper.like(StrUtil.isNotBlank(device.getDeviceName()),"d.device_name", device.getDeviceName());
            wrapper.eq(!Objects.isNull(device.getProductId()),"d.product_id", device.getProductId());
        }
        List<Device> devices = socialDeviceMapper.selectDeviceList(wrapper);
        //设备功能查询
        for (Device deviceThingsModel : devices) {
            LambdaQueryWrapper<ThingsModel> thingsModelLambdaQueryWrapper = new LambdaQueryWrapper<>();
            thingsModelLambdaQueryWrapper.eq(ThingsModel::getProductId, deviceThingsModel.getProductId());
            thingsModelLambdaQueryWrapper.eq(ThingsModel::getType, "2");
            thingsModelLambdaQueryWrapper.select(ThingsModel::getProductId, ThingsModel::getProductName, ThingsModel::getModelName, ThingsModel::getIdentifier);
            List<ThingsModel> thingsModels = thingsModelMapper.selectList(thingsModelLambdaQueryWrapper);
            deviceThingsModel.setThingsModelList(thingsModels);
        }
        return devices;
    }


    /**
     * 查询设备
     *
     * @param deviceId 设备主键
     * @return 设备
     */
    // TODO 22--slaveId
    @Override
    public DeviceShortOutput selectDeviceRunningStatusByDeviceId(Long deviceId) {
        DeviceShortOutput device = deviceMapper.selectDeviceRunningStatusByDeviceId(deviceId);
        JSONObject thingsModelObject = JSONObject.parseObject(itslCache.getCacheThingsModelByProductId(device.getProductId()));
        JSONArray properties = thingsModelObject.getJSONArray("properties");
        JSONArray functions = thingsModelObject.getJSONArray("functions");
        List<ValueItem> thingsModelValueItems = itslValueCache.getCacheDeviceStatus(device.getProductId(), device.getSerialNumber());
        // 物模型转换赋值
        List<ThingsModelValueItem> thingsList = new ArrayList<>();
        //判断一下properties 和 functions是否为空, 否则报空指针
        if (!CollectionUtils.isEmpty(properties)) {
            thingsList.addAll(convertJsonToThingsList(properties, thingsModelValueItems, 1));
        }
        if (!CollectionUtils.isEmpty(functions)) {
            thingsList.addAll(convertJsonToThingsList(functions, thingsModelValueItems, 2));
        }
        device.setThingsModels(thingsList);
        return device;

    }

    /**
     * 物模型基本类型转换赋值
     *
     * @param jsonArray
     * @param thingsModelValues
     * @param type
     * @return
     */
    @Async
    public List<ThingsModelValueItem> convertJsonToThingsList(JSONArray jsonArray, List<ValueItem> thingsModelValues, Integer type) {
        List<ThingsModelValueItem> thingsModelList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            ThingsModelValueItem thingsModel = new ThingsModelValueItem();
            JSONObject thingsJson = jsonArray.getJSONObject(i);
            JSONObject datatypeJson = thingsJson.getJSONObject("datatype");
            thingsModel.setId(thingsJson.getString("id"));
            thingsModel.setName(thingsJson.getString("name"));
            thingsModel.setIsMonitor(thingsJson.getInteger("isMonitor") == null ? 0 : thingsJson.getInteger("isMonitor"));
            thingsModel.setIsReadonly(thingsJson.getInteger("isReadonly") == null ? 0 : thingsJson.getInteger("isReadonly"));
            thingsModel.setIsChart(thingsJson.getInteger("isChart") == null ? 0 : thingsJson.getInteger("isChart"));
            thingsModel.setIsSharePerm(thingsJson.getInteger("isSharePerm") == null ? 0 : thingsJson.getInteger("isSharePerm"));
            thingsModel.setIsHistory(thingsJson.getInteger("isHistory") == null ? 0 : thingsJson.getInteger("isHistory"));
            thingsModel.setIsApp(thingsJson.getInteger("isApp") == null ? 0 : thingsJson.getInteger("isApp"));
            thingsModel.setOrder(thingsJson.getInteger("order") == null ? 0 : thingsJson.getInteger("order"));
            thingsModel.setType(type);
            // 获取value
            for (ValueItem valueItem : thingsModelValues) {
                if (valueItem.getId().equals(thingsModel.getId())) {
                    thingsModel.setValue(valueItem.getValue());
                    thingsModel.setShadow(valueItem.getShadow());
                    thingsModel.setTs(valueItem.getTs());
                    break;
                }
            }
            // json转DataType(DataType赋值)
            Datatype dataType = convertJsonToDataType(datatypeJson, thingsModelValues, type, thingsModel.getId() + "_");
            thingsModel.setDatatype(dataType);
            if (JsonUtils.isJson(thingsModel.getValue())) {
                JSONObject jsonObject = JSONObject.parseObject(thingsModel.getValue());
                for (EnumItem enumItem : dataType.getEnumList()) {
                    ThingsModelValueItem model = new ThingsModelValueItem();
                    BeanUtils.copyProperties(thingsModel, model);
                    String val = jsonObject.getString(enumItem.getValue());
                    model.setValue(val);
                    model.setName(enumItem.getValue());
                    thingsModelList.add(model);
                }
            } else {
                // 物模型项添加到集合
                thingsModelList.add(thingsModel);
            }
        }
        return thingsModelList;
    }

    /**
     * 物模型DataType转换
     *
     * @param datatypeJson
     * @param thingsModelValues
     * @param type
     * @param parentIdentifier  上级标识符
     * @return
     */
    private Datatype convertJsonToDataType(JSONObject datatypeJson, List<ValueItem> thingsModelValues, Integer type, String parentIdentifier) {
        Datatype dataType = new Datatype();
        //有些物模型数据定义为空的情况兼容
        if (datatypeJson == null) {
            return dataType;
        }
        dataType.setType(datatypeJson.getString("type"));
        if (dataType.getType().equals("decimal")) {
            dataType.setMax(datatypeJson.getBigDecimal("max"));
            dataType.setMin(datatypeJson.getBigDecimal("min"));
            dataType.setStep(datatypeJson.getBigDecimal("step"));
            dataType.setUnit(datatypeJson.getString("unit"));
        } else if (dataType.getType().equals("integer")) {
            dataType.setMax(datatypeJson.getBigDecimal("max"));
            dataType.setMin(datatypeJson.getBigDecimal("min"));
            dataType.setStep(datatypeJson.getBigDecimal("step"));
            dataType.setUnit(datatypeJson.getString("unit"));
        } else if (dataType.getType().equals("bool")) {
            dataType.setFalseText(datatypeJson.getString("falseText"));
            dataType.setTrueText(datatypeJson.getString("trueText"));
        } else if (dataType.getType().equals("string")) {
            dataType.setMaxLength(datatypeJson.getInteger("maxLength"));
        } else if (dataType.getType().equals("enum")) {
            List<EnumItem> enumItemList = JSON.parseArray(datatypeJson.getString("enumList"), EnumItem.class);
            dataType.setEnumList(enumItemList);
            dataType.setShowWay(datatypeJson.getString("showWay"));
        } else if (dataType.getType().equals("object")) {
            JSONArray jsonArray = JSON.parseArray(datatypeJson.getString("params"));
            // 物模型值过滤（parentId_开头）
            thingsModelValues = thingsModelValues.stream().filter(x -> x.getId().startsWith(parentIdentifier)).collect(Collectors.toList());
            List<ThingsModelValueItem> thingsList = convertJsonToThingsList(jsonArray, thingsModelValues, type);
            // 排序
            thingsList = thingsList.stream().sorted(Comparator.comparing(ThingsModelValueItem::getOrder).reversed()).collect(Collectors.toList());
            dataType.setParams(thingsList);
        } else if (dataType.getType().equals("array")) {
            dataType.setArrayType(datatypeJson.getString("arrayType"));
            dataType.setArrayCount(datatypeJson.getInteger("arrayCount"));
            if ("object".equals(dataType.getArrayType())) {
                // 对象数组
                JSONArray jsonArray = datatypeJson.getJSONArray("params");
                // 物模型值过滤（parentId_开头）
                thingsModelValues = thingsModelValues.stream().filter(x -> x.getId().startsWith(parentIdentifier)).collect(Collectors.toList());
                List<ThingsModelValueItem> thingsList = convertJsonToThingsList(jsonArray, thingsModelValues, type);
                // 排序
                thingsList = thingsList.stream().sorted(Comparator.comparing(ThingsModelValueItem::getOrder).reversed()).collect(Collectors.toList());
                // 数组类型物模型里面对象赋值
                List<com.fastbee.iot.model.ThingsModelItem.ThingsModel>[] arrayParams = new List[dataType.getArrayCount()];
                for (int i = 0; i < dataType.getArrayCount(); i++) {
                    List<com.fastbee.iot.model.ThingsModelItem.ThingsModel> thingsModels = new ArrayList<>();
                    for (int j = 0; j < thingsList.size(); j++) {
                        com.fastbee.iot.model.ThingsModelItem.ThingsModel thingsModel = new com.fastbee.iot.model.ThingsModelItem.ThingsModel();
                        BeanUtils.copyProperties(thingsList.get(j), thingsModel);
                        String shadow = thingsList.get(j).getShadow();
                        if (StringUtils.isNotEmpty(shadow) && !shadow.equals("")) {
                            String[] shadows = shadow.split(",");
                            if (i + 1 > shadows.length) {
                                // 解决产品取消发布，增加数组长度导致设备影子和值赋值失败
                                thingsModel.setShadow(" ");
                            } else {
                                thingsModel.setShadow(shadows[i]);
                            }
                        }
                        String value = thingsList.get(j).getValue();
                        if (StringUtils.isNotEmpty(value) && !value.equals("")) {
                            String[] values = value.split(",");
                            if (i + 1 > values.length) {
                                thingsModel.setValue(" ");
                            } else {
                                thingsModel.setValue(values[i]);
                            }
                        }
                        thingsModels.add(thingsModel);
                    }
                    arrayParams[i] = thingsModels;
                }
                dataType.setArrayParams(arrayParams);
            }
        }
        return dataType;
    }

}
