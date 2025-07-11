package com.fastbee.bootstrap.tcp.config;

import com.fastbee.protocol.WModelManager;
import com.fastbee.base.core.HandlerMapping;
import com.fastbee.base.core.SpringHandlerMapping;
import com.fastbee.base.session.SessionListener;
import com.fastbee.base.session.SessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author bill
 */
@Configuration
public class TcpBeanConfig {

    @Bean
    public HandlerMapping handlerMapping(){
        return new SpringHandlerMapping();
    }

    @Bean
    public TcpHandlerInterceptor handlerInterceptor(){
        return new TcpHandlerInterceptor();
    }

    @Bean
    public SessionListener sessionListener(){
        return new TcpSessionListener();
    }

    @Bean
    public SessionManager sessionManager(SessionListener sessionListener){
        return new SessionManager(sessionListener);
    }

    @Bean
    public WModelManager wModelManager(){
        return new WModelManager("com.fastbee.modbus");
    }


}
