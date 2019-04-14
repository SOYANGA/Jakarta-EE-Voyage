package com.github.soyanga.xml;

import com.github.soyanga.auto.ChineseCurrency;
import com.github.soyanga.auto.ShapeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @program: springcore-case-IoC
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-10 18:53
 * @Version 1.0
 */
public class IoCAutoApplication {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "application-context.xml"
        );
        ChineseCurrency chineseCurrency = (ChineseCurrency) context.getBean("chineseCurrency");
        System.out.println(chineseCurrency);

        ShapeService shapeService = (ShapeService) context.getBean("shapeService2");
//        System.out.println(shapeService.getShape());

        System.out.println(shapeService);
        //销毁bean
        ((ClassPathXmlApplicationContext) context).getBeanFactory().destroyBean(chineseCurrency);
    }
}


