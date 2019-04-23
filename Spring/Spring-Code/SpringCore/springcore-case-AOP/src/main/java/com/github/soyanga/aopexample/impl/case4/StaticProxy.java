package com.github.soyanga.aopexample.impl.case4;

import com.github.soyanga.aopexample.component.LogComponent;
import com.github.soyanga.aopexample.component.SercurityComponent;
import com.github.soyanga.aopexample.component.TimeComponent;
import com.github.soyanga.aopexample.impl.AlipayServiceImpl;
import com.github.soyanga.aopexample.impl.WeixinPayServiceImpl;

/**
 * @program: springcore-case-AOP
 * @Description: 静态代理
 * @Author: SOYANGA
 * @Create: 2019-04-18 00:52
 * @Version 1.0
 */
public class StaticProxy {
    public static void main(String[] args) {

        //ali专一代理的静态代理
//        StaticProxyAlipayService staticProxy = new StaticProxyAlipayService(
//                new AlipayServiceImpl(),
//                new LogComponent(),
//                new SercurityComponent(),
//                new TimeComponent()
//        );
//        staticProxy.pay();


        //静态代理接口实现
        StaticProxyPayService staticProxy = new StaticProxyPayService(
                new WeixinPayServiceImpl(),
                new LogComponent(),
                new SercurityComponent(),
                new TimeComponent()
        );
        staticProxy.pay();

    }
}
