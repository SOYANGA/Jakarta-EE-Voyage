package com.github.soyanga.aopexample.impl.case5;

import com.github.soyanga.aopexample.PayService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @program: springcore-case-AOP
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-18 21:49
 * @Version 1.0
 */
public class AopXMLApplication {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-aop2.xml");

        PayService service = (PayService) context.getBean("weixinPayService");
        service.pay();

    }
}
