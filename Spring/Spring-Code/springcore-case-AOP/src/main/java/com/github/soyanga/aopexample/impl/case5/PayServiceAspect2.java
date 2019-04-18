package com.github.soyanga.aopexample.impl.case5;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @program: springcore-case-AOP
 * @Description:  xml-切面Bean
 * @Author: SOYANGA
 * @Create: 2019-04-18 19:43
 * @Version 1.0
 */
@Component
public class PayServiceAspect2 {


    public void payPointcut() {

    }

//    @Pointcut(value = "args(java.lang.String)")
//    public void StringArgs() {
//
//    }

    public void StringArgs() {

    }

    public void beaforeString() {
        System.out.println("Method has a String parameter");

    }


    //时间 切点
    public void beforeLog() {
        //干了什么事
        System.out.println("> before log");

    }

    public void beforeSecurity() {
        System.out.println(">> before sercurity");
    }

    public void beforeStartTime() {
        System.out.println(">>> before start time ");
    }

    public void afterEndTime() {
        System.out.println(">>> afer end time");
    }


    /**
     * @param joinPoint 临界点
     */
    public Object aroundTime(ProceedingJoinPoint joinPoint) {
        //切面前面代码
        System.out.println(">>>> around befor");

        //(临界点)连接点方法的执行
        Object retValue = null;
        try {
            retValue = joinPoint.proceed();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable.getMessage());
        }

        //切面后代码
        System.out.println(">>>> around after");
        return retValue;
    }

    //pay方法返回时后执行
    public void afterRetuning() {
        System.out.println(">>>>> afer returning");
    }

    public void afterThrowing(RuntimeException e) {
        System.out.println(">>>>>> after throwing " + e.getMessage());
    }

}
