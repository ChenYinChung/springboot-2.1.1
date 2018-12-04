package com.sb.schedule;

import com.sb.config.QuartzJob;
import com.sb.util.PropertiesUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Date;


/**
 * @Auther: sammy
 * @Date: 2018/5/17 22:43
 * @Description:
 */
@QuartzJob(name = "KafkaJob", cronExp = "0/30 * * * * ?")
public class KafkaJob extends QuartzJobBean {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    static final String URL_KAFKA_SEND = "/kafka/send?message=";

    @Autowired
    RestTemplate restTemplate;

    public void executeInternal(JobExecutionContext context) throws JobExecutionException {

//        RestTemplate restTemplate = new RestTemplate();

        try {
            String host = PropertiesUtils.applicationHost();

            String result = restTemplate.getForObject(host+URL_KAFKA_SEND+new Date(), String.class);

            logger.info("kafka send get result[{}]",result);

        } catch (IOException e) {
            logger.error("error occurs" ,e);
        }

    }

}
