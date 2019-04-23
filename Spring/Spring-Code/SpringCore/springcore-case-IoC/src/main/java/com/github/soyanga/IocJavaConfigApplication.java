package com.github.soyanga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: springcore-case-IoC
 * @Description: 配置类 跟业务逻辑无关联，主要是配置Bean的实例化
 * @Author: SOYANGA
 * @Create: 2019-04-10 16:46
 * @Version 1.0
 */
@Configuration
public class IocJavaConfigApplication {

    public static class Student {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }


    @Bean
    public String hello() {
        return "Hello";
    }


    @Bean(value = "zhangsanStudent")
    public Student zhangsan() {
        Student student = new Student();
        student.setId(1);
        student.setName("zhangsan");
        return student;
    }

    @Autowired
    private String hello;

    @Bean(value = "lisiStudent")
    public Student lisi() {
        Student student = new Student();
        student.setId(1);
        student.setName("lisi");
        return student;
    }


    /**
     * 基于Java Conﬁg的配置注解方式 （主要通过Conﬁguration和Bean注解）
     *
     * @param args
     */
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(IocJavaConfigApplication.class);

        String hello = (String) context.getBean("hello");
        System.out.println(hello);

        Student zhangsanStudent = (Student) context.getBean("zhangsanStudent");
        System.out.println(zhangsanStudent);

        Student lisiStudent = (Student) context.getBean("lisiStudent");
        System.out.println(lisiStudent);

    }

}
