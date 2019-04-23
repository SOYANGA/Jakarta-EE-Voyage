package com.github.soyanga.aopexample.impl.case3;

import com.github.soyanga.aopexample.PayService;

/**
 * @program: springcore-case-AOP
 * @Description: 阿里支付
 * @Author: SOYANGA
 * @Create: 2019-04-17 17:34
 * @Version 1.0
 */
public class AlipayServiceImpl implements PayService {

    @Override
    public void pay() {

        //1.安全检查

        //2.日志记录

        //3-A 支付开始时间
        //3.核心支付逻辑 ------>减钱
        System.out.println("alipayService");
        //3—-B 支付结束时间

    }
}
