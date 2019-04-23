package com.github.soyanga.aopexample.impl.case5;

import com.github.soyanga.aopexample.PayService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @program: springcore-case-AOP
 * @Description: JavaConfig配置切面
 * @Author: SOYANGA
 * @Create: 2019-04-18 21:14
 * @Version 1.0
 */
@Configuration
@ComponentScan(basePackages = "com.github.soyanga.aopexample.impl.case5") //扫描包
@EnableAspectJAutoProxy  //相当于appliction-aop.xml中添加扫描的aop的注解
public class AopJavaConfigApplication {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AopJavaConfigApplication.class);

        PayService payService = (PayService) context.getBean("weixinPayService");
        payService.pay();
    }
}
