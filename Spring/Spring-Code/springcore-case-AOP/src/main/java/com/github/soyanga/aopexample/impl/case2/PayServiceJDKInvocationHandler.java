package com.github.soyanga.aopexample.impl.case2;

import com.github.soyanga.aopexample.PayService;
import com.github.soyanga.aopexample.component.LogComponent;
import com.github.soyanga.aopexample.component.SercurityComponent;
import com.github.soyanga.aopexample.component.TimeComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


/**
 * @program: springcore-case-AOP
 * @Description: AOP动态代理
 * @Author: SOYANGA
 * @Create: 2019-04-17 20:50
 * @Version 1.0
 */
@Component
public class PayServiceJDKInvocationHandler implements InvocationHandler {

    /**
     * 安全检查
     */
    @Autowired
    private SercurityComponent sercurityComponent;

    /**
     * 日志信息
     */
    @Autowired
    private LogComponent logComponent;


    /**
     * 支付时间
     */
    @Autowired
    private TimeComponent timeComponent;

    @Autowired
    @Qualifier("alipayService")
    private PayService payService;

    /**
     * @param proxy  代理对象
     * @param method 方法
     * @param args   方法参数
     * @return 返回一个
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //1.安全检查 ---code
        sercurityComponent.sercurity();

        //2.日志记录 ---code
        logComponent.log();

        //3-A 支付开始时间
        long startTime = timeComponent.startTime();

        //3.核心支付逻辑 ------>减钱
        Object returnValue = method.invoke(payService, args);

        //3—-B 支付结束时间
        long endTiem = timeComponent.endTime();

        long payTiem = timeComponent.compute(startTime, endTiem);

        return returnValue;
    }

}


//    public Object proxy(Object target) {
//        return Proxy.newProxyInstance(target.getClass(),
//                new Class[]{
//                        PayService.class;
//                },
//                )
//    }

//    public static void main(String[] args) {
//        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
//
//        payServiceJDKInvocationHandler handler =
//                (payServiceJDKInvocationHandler) context.getBean("payServiceJDKInvocationHandler");
//
//        PayService alipayService = (PayService) context.getBean("alipayService");
//        PayService payService = (PayService) Proxy.newProxyInstance(alipayService.getClass().getClassLoader(),
//                new Class[]{
//                        PayService.class
//                }, handler
//        );
//        payService.pay();
//    }
