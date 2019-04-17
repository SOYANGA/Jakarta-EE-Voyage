package com.github.soyanga.aopexample.impl.exmaplecglib;

import com.github.soyanga.aopexample.PayService;
import com.github.soyanga.aopexample.component.LogComponent;
import com.github.soyanga.aopexample.component.SercurityComponent;
import com.github.soyanga.aopexample.component.TimeComponent;
import com.github.soyanga.aopexample.impl.case3.AlipayServiceImpl;
import com.github.soyanga.aopexample.impl.case3.PaySerciveCglibProxy;
import org.springframework.cglib.proxy.Enhancer;

/**
 * @program: springcore-case-AOP
 * @Description: exampleTest
 * @Author: SOYANGA
 * @Create: 2019-04-17 23:55
 * @Version 1.0
 */
public class CglibProxy {
    public static void main(String[] args) {
        BankService target = new BankService();
        //method interceptor == invocation handler
        PaySerciveCglibProxy interceptor = new PaySerciveCglibProxy(
                target,
                new LogComponent(),
                new SercurityComponent(),
                new TimeComponent()
        );
        //proxy object
        BankService proxy = (BankService) Enhancer.create(target.getClass(), interceptor);
        proxy.pay();
    }
}
