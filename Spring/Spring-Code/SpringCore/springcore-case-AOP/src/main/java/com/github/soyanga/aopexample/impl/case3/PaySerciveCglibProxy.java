package com.github.soyanga.aopexample.impl.case3;

import com.github.soyanga.aopexample.PayService;
import com.github.soyanga.aopexample.component.LogComponent;
import com.github.soyanga.aopexample.component.SercurityComponent;
import com.github.soyanga.aopexample.component.TimeComponent;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @program: springcore-case-AOP
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-17 23:47
 * @Version 1.0
 */
public class PaySerciveCglibProxy implements MethodInterceptor {

    private final Object target;

    private final LogComponent logComponent;

    private final SercurityComponent sercurityComponent;

    private final TimeComponent timeComponent;

    public PaySerciveCglibProxy(Object target, LogComponent logComponent, SercurityComponent sercurityComponent, TimeComponent timeComponent) {
        this.target = target;
        this.logComponent = logComponent;
        this.sercurityComponent = sercurityComponent;
        this.timeComponent = timeComponent;
    }

    /**
     * @param proxy       代理对象
     * @param method
     * @param objects
     * @param methodProxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object proxy, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        //1.安全检查 ---code
        sercurityComponent.sercurity();

        //2.日志记录 ---code
        logComponent.log();

        //3-A 支付开始时间
        long startTime = timeComponent.startTime();

        //3.核心支付逻辑 ------>减钱

        Object returnValue = methodProxy.invoke(target, objects);

        //3—-B 支付结束时间
        long endTiem = timeComponent.endTime();

        long payTiem = timeComponent.compute(startTime, endTiem);
        //花费时间
        System.out.println("Cast Time: " + payTiem + "ms");
        return returnValue;
    }
}
