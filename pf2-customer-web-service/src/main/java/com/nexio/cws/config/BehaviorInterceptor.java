package com.nexio.cws.config;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * @Auther: sammy
 *
 * AOP 攔截所有controller 的 Request 紀錄傳輸資訊
 *
 */
@Aspect
@Component
public class BehaviorInterceptor {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HttpServletRequest request;

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    public BehaviorInterceptor(HttpServletRequest request) {
        this.request = request;
    }

    @Pointcut("execution(public * com.nexio.cws.controller..*.*(..))")
    public void behaviroLog(){}



    @Before("behaviroLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());

        StringBuilder sb = new StringBuilder();

        sb.append("Entering in Method :  " + joinPoint.getSignature().getName()).append(System.getProperty("line.separator"));
        sb.append("Class Name :  " + joinPoint.getSignature().getDeclaringTypeName()).append(System.getProperty("line.separator"));
        sb.append("Arguments :  " + Arrays.toString(joinPoint.getArgs())).append(System.getProperty("line.separator"));
        sb.append("Target class : " + joinPoint.getTarget().getClass().getName()).append(System.getProperty("line.separator"));

        if (null != request) {
            sb.append("Start Header Section of request ").append(System.getProperty("line.separator"));
            sb.append("Method Type : " + request.getMethod()).append(System.getProperty("line.separator"));
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                sb.append("Header Name: " + headerName + " Header Value : " + headerValue).append(System.getProperty("line.separator"));
            }
            sb.append("Request Path info :" + request.getServletPath()).append(System.getProperty("line.separator"));
            sb.append("End Header Section of request ").append(System.getProperty("line.separator"));
        }

        logger.info(sb.toString());
    }

    @AfterReturning(returning = "ret", pointcut = "behaviroLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        logger.info("RESPONSE : " + ret);
        logger.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));
    }

    @Around("behaviroLog()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        try {
            String className = joinPoint.getSignature().getDeclaringTypeName();
            String methodName = joinPoint.getSignature().getName();
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            logger.info("Method " + className + "." + methodName + " ()" + " execution time : "
                    + elapsedTime + " ms");

            return result;
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument " + Arrays.toString(joinPoint.getArgs()) + " in "
                    + joinPoint.getSignature().getName() + "()");
            throw e;
        }
    }
}
