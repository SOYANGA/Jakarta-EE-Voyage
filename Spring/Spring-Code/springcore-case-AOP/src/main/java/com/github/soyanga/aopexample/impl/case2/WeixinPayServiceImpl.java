package com.github.soyanga.aopexample.impl.case2;

import com.github.soyanga.aopexample.PayService;
import org.springframework.stereotype.Service;

/**
 * @program: springcore-case-AOP
 * @Description: 微信支付
 * @Author: SOYANGA
 * @Create: 2019-04-17 17:34
 * @Version 1.0
 */
@Service("weixinPayService")
public class WeixinPayServiceImpl implements PayService {
    @Override
    public void pay() {

        //1.安全检查

        //2.日志记录

        //3-A 支付开始时间
        //3.核心支付逻辑 ------>减钱
        //3—-B 支付结束时间
        System.out.println("weixinPayService");
    }
}
