package com.sb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: sammy
 * @Date: 2018/5/17 22:43
 * @Description:
 */
@Configuration
public class ListenerConfig {

    @Bean
    public QuartJobSchedulingListener applicationStartListener(){
        return new QuartJobSchedulingListener();
    }
}