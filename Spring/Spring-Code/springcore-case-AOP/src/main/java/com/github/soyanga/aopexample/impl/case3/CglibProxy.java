package com.github.soyanga.aopexample.impl.case3;

import com.github.soyanga.aopexample.PayService;
import com.github.soyanga.aopexample.component.LogComponent;
import com.github.soyanga.aopexample.component.SercurityComponent;
import com.github.soyanga.aopexample.component.TimeComponent;
import com.github.soyanga.aopexample.impl.case3.AlipayServiceImpl;
import org.springframework.cglib.proxy.Enhancer;

/**
 * @program: springcore-case-AOP
 * @Description: Cglib 动态代理
 * @Author: SOYANGA
 * @Create: 2019-04-17 23:55
 * @Version 1.0
 */
public class CglibProxy {
    public static void main(String[] args) {
        PayService target = new AlipayServiceImpl();
        //method interceptor == invocation handler
        PaySerciveCglibProxy interceptor = new PaySerciveCglibProxy(
                target,
                new LogComponent(),
                new SercurityComponent(),
                new TimeComponent()
        );

        //proxy object
        PayService proxy = (PayService) Enhancer.create(target.getClass(), interceptor);
        proxy.pay();
    }
}
