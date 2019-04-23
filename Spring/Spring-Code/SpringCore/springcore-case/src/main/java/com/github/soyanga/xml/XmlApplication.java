package com.github.soyanga.xml;

import com.github.soyanga.common.Shape;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @program: springcore-case
 * @Description: 创建IoC容器并使用Bean
 * @Author: SOYANGA
 * @Create: 2019-04-10 00:38
 * @Version 1.0
 */
public class XmlApplication {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        XmlShapeCompute xmlShapeCompute = (XmlShapeCompute) context.getBean("xmlShapeCompute");
        Shape shape = xmlShapeCompute.computeShape(args[0]);
        System.out.println(shape);
    }
}
