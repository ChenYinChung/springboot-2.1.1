package com.sb.util;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Properties;

/**
 * @Auther: sammy
 * @Date: 2018/5/17 22:45
 * @Description:
 */
public class PropertiesUtils {

    private static final String QUARTZ_PROPERTIES_PATH = "/quartz.properties";
    private static final String APPLICATION_PROPERTIES_PATH = "/application.properties";

    public static Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource(QUARTZ_PROPERTIES_PATH));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    public static Properties applicationProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource(APPLICATION_PROPERTIES_PATH));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }


    public static String applicationHost() throws IOException {
        return applicationProperties().getProperty("host");
    }
}
