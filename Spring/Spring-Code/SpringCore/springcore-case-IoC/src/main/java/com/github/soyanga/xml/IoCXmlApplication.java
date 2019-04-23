package com.github.soyanga.xml;

import com.github.soyanga.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;

/**
 * @program: springcore-case-IoC
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-10 18:53
 * @Version 1.0
 */
public class IoCXmlApplication {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "application-context.xml"
        );

        //Ioc容器会对象复用，创建多个对象时。
        //通过构造方法实例化对象
        ExampleBean bean1 = context.getBean(ExampleBean.class);
        ExampleBean bean2 = (ExampleBean) context.getBean("exampleBean");

        ExampleBean2 bean21 = context.getBean(ExampleBean2.class);
        ExampleBean2 bean22 = (ExampleBean2) context.getBean("exampleBean2");

        //通过静态工厂实例化对象
        ClientService service1 = (ClientService) context.getBean("clientService");
        ClientService service2 = (ClientService) context.getBean("clientService");
//        ClientService service3 = context.getBean(ClientService.class);
//        ClientService service4 = context.getBean(ClientService.class);

        //通过实例工厂方法实例对象
        ClientService service5 = (ClientService) context.getBean("clientService2");


//        System.out.println(bean1 + " ------- " + bean2);
//        System.out.println(bean21 + " ------- " + bean22);
//        System.out.println();
//        System.out.println(service1 + " ------- " + service2);
//        System.out.println();
//        System.out.println(service5);
//        System.out.println(service3 + " ------- " + service4);
        Foo foo = (Foo) context.getBean("foo");
        Foo foo1 = (Foo) context.getBean("foo1");
        ExampleBean3 exampleBean3 = (ExampleBean3) context.getBean("exampleBean3");
        ExampleBean3 exampleBean4 = (ExampleBean3) context.getBean("exampleBean4");
        ExampleBean3 exampleBean5 = (ExampleBean3) context.getBean("exampleBean5");
        ComplexShape complexShape = (ComplexShape) context.getBean("complexShape");
        System.out.println(foo);
        System.out.println(foo1);
        System.out.println(exampleBean3);
        System.out.println(exampleBean4);
        System.out.println(exampleBean5);
        System.out.println(complexShape);

        DataSource dataSource = (DataSource) context.getBean("dataSource");
        System.out.println(dataSource);

        Idref idref = (Idref) context.getBean("idref");

        Bar bar = (Bar) context.getBean(idref.getId());
        System.out.println(bar);

        Foo foo2 = (Foo) context.getBean("foo2");
        System.out.println(foo2);

        ComplexObject complexObject = (ComplexObject) context.getBean("complexObject");
        System.out.println(complexObject);

        ExampleEmail exampleEmail = (ExampleEmail) context.getBean("exampleEmail");
        System.out.println(exampleEmail);

    }
}


