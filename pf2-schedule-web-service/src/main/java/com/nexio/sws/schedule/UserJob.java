package com.sb.schedule;

import com.sb.config.QuartzJob;
import com.sb.util.PropertiesUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.client.RestTemplate;

/**
 * @Auther: sammy
 * @Date: 2018/5/17 23:54
 * @Description:
 */

@QuartzJob(name = "UserJob", cronExp = "0 0/10 * * * ?")
public class UserJob extends QuartzJobBean {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RestTemplate restTemplate;


    static final String URL_USER_CREATE = "/user/add/";
    static final String URL_USER_LOGIN = "/user/login";
    static final String URL_USER_FIND = "/user/find/";
    static final String URL_USER_DELETE = "/user/delete/";

    public void executeInternal(JobExecutionContext context) throws JobExecutionException {

        String host = null;
        try {
            host = PropertiesUtils.applicationHost();


            for (int i = 0; i < 50; i++) {
                String user_name = "sample";
                user_name += i;
                String add = host + URL_USER_CREATE + user_name + "/" + i;
                String json = "{\"name\":\"" + user_name + "\",\"age\":" + i + "}";

                // add new user
                get(add);
            }

            Thread.sleep(3000);

            for (int i = 0; i < 50; i++) {
                String user_name = "sample";
                user_name += i;
                String add = host + URL_USER_CREATE + user_name + "/" + i;
                String json = "{\"name\":\"" + user_name + "\",\"age\":" + i + "}";

                // find user
                String find = host + URL_USER_FIND + user_name;
                get(find);
            }
            Thread.sleep(3000);

            for (int i = 0; i < 50; i++) {
                String user_name = "sample";
                user_name += i;
                String json = "{\"name\":\"" + user_name + "\",\"age\":" + i + "}";

                // login
                String login = host + URL_USER_LOGIN;
                post(login, json);
            }

            Thread.sleep(3000);
            for (int i = 0; i < 50; i++) {
                String user_name = "sample";
                user_name += i;
                //delete
                String delete = host + URL_USER_DELETE+user_name;
                delete(delete);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void get(String url) {
        RestTemplate restTemplate = new RestTemplate();

        String result = restTemplate.getForObject(url, String.class);

        logger.info("User get[{}] result[{}]", url, result);

    }


    private void post(String url, String json) {

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        String result = restTemplate.postForObject(url, entity, String.class);

        logger.info("User post[{}] result[{}]", url, result);
    }

    private void delete(String url ) {

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);

        restTemplate.delete(url);
        logger.info("User delete[{}]", url);
    }

}
