package com.github.soyanga.aopexample.impl.case5;

import org.springframework.stereotype.Service;

/**
 * @program: springcore-case-AOP
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-18 20:34
 * @Version 1.0
 */
@Service
public class BankPayService {
    public void pay() {
        System.out.println("无参数");
    }

    public void pay(String str) {
        System.out.println("有参数 " + str);
    }
}
