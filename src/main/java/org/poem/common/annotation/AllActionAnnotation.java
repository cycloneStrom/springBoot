package org.poem.common.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.poem.common.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
* Created by poem on 2016/5/7.
* 切入点日志管理
*/
@Configuration
@Aspect
public class AllActionAnnotation {

    /**
     * 日志管理
     */
    private static  final Logger loggrer = LoggerFactory.getLogger(AllActionAnnotation.class);

    /**
     * 切面方法
     * 使用表达式  所有org.poem.community下的任意方法
     */
    @Pointcut(value = "execution(* org.poem.community.controller.error..*.*(..))")
    private void allMethod(){

    }

    /**
     * 前置通知
     * @param joinPoint
     */
    @Before("allMethod()")
    public void beforeAdvice(JoinPoint joinPoint){
        /*获取调用的类名*/
        String targetName = joinPoint.getTarget().getClass().getName();
        /*获取调用的方法名*/
        String targetMethodName = joinPoint.getSignature().getName();
        /*写入日志*/
        loggrer.info("前置通知：" + targetName + " 类的" + targetMethodName + " 方法开始执行.");
    }

    /**
     * 定义后只通知
     * @param joinPoint
     */
    @AfterReturning("allMethod()")
    public void afterAdvice(JoinPoint joinPoint ){
         /*获取调用的类名*/
        String targetName = joinPoint.getTarget().getClass().getName();
        /*获取调用的方法名*/
        String targetMethodName = joinPoint.getSignature().getName();
        /*写入日志*/
        loggrer.info("后置通知：" + targetName + " 类的" + targetMethodName + " 方法结束执行.\n");
    }

    /**
     * 异常通知
     * @param joinPoint
     */
    @AfterThrowing(pointcut = "allMethod()",throwing = "e")
    public void throwAdvice(JoinPoint joinPoint){
        /*获取调用的类名*/
        String targetName = joinPoint.getTarget().getClass().getName();
        /*获取调用的方法名*/
        String targetMethodName = joinPoint.getSignature().getName();
        /*写入日志*/
        loggrer.info("异常通知：" + targetName + " 类的" + targetMethodName + " 方法发生异常.");
    }

    /**
     * 定义环绕通知
     * @param joinPoint
     */
    @Around(value = "allMethod()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object object = null;
        /* 开始时间 */
        Date startDate = new Date();
        /* 执行进程 */
        object = joinPoint.proceed();
        /* 结束时间 */
        Date endDate = new Date();

        /*获取调用的类名*/
        String targetName = joinPoint.getTarget().getClass().getName();
        /*获取调用的方法名*/
        String targetMethodName = joinPoint.getSignature().getName();

        /*写入日志*/
        loggrer.info("环绕通知：" + targetName + " 类的" + targetMethodName + " 方法正在执行，开始调用时间：" + DateUtils.formatDateTime(startDate) + ",结束时间：" + DateUtils.formatDateTime(endDate) + "，耗时：" + (endDate.getTime() - startDate.getTime()) + "毫秒.");

        return object;
    }
}
