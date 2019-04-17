package com.github.soyanga.aopexample.impl.case2;

import com.github.soyanga.aopexample.PayService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Proxy;

/**
 * @program: springcore-case-AOP
 * @Description: JDK动态代理--实现AOP
 * @Author: SOYANGA
 * @Create: 2019-04-17 21:26
 * @Version 1.0
 */
public class JDKProxy {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

        PayServiceJDKInvocationHandler handler =
                (PayServiceJDKInvocationHandler) context.getBean("payServiceJDKInvocationHandler");

        PayService alipayService = (PayService) context.getBean("alipayService");

        PayService payService = (PayService) Proxy.newProxyInstance(alipayService.getClass().getClassLoader(),
                new Class[]{
                        PayService.class
                }, handler
        );
        payService.pay();
    }

}
