package com.sb.config;

import com.sb.util.PropertiesUtils;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.Set;


/**
 * when application startup , auto scan this class and config first
 *
 * @Auther: sammy
 * @Date: 2018/5/17 22:43
 * @Description:
 */
public class QuartJobSchedulingListener implements ApplicationListener<ContextRefreshedEvent> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            ApplicationContext applicationContext = event.getApplicationContext();
            loadAndRunQuartzJob(applicationContext);
        } catch (Exception e) {
            logger.error("startup error",e);
            System.exit(0);
        }
    }

    private void loadAndRunQuartzJob(ApplicationContext applicationContext) throws Exception {

        Map<String, Object> quartzJobBeans = applicationContext.getBeansWithAnnotation(QuartzJob.class);
        Set<String> beanNames = quartzJobBeans.keySet();
        SchedulerFactoryBean schedulerFactoryBean =  buildSchedulerFactoryBean();
        schedulerFactoryBean.afterPropertiesSet();

        beanNames.forEach(t -> {
            QuartzJobBean job = (QuartzJobBean) quartzJobBeans.get(t);
            if (Job.class.isAssignableFrom(job.getClass())) {
                try {
                    CronTriggerFactoryBean cronTriggerFactoryBean = buildCronTriggerFactoryBean(job);
                    JobDetailFactoryBean jobDetailFactoryBean = buidlJobDetailFactoryBean(job);
                    jobDetailFactoryBean.setApplicationContext(applicationContext);
                    jobDetailFactoryBean.afterPropertiesSet();

                    cronTriggerFactoryBean.setJobDetail(jobDetailFactoryBean.getObject());
                    cronTriggerFactoryBean.afterPropertiesSet();

                    if(!schedulerFactoryBean.getObject().checkExists(jobDetailFactoryBean.getObject().getKey())){
                        schedulerFactoryBean.getObject().scheduleJob(jobDetailFactoryBean.getObject(), cronTriggerFactoryBean.getObject());
                    }

                    schedulerFactoryBean.getObject().start();
                } catch (ParseException |SchedulerException pe) {
                    logger.error("CronTriggerFactoryBean error",pe);
                }

            }
        });
    }


    private CronTriggerFactoryBean buildCronTriggerFactoryBean(QuartzJobBean job) {

        CronTriggerFactoryBean cronTriggerFactoryBean = null;
        QuartzJob quartzJobAnnotation = AnnotationUtils.findAnnotation(job.getClass(), QuartzJob.class);
        cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setCronExpression(quartzJobAnnotation.cronExp());
        cronTriggerFactoryBean.setName(quartzJobAnnotation.name() + "_trigger");
        cronTriggerFactoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);
        return cronTriggerFactoryBean;
    }

    private JobDetailFactoryBean buidlJobDetailFactoryBean(QuartzJobBean job) {
        QuartzJob quartzJobAnnotation = AnnotationUtils.findAnnotation(job.getClass(), QuartzJob.class);
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setName(quartzJobAnnotation.name());
        jobDetailFactoryBean.setJobClass(job.getClass());
        jobDetailFactoryBean.setDurability(true);
        jobDetailFactoryBean.setBeanName(job.getClass().getName());

        return jobDetailFactoryBean;
    }

    private SchedulerFactoryBean buildSchedulerFactoryBean() throws IOException {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.setQuartzProperties(PropertiesUtils.quartzProperties());
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        return schedulerFactoryBean;
    }



}