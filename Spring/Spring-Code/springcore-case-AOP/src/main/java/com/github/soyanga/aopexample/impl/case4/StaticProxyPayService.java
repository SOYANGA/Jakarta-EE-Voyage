package com.github.soyanga.aopexample.impl.case4;

import com.github.soyanga.aopexample.PayService;
import com.github.soyanga.aopexample.component.LogComponent;
import com.github.soyanga.aopexample.component.SercurityComponent;
import com.github.soyanga.aopexample.component.TimeComponent;
import com.github.soyanga.aopexample.impl.AlipayServiceImpl;

/**
 * @program: springcore-case-AOP
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-18 00:48
 * @Version 1.0
 */
public class StaticProxyPayService implements PayService {

    private final PayService target;  //被代理类

    private final LogComponent logComponent;

    private final SercurityComponent sercurityComponent;

    private final TimeComponent timeComponent;

    public StaticProxyPayService(PayService target, LogComponent logComponent, SercurityComponent sercurityComponent, TimeComponent timeComponent) {
        this.target = target;
        this.logComponent = logComponent;
        this.sercurityComponent = sercurityComponent;
        this.timeComponent = timeComponent;
    }


    @Override
    public void pay() {
        //1.安全检查 ---code
        sercurityComponent.sercurity();

        //2.日志记录 ---code
        logComponent.log();

        //3-A 支付开始时间
        long startTime = timeComponent.startTime();

        //3.核心支付逻辑 ------>减钱

        this.target.pay();

        //3—-B 支付结束时间
        long endTiem = timeComponent.endTime();

        long payTiem = timeComponent.compute(startTime, endTiem);
        //花费时间
        System.out.println("Cast Time: " + payTiem + "ms");
    }
}
