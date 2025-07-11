package com.fastbee.scada.service.impl;

import com.fastbee.common.config.RuoYiConfig;
import com.fastbee.common.core.domain.AjaxResult;
import com.fastbee.common.core.domain.entity.SysRole;
import com.fastbee.common.core.domain.entity.SysUser;
import com.fastbee.common.core.domain.model.LoginUser;
import com.fastbee.common.core.redis.RedisCache;
import com.fastbee.common.utils.DateUtils;
import com.fastbee.common.utils.StringUtils;
import com.fastbee.common.utils.bean.BeanUtils;
import com.fastbee.common.utils.file.FileUploadUtils;
import com.fastbee.iot.domain.DeviceLog;
import com.fastbee.iot.domain.EventLog;
import com.fastbee.iot.domain.FunctionLog;
import com.fastbee.iot.model.HistoryModel;
import com.fastbee.iot.service.IDeviceLogService;
import com.fastbee.scada.domain.Scada;
import com.fastbee.scada.domain.ScadaGallery;
import com.fastbee.scada.mapper.ScadaDeviceBindMapper;
import com.fastbee.scada.mapper.ScadaGalleryMapper;
import com.fastbee.scada.mapper.ScadaMapper;
import com.fastbee.scada.service.IScadaService;
import com.fastbee.scada.vo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.fastbee.common.utils.SecurityUtils.getLoginUser;

/**
 * 组态中心Service业务层处理
 *
 * @author kerwincui
 * @date 2023-11-10
 */
@Service
public class ScadaServiceImpl implements IScadaService
{
    @Resource
    private ScadaMapper scadaMapper;
    @Resource
    private ScadaGalleryMapper scadaGalleryMapper;
    @Resource
    private RedisCache redisCache;
    @Resource
    private ScadaDeviceBindMapper scadaDeviceBindMapper;
    @Resource
    private IDeviceLogService deviceLogService;

    /**
     * 查询组态中心
     *
     * @param id 组态中心主键
     * @return 组态中心
     */
    @Override
    public Scada selectScadaById(Long id)
    {
        return scadaMapper.selectScadaById(id);
    }

    /**
     * 查询组态中心列表
     *
     * @param scada 组态中心
     * @return 组态中心
     */
    @Override
    public List<Scada> selectScadaList(Scada scada)
    {
        return scadaMapper.selectScadaList(scada);
    }

    /**
     * 新增组态中心
     *
     * @param scada 组态中心
     * @return 结果
     */
    @Override
    public AjaxResult insertScada(Scada scada)
    {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return AjaxResult.error("请登录后重试！");
        }
        scada.setCreateBy(loginUser.getUserId().toString());
        UUID uuid = UUID.randomUUID();
        scada.setGuid(uuid.toString());
        return scadaMapper.insertScada(scada) > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 修改组态中心
     *
     * @param scada 组态中心
     * @return 结果
     */
    @Override
    public int updateScada(Scada scada)
    {
        scada.setUpdateTime(DateUtils.getNowDate());
        return scadaMapper.updateScada(scada);
    }

    /**
     * 批量删除组态中心
     *
     * @param ids 需要删除的组态中心主键
     * @return 结果
     */
    @Override
    public int deleteScadaByIds(Long[] ids)
    {
        return scadaMapper.deleteScadaByIds(ids);
    }

    /**
     * 删除组态中心信息
     *
     * @param id 组态中心主键
     * @return 结果
     */
    @Override
    public int deleteScadaById(Long id)
    {
        return scadaMapper.deleteScadaById(id);
    }

    /**
     * 根据guid获取组态详情
     * @param guid 组态id
     * @return
     */
    @Override
    public Scada selectScadaByGuid(String guid) {
        Scada scada = scadaMapper.selectScadaByGuid(guid);
        // 查询绑定设备
        List<ScadaBindDeviceSimVO> simVOList = scadaDeviceBindMapper.listDeviceSimByGuid(scada.getGuid());
        scada.setBindDeviceList(simVOList);
        return scada;
    }

    @Override
    public AjaxResult uploadGalleryFavorites(MultipartFile file, String categoryName) {
        LoginUser loginUser = getLoginUser();
        if (Objects.isNull(loginUser)) {
            return AjaxResult.error("请登录后重试！");
        }
        // 上传文件路径
        String filePath;
        // 上传并返回新文件名称
        try{
            filePath = FileUploadUtils.upload(RuoYiConfig.getUploadPath(), file);
        }catch (Exception e){
            return AjaxResult.error(500,"上传图库文件异常，"+ e);
        }
        String fileName = file.getOriginalFilename();
        ScadaGallery scadaGallery = new ScadaGallery();
        scadaGallery.setFileName(fileName);
        scadaGallery.setCategoryName(categoryName);
        scadaGallery.setResourceUrl(filePath);
        Long userId = loginUser.getUserId();
        scadaGallery.setTenantId(userId);
        scadaGallery.setTenantName(loginUser.getUsername());
        int i = scadaGalleryMapper.insertScadaGallery(scadaGallery);
        if (i <= 0) {
            return AjaxResult.error("上传失败，请重试！");
        }
        String key = this.getGalleryFavoritesRedisKey(userId);
        redisCache.sAdd(key, scadaGallery.getId());
        return AjaxResult.success();
    }

    private String getGalleryFavoritesRedisKey(Long userId) {
        return "scada:gallery_favorites_userId_" + userId;
    }

    @Override
    public AjaxResult saveGalleryFavorites(FavoritesVO favoritesVO) {
        LoginUser loginUser = getLoginUser();
        if (Objects.isNull(loginUser)) {
            return AjaxResult.error("请登录后重试！");
        }
        Long userId = loginUser.getUserId();
        List<String> idList = StringUtils.str2List(favoritesVO.getIdStr(), ",", true, true);
        String key = this.getGalleryFavoritesRedisKey(userId);
        for (String id : idList) {
            redisCache.sAdd(key, id);
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult deleteGalleryFavorites(Long[] ids) {
        LoginUser loginUser = getLoginUser();
        if (Objects.isNull(loginUser)) {
            return AjaxResult.error("请登录后重试！");
        }
        Long userId = loginUser.getUserId();
        String key = this.getGalleryFavoritesRedisKey(userId);
        for (Long id : ids) {
            redisCache.setRemove(key, id);
        }
        // 删除图库
        scadaGalleryMapper.deleteScadaGalleryByIds(ids);
        return AjaxResult.success();
    }

    @Override
    public List<ScadaGallery> listGalleryFavorites(ScadaGallery scadaGallery) {
        LoginUser loginUser = getLoginUser();
        if (Objects.isNull(loginUser)) {
            return new ArrayList<>();
        }
        Long userId = loginUser.getUserId();
        String key = this.getGalleryFavoritesRedisKey(userId);
        Set<Long> cacheSet = redisCache.getCacheSet(key);
        if (CollectionUtils.isEmpty(cacheSet)) {
            return new ArrayList<>();
        }
        return scadaGalleryMapper.selectScadaGalleryByIdSet(cacheSet);
    }

    @Override
    public Map<String, List<ScadaHistoryModelVO>> listThingsModelHistory(ThingsModelHistoryParam param) {
        String serialNumber = param.getSerialNumber();
        List<ThingsModelHistoryParam.ThingsModelSim> thingsModelList = param.getThingsModelList();
        if (CollectionUtils.isEmpty(thingsModelList)) {
            return new HashMap<>(2);
        }
        List<ScadaHistoryModelVO> historyModelList = new ArrayList<>();
        List<String> propertyIdentifierList = thingsModelList.stream().filter(t -> 1 == t.getType()).map(ThingsModelHistoryParam.ThingsModelSim::getIdentifier).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(propertyIdentifierList)) {
            DeviceLog deviceLog = new DeviceLog();
            deviceLog.setIdentityList(propertyIdentifierList);
            deviceLog.setSerialNumber(serialNumber);
            deviceLog.setBeginTime(param.getBeginTime());
            deviceLog.setEndTime(param.getEndTime());
            List<HistoryModel> historyModelList1 = deviceLogService.listHistory(deviceLog);
            for (HistoryModel historyModel : historyModelList1) {
                ScadaHistoryModelVO scadaHistoryModelVO = new ScadaHistoryModelVO();
                BeanUtils.copyProperties(historyModel, scadaHistoryModelVO);
                historyModelList.add(scadaHistoryModelVO);
            }
        }
        List<String> functionIdentifierList = thingsModelList.stream().filter(t -> 2 == t.getType()).map(ThingsModelHistoryParam.ThingsModelSim::getIdentifier).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(functionIdentifierList)) {
            FunctionLog functionLog = new FunctionLog();
            functionLog.setIdentifyList(functionIdentifierList);
            functionLog.setSerialNumber(serialNumber);
            functionLog.setBeginTime(DateUtils.dateTime(DateUtils.YY_MM_DD_HH_MM_SS, param.getBeginTime()));
            functionLog.setEndTime(DateUtils.dateTime(DateUtils.YY_MM_DD_HH_MM_SS, param.getEndTime()));
            historyModelList.addAll(scadaMapper.listFunctionLogHistory(functionLog));
        }
        List<String> eventIdentifierList = thingsModelList.stream().filter(t -> 3 == t.getType()).map(ThingsModelHistoryParam.ThingsModelSim::getIdentifier).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(eventIdentifierList)) {
            EventLog eventLog = new EventLog();
            eventLog.setIdentityList(eventIdentifierList);
            eventLog.setSerialNumber(serialNumber);
            Map<String, Object> params = new HashMap<>(2);
            params.put("beginTime", param.getBeginTime());
            params.put("endTime",param.getEndTime());
            eventLog.setParams(params);
            historyModelList.addAll(scadaMapper.listEventLogHistory(eventLog));
        }
        // 分组
        return historyModelList.stream().collect(Collectors.groupingBy(ScadaHistoryModelVO::getIdentity));
    }

    @Override
    public Integer getStatusBySerialNumber(String serialNumber) {
        return scadaMapper.getStatusBySerialNumber(serialNumber);
    }

    @Override
    public ScadaStatisticVO selectStatistic() {
        Long tenantId = null;
        Long userId = null;
        SysUser user = getLoginUser().getUser();
        List<SysRole> roles = user.getRoles();
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).getRoleKey().equals("tenant")) {
                // 租户查看产品下所有设备
                tenantId = user.getUserId();
            } else if (roles.get(i).getRoleKey().equals("general")) {
                // 用户查看自己设备
                userId = user.getUserId();
            }
        }
        // 获取设备、产品和告警数量
        ScadaStatisticVO statistic = scadaMapper.selectDeviceProductAlertCount(tenantId, userId);
        if (statistic == null) {
            statistic = new ScadaStatisticVO();
            return statistic;
        }
        return statistic;
    }
}
