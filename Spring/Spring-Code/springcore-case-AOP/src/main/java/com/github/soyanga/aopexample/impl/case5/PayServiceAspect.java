package com.github.soyanga.aopexample.impl.case5;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @program: springcore-case-AOP
 * @Description: 注解切面Bean
 * @Author: SOYANGA
 * @Create: 2019-04-18 19:43
 * @Version 1.0
 */
//@Aspect
@Component
public class PayServiceAspect {

    //切点在PayService.pay()上
    @Pointcut(value = "execution(* com.github.soyanga.aopexample.*.pay())")
    public void payPointcut() {

    }

    //    @Pointcut(value = "args(java.lang.String)")
//    public void StringArgs() {
//
//    }
    // 针对BankService的有参pay()方法的拦截
    @Pointcut(value = "execution(* com.github.soyanga.aopexample.impl.*.*.pay(java.lang.String))")
    public void StringArgs() {

    }

    @Before(value = "StringArgs()")
    public void beaforeString() {
        System.out.println("Method has a String parameter");

    }


    //时间 切点
    @Before(value = "payPointcut()")
    public void beforeLog() {
        //干了什么事
        System.out.println("> before log");

    }

    @Before(value = "payPointcut()")
    public void beforeSecurity() {
        System.out.println(">> before sercurity");
    }

    @Before(value = "payPointcut()")
    public void beforeStartTime() {
        System.out.println(">>> before start time ");
    }

    @After(value = "payPointcut()")
    public void afterEndTime() {
        System.out.println(">>> afer end time");
    }


    /**
     * @param joinPoint 临界点
     */
    @Around(value = "payPointcut()")
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
    @AfterReturning(value = "payPointcut()")
    public void afterRetuning() {
        System.out.println(">>>>> afer returning");
    }

    @AfterThrowing(value = "payPointcut()", throwing = "e")
    public void afterThrowing(RuntimeException e) {
        System.out.println(">>>>>> after throwing " + e.getMessage());
    }

}
