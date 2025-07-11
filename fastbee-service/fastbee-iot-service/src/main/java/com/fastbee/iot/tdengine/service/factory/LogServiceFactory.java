package com.fastbee.iot.tdengine.service.factory;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
//import com.fastbee.framework.config.MyBatisConfig;
import com.fastbee.iot.mapper.EventLogMapper;
import com.fastbee.iot.mapper.FunctionLogMapper;
import com.fastbee.iot.tdengine.service.impl.MySqlLogServiceImpl;
import com.fastbee.iot.tdengine.service.impl.TdengineLogServiceImpl;
import com.fastbee.iot.tdengine.config.TDengineConfig;
import com.fastbee.iot.tdengine.service.ILogService;
import com.fastbee.iot.mapper.DeviceLogMapper;
import com.fastbee.iot.tdengine.dao.TDDeviceLogDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * 类名: DeviceLogServiceImpl
 * 时间: 2022/5/19,0019 18:09
 * 开发人: wxy
 */
@Component
public class LogServiceFactory {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public ILogService getLogService() {
        //先获取TDengine的配置，检测TDengine是否已经配置
        if (containBean(TDengineConfig.class)) {
            TDengineConfig tDengineConfig = applicationContext.getBean(TDengineConfig.class);
            TDDeviceLogDAO tDDeviceLogDAO = applicationContext.getBean(TDDeviceLogDAO.class);
            ILogService logService = new TdengineLogServiceImpl(tDengineConfig, tDDeviceLogDAO);
            return logService;
        } else if (containBean(MybatisPlusAutoConfiguration.class)) {
            //没有配置TDengine，那么使用MySQL的日志配置
            DeviceLogMapper deviceLogMapper = applicationContext.getBean(DeviceLogMapper.class);
            EventLogMapper eventLogMapper = applicationContext.getBean(EventLogMapper.class);
            FunctionLogMapper functionLogMapper = applicationContext.getBean(FunctionLogMapper.class);
            ILogService logService = new MySqlLogServiceImpl(deviceLogMapper, eventLogMapper, functionLogMapper);
            return logService;
        } else {
            return null;
        }
    }

    /**
    * @Method containBean
    * @Description 根据类判断是否有对应bean
    * @Param 类
    * @return
    * @date 2022/5/22,0022 14:12
    * @author wxy
    *
    */
    private boolean containBean(@Nullable Class<?> T) {
        String[] beans = applicationContext.getBeanNamesForType(T);
        if (beans == null || beans.length == 0) {
            return false;
        } else {
            return true;
        }
    }
}
