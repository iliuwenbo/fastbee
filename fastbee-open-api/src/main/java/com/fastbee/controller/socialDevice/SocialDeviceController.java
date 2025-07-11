package com.fastbee.controller.socialDevice;

import com.alibaba.fastjson2.JSONObject;
import com.fastbee.common.constant.HttpStatus;
import com.fastbee.common.core.controller.BaseController;
import com.fastbee.common.core.domain.AjaxResult;
import com.fastbee.common.core.mq.InvokeReqDto;
import com.fastbee.common.core.mq.SocialInvokeReqDto;
import com.fastbee.common.core.page.TableDataInfo;
import com.fastbee.common.exception.base.BaseException;
import com.fastbee.common.utils.MessageUtils;
import com.fastbee.common.utils.SecurityUtils;
import com.fastbee.iot.data.service.IFunctionInvoke;
import com.fastbee.iot.domain.Device;
import com.fastbee.iot.domain.ThingsModel;
import com.fastbee.iot.domain.bo.DeviceBo;
import com.fastbee.iot.domain.bo.ThingsModelBo;
import com.fastbee.iot.model.SocialDeviceShortOutput;
import com.fastbee.iot.model.ThingsModels.ThingsModelValueItem;
import com.fastbee.iot.service.ISocialDeviceService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备Controller
 *
 * @author kerwincui
 * @date 2021-12-16
 */
@Api(tags = "第三方接入设备管理")
@RestController
@RequestMapping("/iot/platform/device")
public class SocialDeviceController extends BaseController
{
    @Autowired
    private ISocialDeviceService deviceService;
    @Autowired
    private IFunctionInvoke functionInvoke;


    /**
     * 查询设备简短列表，主页列表数据
     */
    @PreAuthorize("@ss.hasPermi('iot:device:list')")
    @GetMapping("/shortList")
    @ApiOperation("设备分页简短列表")
    public Map<String,Object> shortList(Device device)
    {
        startPage();
        List<SocialDeviceShortOutput> deviceShortOutputs = deviceService.selectDeviceShortList(device);
        Map<String,Object> rspData = new HashMap<>();
        rspData.put("code",HttpStatus.SUCCESS);
        rspData.put("msg",MessageUtils.message("query.success"));
        rspData.put("list",deviceShortOutputs);
        rspData.put("total",new PageInfo(deviceShortOutputs).getTotal());
        return rspData;
    }

    @PostMapping("/service/invoke")
    @PreAuthorize("@ss.hasPermi('iot:service:invoke')")
    @ApiOperation(value = "服务下发", httpMethod = "POST", response = AjaxResult.class, notes = "服务下发")
    public AjaxResult invoke(@Valid @RequestBody SocialInvokeReqDto reqDto) {
        Device device = new Device();
        device.setSerialNumber(reqDto.getSerialNumber());
        List<SocialDeviceShortOutput> deviceShortOutputs = deviceService.selectDeviceShortList(device);
        if (deviceShortOutputs.isEmpty()) {
            throw new BaseException("设备不存在！");
        }
        SocialDeviceShortOutput socialDeviceShortOutput = deviceShortOutputs.get(0);
        InvokeReqDto invokeReqDto = new InvokeReqDto();
        Long userId = SecurityUtils.getLoginUser().getUserId();
        invokeReqDto.setSerialNumber(reqDto.getSerialNumber());
        invokeReqDto.setParams(new JSONObject(reqDto.getRemoteCommand()));
        invokeReqDto.setUserId(userId);
        invokeReqDto.setProductId(socialDeviceShortOutput.getProductId());
        invokeReqDto.setIsShadow(socialDeviceShortOutput.getIsShadow()==0?"true":"false");
        List<ThingsModelValueItem> thingsModels = socialDeviceShortOutput.getThingsModels();
        String firstKey = reqDto.getRemoteCommand().keySet().iterator().next();
        for (ThingsModelValueItem thingsModel : thingsModels) {
            if (thingsModel.getId().equals(firstKey)) {
                invokeReqDto.setModelName(thingsModel.getName());
                invokeReqDto.setIdentifier(firstKey);
                invokeReqDto.setType(thingsModel.getType());
            }
        }
        return functionInvoke.invokeNoReply(invokeReqDto);
    }


    /**
     * 查询所有功能
     */
    @PreAuthorize("@ss.hasPermi('iot:device:list')")
    @GetMapping("/function/selectAll")
    @ApiOperation("查询所有功能")
    public AjaxResult thingsModelShortList(ThingsModelBo thingsModel)
    {
        List<ThingsModel> thingsModelShortList = deviceService.selectAll(thingsModel);
        return success(thingsModelShortList);
    }

    /**
     * 查询所有设备
     */
    @PreAuthorize("@ss.hasPermi('iot:device:list')")
    @GetMapping("/device/list")
    @ApiOperation("查询所有设备")
    public TableDataInfo deviceSelectList(DeviceBo device)
    {
        startPage();
        List<Device> devices = deviceService.deviceSelectList(device);
        return getDataTable(devices);
    }

    /**
     * 获取设备运行状态
     */
    @PreAuthorize("@ss.hasPermi('iot:device:query')")
    @GetMapping(value = "/runningStatus")
    @ApiOperation("获取设备详情和运行状态")
    public AjaxResult getRunningStatusInfo(Long deviceId)
    {
        return AjaxResult.success(deviceService.selectDeviceRunningStatusByDeviceId(deviceId));
    }

}
