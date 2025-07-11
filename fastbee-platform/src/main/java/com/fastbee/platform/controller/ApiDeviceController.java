package com.fastbee.platform.controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.bean.BeanUtil;
import com.fastbee.common.exception.base.BaseException;
import com.fastbee.common.utils.SecurityUtils;
import com.fastbee.iot.domain.Device;
import com.fastbee.iot.domain.Product;
import com.fastbee.iot.service.IDeviceService;
import com.fastbee.iot.service.IProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fastbee.common.core.controller.BaseController;
import com.fastbee.common.core.domain.AjaxResult;
import com.fastbee.common.utils.poi.ExcelUtil;
import com.fastbee.platform.domain.ApiDevice;
import com.fastbee.platform.service.IApiDeviceService;
import com.fastbee.common.core.page.TableDataInfo;

/**
 * 平台接入设备中间Controller
 *
 * @author lwb
 * @date 2025-04-27
 */
@RestController
@RequestMapping("/api/device")
@Api(tags = "平台接入设备中间")
public class ApiDeviceController extends BaseController {
    @Autowired
    private IApiDeviceService apiDeviceService;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IProductService productService;
    /**
     * 查询平台接入设备中间列表
     */
    @PreAuthorize("@ss.hasPermi('iot:device:list')")
    @GetMapping("/list")
    @ApiOperation("查询平台接入设备中间列表")
    public TableDataInfo list(ApiDevice apiDevice) {
        startPage();
        List<ApiDevice> list = apiDeviceService.selectApiDeviceList(apiDevice);
        return getDataTable(list);
    }

    /**
     * 导出平台接入设备中间列表
     */
    @ApiOperation("导出平台接入设备中间列表")
    @PreAuthorize("@ss.hasPermi('iot:device:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, ApiDevice apiDevice) {
        List<ApiDevice> list = apiDeviceService.selectApiDeviceList(apiDevice);
        ExcelUtil<ApiDevice> util = new ExcelUtil<ApiDevice>(ApiDevice. class);
        util.exportExcel(response, list, "平台接入设备中间数据");
    }

    /**
     * 获取平台接入设备中间详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:device:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取平台接入设备中间详细信息")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return success(apiDeviceService.queryByIdWithCache(id));
    }

    /**
     * 新增平台接入设备中间
     */
    @PreAuthorize("@ss.hasPermi('iot:device:add')")
    @PostMapping
    @ApiOperation("新增平台接入设备中间")
    public AjaxResult add(@RequestBody ApiDevice apiDevice) {
        return toAjax(apiDeviceService.insertWithCache(apiDevice));
    }

    /**
     * 修改平台接入设备中间
     */
    @PreAuthorize("@ss.hasPermi('iot:device:edit')")
    @PutMapping
    @ApiOperation("修改平台接入设备中间")
    public AjaxResult edit(@RequestBody ApiDevice apiDevice) {
        return toAjax(apiDeviceService.updateWithCache(apiDevice));
    }

    /**
     * 删除平台接入设备中间
     */
    @PreAuthorize("@ss.hasPermi('iot:device:remove')")
    @DeleteMapping("/{ids}")
    @ApiOperation("删除平台接入设备中间")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(apiDeviceService.deleteWithCacheByIds(ids, true));
    }

    /**
     * 同步平台接入设备信息到设备表
     */
    @PreAuthorize("@ss.hasPermi('iot:device:sync')")
    @PostMapping("/device/insert")
    @ApiOperation("同步平台接入设备信息到设备表")
    public AjaxResult deviceInsert(@RequestBody List<ApiDevice> deviceList) {
        Product product = productService.selectProductByProductId(deviceList.get(0).getProductId());
        if (product == null) {
            throw new BaseException("产品不存在！");
        }
        for (ApiDevice apiDevice : deviceList) {
            apiDevice.setProductId(product.getProductId());
            apiDevice.setProductName(product.getProductName());
            apiDevice.setProtocolCode(product.getProtocolCode());
            apiDevice.setDeviceType(product.getDeviceType());
            apiDevice.setLocationWay(1);
            apiDevice.setStatus(1);
            apiDevice.setIsSimulate(0);
            apiDevice.setIsShadow(0);
            apiDevice.setIsBinding(1);
            apiDevice.setFirmwareVersion(BigDecimal.valueOf(1L));
            apiDevice.setTenantId(SecurityUtils.getUserId());
            apiDevice.setTenantName(SecurityUtils.getUsername());
        }
        List<Device> devices = BeanUtil.copyToList(deviceList, Device.class);
        try {
            deviceService.saveOrUpdateBatch(devices);
            apiDeviceService.saveOrUpdateBatch(deviceList);
        } catch (BaseException e) {
            // 检查是否是唯一约束冲突
            System.out.println("错误：该设备已存在，序列号：" + e.getMessage());
        }
        return toAjax(true);
    }

}
