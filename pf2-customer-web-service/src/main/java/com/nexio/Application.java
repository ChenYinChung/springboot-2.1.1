package com.nexio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages = { "com.nexio.cws" })
@PropertySource({"application.properties","undertow.properties", "jdbc.properties","redis.yaml"})

@EnableCaching
public class Application {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    public static void main(String args[]){
        //執行SpringApplication
        SpringApplication.run(Application.class, args);

    }


}