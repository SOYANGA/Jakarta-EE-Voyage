package com.github.soyanga.spel;

import com.github.soyanga.spel.pojo.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.applet.AppletContext;
import java.util.Map;
import java.util.Properties;

/**
 * @program: springcore-case-SpEL
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-16 16:47
 * @Version 1.0
 */
public class SpELApplication {

    public static void main(String[] args) {
//        //创建IoC容器
        ApplicationContext context = new ClassPathXmlApplicationContext
                ("application-spel.xml");
//        for (int i = 0; i < 10; i++) {
//            GuessNumber guessNumber = (GuessNumber) context.getBean("guessNumber");
//            System.out.println(guessNumber.toString() + guessNumber.hashCode());
//        }

        //系统属性
        Properties properties = System.getProperties();
        //遍历方式1
//        for(Object key:properties.keySet()){
//
//        }
//        遍历方式2
        System.out.println("Properties -----------------------------");
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        System.out.println("\n\n\n\nEnvironment ----------------------------");
        Map<String, String> envs = System.getenv();
        for (Map.Entry<String, String> entry : envs.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        //内置系统属性Bean
        SystemPropertiesBean systemPropertiesBean = (SystemPropertiesBean) context.getBean("systemPropertiesBean");
        System.out.println(systemPropertiesBean);

        //内置系统环境Bean
        SystemEnvironmentBean systemEnvironmentBean = (SystemEnvironmentBean) context.getBean("systemEnvironmentBean");
        System.out.println("\n\n" + systemEnvironmentBean);

        //基于注解的配置获取properties属性文件中的内容生成Bean
        MyDataSource myDataSource = (MyDataSource) context.getBean("myDataSource");
        System.out.println("\n" + myDataSource);

        //注解+占位符的方式获取properties属性文件中的内容并生成Bean
        MyDataSource2 myDataSource2 = (MyDataSource2) context.getBean("myDataSource2");
        System.out.println("\n" + myDataSource2);

    }
}
