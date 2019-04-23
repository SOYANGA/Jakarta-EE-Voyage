package com.github.soyanga.auto;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;

/**
 * @program: springcore-case-IoC
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-15 00:48
 * @Version 1.0
 */
@Component
//@Repository //DAO
//@Service   //service
//@Controller  //MVC Controller
@Scope(value = "prototype")
public class ChineseCurrency {

    public LocalDateTime currentDate() {
        return LocalDateTime.now();
    }

    public ChineseCurrency() {
        System.out.println("Bean构造方法");
    }

    @PostConstruct
    public void init() {
        System.out.println("Bean实例化后执行");
    }

    @PreDestroy
    public void destory() {
        System.out.println("bean销毁之前执行");
    }

}
