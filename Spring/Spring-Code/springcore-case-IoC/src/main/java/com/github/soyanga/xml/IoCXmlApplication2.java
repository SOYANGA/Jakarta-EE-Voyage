package com.github.soyanga.xml;

import com.github.soyanga.*;
import com.github.soyanga.MyScope.SimpleThreadScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.util.concurrent.CountDownLatch;

/**
 * @program: springcore-case-IoC
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-10 18:53
 * @Version 1.0
 */
public class IoCXmlApplication2 {
    public static void main(String[] args) {
        final ApplicationContext context = new ClassPathXmlApplicationContext(
                "application-context.xml"
        );
        lazyInitExample lazyInitExample = (lazyInitExample) context.getBean("lazyInitExample");
        System.out.println(lazyInitExample);
        Customer customer = (Customer) context.getBean("customer");
        Customer2 customer2 = (Customer2) context.getBean("customer2");
        Customer2 customer3 = (Customer2) context.getBean("customer3");

        System.out.println(customer);
        System.out.println(customer2);
        System.out.println(customer3);

        ExampleEmail exampleEmail = (ExampleEmail) context.getBean("exampleEmail");
        ExampleEmail exampleEmail2 = (ExampleEmail) context.getBean("exampleEmail");

        System.out.println(exampleEmail.hashCode());
        System.out.println(exampleEmail2.hashCode());

        //代码中注册Scope
//        ((ClassPathXmlApplicationContext) context).getBeanFactory()
//                .registerScope("thread", new SimpleThreadScope());
        final int count = 3;
        final CountDownLatch countDownLatch = new CountDownLatch(count);

        for (int i = 0; i < count; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ExampleEmail exampleEmail3 = (ExampleEmail) context.getBean("exampleEmail2");
                    System.out.println("Current Thread: " + Thread.currentThread().getName() + " " +
                            exampleEmail3.hashCode());
                    countDownLatch.countDown();
                }
            }).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}


