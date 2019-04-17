package com.github.soyanga.aopexample.component;

import org.springframework.stereotype.Component;

/**
 * @program: springcore-case-AOP
 * @Description: 2.日志记录
 * @Author: SOYANGA
 * @Create: 2019-04-17 17:40
 * @Version 1.0
 */
@Component
public class LogComponent {
    //2.日志记录
    public void log() {
        //TODO 实现日志功能
        System.out.println("LogComponent");
    }
}
