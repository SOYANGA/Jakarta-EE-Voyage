package com.github.soyanga.springBootBasic.config;

import com.github.soyanga.springBootBasic.component.ExampleBean;
import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import java.util.Properties;

/**
 * @program: springBoot-Basics
 * @Description: 配置@Configuration的使用 相当于xml 配置类
 * @Author: SOYANGA
 * @Create: 2019-05-02 16:49
 * @Version 1.0
 */
@Configuration
public class AppConfiguration {

    @Bean(value = "hello")
    public String helloString() {
        return "hello";
    }


    @Bean(value = "welcome")
    public String welcomeString() {
        return "welcome";
    }


    @Bean
    public Properties properties() {
        return new Properties();
    }

    @Bean(initMethod = "init", destroyMethod = "destroy")
    @Scope(scopeName = "prototype") //非单例
    public ExampleBean exampleBean() {
        ExampleBean exampleBean = new ExampleBean();
        exampleBean.setName("jack");
        return exampleBean;
    }


    /**
     * 自己写的Gson Bean 添加@Primary注解作为Gson的主类
     *
     * @return
     */
    @Bean
    @Primary
    public Gson gson() {
        return new Gson();
    }
}

