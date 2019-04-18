package com.github.soyanga.aopexample.impl.case5;

import com.github.soyanga.aopexample.PayService;
import com.github.soyanga.aopexample.impl.case5.BankPayService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @program: springcore-case-AOP
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-18 20:15
 * @Version 1.0
 */
public class AopApplication {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-aop.xml");

//        PayService payService = (PayService) context.getBean("alipayService");
//        payService.pay();

        BankPayService bankService = (BankPayService) context.getBean("bankPayService");
        bankService.pay();
        System.out.println("----------------------------------");
        bankService.pay("hello");  //有参
    }
}
