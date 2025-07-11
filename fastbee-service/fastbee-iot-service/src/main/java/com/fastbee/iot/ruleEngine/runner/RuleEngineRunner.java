package com.fastbee.iot.ruleEngine.runner;

import com.fastbee.iot.service.impl.SceneMiddleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(4)
public class RuleEngineRunner implements ApplicationRunner {

    @Autowired
    private SceneMiddleServiceImpl sceneMiddleService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        sceneMiddleService.init();
        log.info("初始化自定义规则配置成功");
    }
}
