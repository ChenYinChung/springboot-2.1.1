package com.sb.schedule;

import com.sb.config.QuartzJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

@QuartzJob(name = "Hello2Job", cronExp = "0 0/5 * * * ?")
public class Hello2Job extends QuartzJobBean {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("Hello 2 Job is running @ " + new Date());
    }
}