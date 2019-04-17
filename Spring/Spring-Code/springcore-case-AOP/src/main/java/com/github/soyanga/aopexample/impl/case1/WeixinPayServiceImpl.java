package com.github.soyanga.aopexample.impl.case1;

import com.github.soyanga.aopexample.PayService;
import com.github.soyanga.aopexample.component.LogComponent;
import com.github.soyanga.aopexample.component.SercurityComponent;
import com.github.soyanga.aopexample.component.TimeComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: springcore-case-AOP
 * @Description: 微信支付
 * @Author: SOYANGA
 * @Create: 2019-04-17 17:34
 * @Version 1.0
 */
@Service("weixinPayService1")
public class WeixinPayServiceImpl implements PayService {
    /**
     * 安全检查
     */
    @Autowired
    private SercurityComponent sercurityComponent;

    /**
     * 日志信息
     */
    @Autowired
    private LogComponent logComponent;


    /**
     * 支付时间
     */
    @Autowired
    private TimeComponent timeComponent;

    @Override
    public void pay() {

        //1.安全检查 ---code
        sercurityComponent.sercurity();

        //2.日志记录 ---code
        logComponent.log();

        //3-A 支付开始时间
        long startTime = timeComponent.startTime();

        //3.核心支付逻辑 ------>减钱


        //3—-B 支付结束时间
        long endTiem = timeComponent.endTime();

        long payTiem = timeComponent.compute(startTime, endTiem);

    }
}
