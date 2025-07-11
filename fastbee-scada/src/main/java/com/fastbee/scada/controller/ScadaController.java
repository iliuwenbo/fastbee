package com.fastbee.scada.controller;

import com.alibaba.fastjson2.JSON;
import com.fastbee.common.annotation.Anonymous;
import com.fastbee.common.annotation.Log;
import com.fastbee.common.config.RuoYiConfig;
import com.fastbee.common.constant.HttpStatus;
import com.fastbee.common.core.controller.BaseController;
import com.fastbee.common.core.domain.AjaxResult;
import com.fastbee.common.core.page.TableDataInfo;
import com.fastbee.common.enums.BusinessType;
import com.fastbee.common.exception.ServiceException;
import com.fastbee.common.exception.file.InvalidExtensionException;
import com.fastbee.common.utils.StringUtils;
import com.fastbee.common.utils.file.MimeTypeUtils;
import com.fastbee.common.utils.poi.ExcelUtil;
import com.fastbee.iot.domain.Device;
import com.fastbee.iot.model.DeviceShortOutput;
import com.fastbee.iot.model.ThingsModelItem.Datatype;
import com.fastbee.iot.model.ThingsModelItem.ThingsModel;
import com.fastbee.iot.model.ThingsModels.ThingsModelValueItem;
import com.fastbee.iot.service.IDeviceService;
import com.fastbee.scada.domain.Scada;
import com.fastbee.scada.domain.ScadaDeviceBind;
import com.fastbee.scada.domain.ScadaGallery;
import com.fastbee.scada.service.IScadaDeviceBindService;
import com.fastbee.scada.service.IScadaService;
import com.fastbee.scada.utils.ScadaCollectionUtils;
import com.fastbee.scada.utils.ScadaFileUploadUtils;
import com.fastbee.scada.utils.ScadaFileUtils;
import com.fastbee.scada.vo.DeviceRealDataVO;
import com.fastbee.scada.vo.FavoritesVO;
import com.fastbee.scada.vo.ScadaDeviceBindDTO;
import com.fastbee.scada.vo.ThingsModelHistoryParam;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 组态中心Controller
 *
 * @author kerwincui
 * @date 2023-11-10
 */
@Anonymous
@Api(tags = "组态中心")
@RestController
@RequestMapping("/scada/center")
public class ScadaController extends BaseController
{
    @Resource
    private IScadaService scadaService;

    @Resource
    private IScadaDeviceBindService scadaDeviceBindService;

    @Resource
    private IDeviceService deviceService;

    /**
     * 查询组态中心列表
     */
    @ApiOperation("查询组态中心列表")
    @PreAuthorize("@ss.hasPermi('scada:center:list')")
    @GetMapping("/list")
    public TableDataInfo list(Scada scada)
    {
        startPage();
        List<Scada> list = scadaService.selectScadaList(scada);
        return getDataTable(list);
    }

    /**
     * 导出组态中心列表
     */
    @ApiOperation("导出组态中心列表")
    @PreAuthorize("@ss.hasPermi('scada:center:export')")
    @Log(title = "组态中心", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Scada scada)
    {
        List<Scada> list = scadaService.selectScadaList(scada);
        ExcelUtil<Scada> util = new ExcelUtil<Scada>(Scada.class);
        util.exportExcel(response, list, "组态中心数据");
    }

    /**
     * 获取组态中心详细信息
     */
    @ApiOperation("查询组态详细信息")
    @PreAuthorize("@ss.hasPermi('scada:center:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(scadaService.selectScadaById(id));
    }

    /**
     * 新增组态中心
     */
    @ApiOperation("新增组态中心")
    @PreAuthorize("@ss.hasPermi('scada:center:add')")
    @Log(title = "组态中心", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Scada scada)
    {
        return scadaService.insertScada(scada);
    }

    /**
     * 修改组态中心
     */
    @ApiOperation("修改组态中心")
    @PreAuthorize("@ss.hasPermi('scada:center:edit')")
    @Log(title = "组态中心", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Scada scada)
    {
        return toAjax(scadaService.updateScada(scada));
    }

    /**
     * 删除组态中心
     */
    @ApiOperation("批量删除组态中心")
    @PreAuthorize("@ss.hasPermi('scada:center:remove')")
    @Log(title = "组态中心", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(scadaService.deleteScadaByIds(ids));
    }

    /**
     * 根据guid获取组态详情
     * @param guid 组态id
     * @return
     */
    @ApiOperation("根据guid获取组态详情")
    @PreAuthorize("@ss.hasPermi('scada:center:query')")
    @GetMapping(value = "/getByGuid")
    public AjaxResult getByGuid(String guid) {
        Scada scada = scadaService.selectScadaByGuid(guid);
        return AjaxResult.success(scada);
    }

    /**
     * 保存组态信息
     */
    @ApiOperation("保存组态信息")
    @Log(title = "组态中心", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('scada:center:edit')")
    @PostMapping("/save")
    public AjaxResult save(@RequestBody Scada scada)
    {
        if (StringUtils.isEmpty(scada.getGuid())) {
            return AjaxResult.error("guid不能为空");
        }
        Scada scadaQuery = new Scada();
        scadaQuery.setGuid(scada.getGuid());
        List<Scada> scadaList = scadaService.selectScadaList(scadaQuery);
        if (StringUtils.isNotEmpty(scada.getBase64())) {
            MultipartFile multipartFile = ScadaFileUtils.base64toMultipartFile(scada.getBase64());
            String url;
            try {
                url = ScadaFileUploadUtils.upload(RuoYiConfig.getUploadPath(), multipartFile, MimeTypeUtils.IMAGE_EXTENSION);
            } catch (IOException | InvalidExtensionException e) {
                throw new ServiceException("修改组态base64转图片异常" + e.getMessage());
            }
            scada.setPageImage(url);
        }
        if (CollectionUtils.isNotEmpty(scadaList)) {
            Scada updateScada = scadaList.get(0);
            updateScada.setScadaData(scada.getScadaData());
            updateScada.setPageImage(scada.getPageImage());
            scadaService.updateScada(updateScada);
        } else {
            scadaService.insertScada(scada);
//            scadaDeviceBindService.insertScadaDeviceBind()
        }
        return AjaxResult.success();
    }

    /**
     * 获取组态绑定的设备数，一个组态可以绑定多个设备，用于多个设备的参数绑定
     * @param scadaDeviceBind 组态guid
     * @return
     */
    @ApiOperation("获取组态绑定的设备列表")
    @PreAuthorize("@ss.hasPermi('scada:center:query')")
    @GetMapping(value = "/listDeviceBind")
    public TableDataInfo listDeviceBind(ScadaDeviceBind scadaDeviceBind) {
        startPage();
        List<ScadaDeviceBind> list = scadaDeviceBindService.selectScadaDeviceBindList(scadaDeviceBind);
//        List<DeviceAllShortOutput> deviceAllShortOutputs = deviceService.selectAllDeviceShortList();
//        Map<String, String> collect = deviceAllShortOutputs.stream().collect(Collectors.toMap(DeviceAllShortOutput::getSerialNumber, DeviceAllShortOutput::getDeviceName));
        for (ScadaDeviceBind deviceZtBind : list) {
            Device device = deviceService.selectShortDeviceBySerialNumber(deviceZtBind.getSerialNumber());
            if (device != null) {
                deviceZtBind.setDeviceName(device.getDeviceName());
                deviceZtBind.setStatus(device.getStatus());
            }
        }
        return getDataTable(list);
    }

    /**
     * 保存组态关联设备
     * @param scadaDeviceBindDTO 组态关联设备
     * @return
     */
    @ApiOperation("保存组态关联设备")
    @PreAuthorize("@ss.hasPermi('scada:center:edit')")
    @PostMapping("/saveDeviceBind")
    public AjaxResult saveDeviceBind(@RequestBody ScadaDeviceBindDTO scadaDeviceBindDTO)
    {
        if (StringUtils.isEmpty(scadaDeviceBindDTO.getScadaGuid()) || StringUtils.isEmpty(scadaDeviceBindDTO.getSerialNumbers())) {
            return error("请选择设备");
        }
        List<String> addSerialNumberList = StringUtils.str2List(scadaDeviceBindDTO.getSerialNumbers(), ",", true, true);
        List<ScadaDeviceBind> scadaDeviceBindList = scadaDeviceBindService.listByGuidAndSerialNumber(scadaDeviceBindDTO.getScadaGuid(), addSerialNumberList);
        List<String> oldSerialNumberList = scadaDeviceBindList.stream().map(ScadaDeviceBind::getSerialNumber).collect(Collectors.toList());
        for (String serialNumber : addSerialNumberList) {
            if (oldSerialNumberList.contains(serialNumber)) {
                continue;
            }
            ScadaDeviceBind scadaDeviceBind = new ScadaDeviceBind();
            scadaDeviceBind.setScadaGuid(scadaDeviceBindDTO.getScadaGuid());
            scadaDeviceBind.setSerialNumber(serialNumber);
            scadaDeviceBindService.insertScadaDeviceBind(scadaDeviceBind);
        }
        return success();
    }

    /**
     * 删除组态设备关联
     */
    @ApiOperation("删除组态设备关联")
    @PreAuthorize("@ss.hasPermi('scada:center:edit')")
    @DeleteMapping("/removeDeviceBind/{ids}")
    public AjaxResult removeDeviceBind(@PathVariable Long[] ids)
    {
        return toAjax(scadaDeviceBindService.deleteScadaDeviceBindByIds(ids));
    }

    /**
     * 获取绑定设备物模型数据，用于绑定变量
     * @param scadaGuid 组态guid
     * @return
     */
    @ApiOperation("获取绑定设备物模型列表")
    @PreAuthorize("@ss.hasPermi('scada:center:query')")
    @GetMapping("/listDeviceThingsModel")
    public TableDataInfo getBindDatalist(Integer pageNum, Integer pageSize, String scadaGuid, String serialNumber)
    {
        List<DeviceRealDataVO> list= new ArrayList<>();
        if(StringUtils.isEmpty(scadaGuid)){
            return getDataTable(list);
        }
        ScadaDeviceBind scadaDeviceBind = new ScadaDeviceBind();
        scadaDeviceBind.setScadaGuid(scadaGuid);
        scadaDeviceBind.setSerialNumber(serialNumber);
        // 查询到组态绑定的设备
        List<ScadaDeviceBind> deviceBindList = scadaDeviceBindService.selectScadaDeviceBindList(scadaDeviceBind);
        List<String> serialNumberList = deviceBindList.stream().map(ScadaDeviceBind::getSerialNumber).collect(Collectors.toList());
        // 查询设备信息
        for (String bindSerialNumber : serialNumberList) {
            Device device = deviceService.selectDeviceNoModel(bindSerialNumber);
            if (device == null) {
                continue;
            }
            DeviceShortOutput deviceShortOutput = deviceService.selectDeviceRunningStatusByDeviceId(device.getDeviceId());
            if (CollectionUtils.isEmpty(deviceShortOutput.getThingsModels())) {
                continue;
            }
            List<ThingsModelValueItem> thingsModelList = deviceShortOutput.getThingsModels();
            for (ThingsModelValueItem thingsModel : thingsModelList) {
                Datatype datatype = thingsModel.getDatatype();
                if ("array".equals(datatype.getType()) && "object".equals(datatype.getArrayType())) {
                    List<ThingsModel>[] arrayParams = datatype.getArrayParams();
                    for (int a = 0; a < arrayParams.length; a++) {
                        for (int i = 0; i < arrayParams[a].size(); i++) {
                            ThingsModel thingsModel1 = arrayParams[a].get(i);
                            DeviceRealDataVO deviceRealDataVO = new DeviceRealDataVO();
                            deviceRealDataVO.setProductId(device.getProductId());
                            deviceRealDataVO.setSerialNumber(bindSerialNumber);
                            deviceRealDataVO.setDeviceName(device.getDeviceName());
                            deviceRealDataVO.setStatus(device.getStatus());
                            if (i < 10) {
                                deviceRealDataVO.setIdentifier("array_0" + a + "_" + thingsModel1.getId());
                            } else {
                                deviceRealDataVO.setIdentifier("array_" + a + "_" + thingsModel1.getId());
                            }
                            deviceRealDataVO.setModelName(thingsModel.getName() + (a + 1) + "_" + thingsModel1.getName());
                            deviceRealDataVO.setUnit(thingsModel1.getDatatype().getUnit());
                            deviceRealDataVO.setType(thingsModel1.getType());
                            list.add(deviceRealDataVO);
                        }
                    }
                } else if ("array".equals(datatype.getType())) {
                    for (int i = 0; i < datatype.getArrayCount(); i++) {
                        DeviceRealDataVO deviceRealDataVO = new DeviceRealDataVO();
                        deviceRealDataVO.setProductId(device.getProductId());
                        deviceRealDataVO.setSerialNumber(bindSerialNumber);
                        deviceRealDataVO.setDeviceName(device.getDeviceName());
                        deviceRealDataVO.setStatus(device.getStatus());
                        if (i < 10) {
                            deviceRealDataVO.setIdentifier("array_0" + i + "_" + thingsModel.getId());
                        } else {
                            deviceRealDataVO.setIdentifier("array_" + i + "_" + thingsModel.getId());
                        }
                        deviceRealDataVO.setModelName(thingsModel.getName() + (i+1));
                        deviceRealDataVO.setUnit(datatype.getUnit());
                        deviceRealDataVO.setType(thingsModel.getType());
                        list.add(deviceRealDataVO);
                    }
                } else if ("object".equals(datatype.getType())) {
                    for (ThingsModelValueItem objectThingsModel : datatype.getParams()) {
                        DeviceRealDataVO deviceRealDataVO = new DeviceRealDataVO();
                        deviceRealDataVO.setProductId(device.getProductId());
                        deviceRealDataVO.setSerialNumber(bindSerialNumber);
                        deviceRealDataVO.setDeviceName(device.getDeviceName());
                        deviceRealDataVO.setStatus(device.getStatus());
                        deviceRealDataVO.setIdentifier(objectThingsModel.getId());
                        deviceRealDataVO.setModelName(thingsModel.getName() + "_" + objectThingsModel.getName());
                        deviceRealDataVO.setUnit(objectThingsModel.getDatatype().getUnit());
                        deviceRealDataVO.setType(thingsModel.getType());
                        list.add(deviceRealDataVO);
                    }
                } else {
                    DeviceRealDataVO deviceRealDataVO = new DeviceRealDataVO();
                    deviceRealDataVO.setProductId(device.getProductId());
                    deviceRealDataVO.setSerialNumber(bindSerialNumber);
                    deviceRealDataVO.setDeviceName(device.getDeviceName());
                    deviceRealDataVO.setStatus(device.getStatus());
                    deviceRealDataVO.setIdentifier(thingsModel.getId());
                    deviceRealDataVO.setModelName(thingsModel.getName());
                    deviceRealDataVO.setUnit(thingsModel.getDatatype().getUnit());
                    deviceRealDataVO.setType(thingsModel.getType());
                    list.add(deviceRealDataVO);
                }
            }
        }
        List resultList = ScadaCollectionUtils.startPage(list, pageNum, pageSize);
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(resultList);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 导入json文件
     * @param file 文件
     * @param guid guid
     * @return
     * @throws IOException
     */
    @ApiOperation("组态页面导入json文件")
    @PreAuthorize("@ss.hasPermi('scada:center:add')")
    @PostMapping("/importJson")
    public AjaxResult importJson(MultipartFile file, String guid) throws IOException {
        InputStream inputStream = file.getInputStream();
        if(file.isEmpty()){
            return AjaxResult.error("无效的配置文件");
        }
        if(file.getOriginalFilename().indexOf("json")==-1){
            return AjaxResult.error("无效的配置文件");
        }
        Scada scada = new Scada();
        try {
            scada = JSON.parseObject(inputStream, Scada.class);
        }catch (Exception e){
            return AjaxResult.error("无效的配置文件");
        }finally {
            inputStream.close();
        }
        Scada oldScada = new Scada();
        if (StringUtils.isNotEmpty(guid)) {
            Scada queryScada = new Scada();
            queryScada.setGuid(guid);
            List<Scada> scadaList = scadaService.selectScadaList(queryScada);
            if (CollectionUtils.isNotEmpty(scadaList)) {
                oldScada = scadaList.get(0);
            }
        }
        if (oldScada == null) {
            guid= UUID.randomUUID().toString();
            scada.setGuid(guid);
            scadaService.insertScada(scada);
        } else {
            scada.setId(oldScada.getId());
            scadaService.updateScada(scada);
        }
        return AjaxResult.success("导入成功");
    }

    /**
     * 收藏图库
     * @param favoritesVO 图库收藏传参类
     * @return
     */
    @ApiOperation("个人收藏图库")
    @PostMapping("/saveGalleryFavorites")
    public AjaxResult saveGalleryFavorites(@RequestBody FavoritesVO favoritesVO) {
        return scadaService.saveGalleryFavorites(favoritesVO);
    }

    /**
     * 查询收藏图库列表
     * @param scadaGallery 图库类
     * @return
     */
    @ApiOperation("查询个人收藏图库列表")
    @GetMapping("/listGalleryFavorites")
    public TableDataInfo listGalleryFavorites(ScadaGallery scadaGallery)
    {
        startPage();
        List<ScadaGallery> list = scadaService.listGalleryFavorites(scadaGallery);
        return getDataTable(list);
    }

    /**
     * 删除收藏图库
     * @return
     */
    @ApiOperation("删除个人收藏图库")
    @DeleteMapping("/deleteGalleryFavorites/{ids}")
    public AjaxResult deleteGalleryFavorites(@PathVariable Long[] ids) {
        return scadaService.deleteGalleryFavorites(ids);
    }

    /**
     * 收藏上传图库
     * @return
     */
    @ApiOperation("个人收藏上传图库")
    @PostMapping("/uploadGalleryFavorites")
    public AjaxResult uploadGalleryFavorites(MultipartFile file, String categoryName) {
        return scadaService.uploadGalleryFavorites(file, categoryName);
    }

    /**
     * 查询变量历史数据
     * @param param 查询条件
     * @return com.fastbee.common.core.domain.AjaxResult
     */
    @ApiOperation("查询变量历史数据")
    @PostMapping("/listThingsModelHistory")
    public AjaxResult listThingsModelHistory(@RequestBody ThingsModelHistoryParam param) {
        return success(scadaService.listThingsModelHistory(param));
    }

    /**
     * 获取设备运行状态
     * @param serialNumber 设备编号
     * @return com.fastbee.common.core.domain.AjaxResult
     */
    @ApiOperation("获取设备运行状态")
    @GetMapping("/getDeviceStatus")
    public AjaxResult getDeviceStatus(String serialNumber) {
        return AjaxResult.success(scadaService.getStatusBySerialNumber(serialNumber));
    }

    /**
     * 获取系统相关统计信息
     * @return com.fastbee.common.core.domain.AjaxResult
     */
    @GetMapping(value = "/statistic")
    @ApiOperation("获取系统相关统计信息")
    public AjaxResult getDeviceStatistic()
    {
        return AjaxResult.success(scadaService.selectStatistic());
    }

}
