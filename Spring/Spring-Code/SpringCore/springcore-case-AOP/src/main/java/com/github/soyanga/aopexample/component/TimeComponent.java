package com.github.soyanga.aopexample.component;

import org.springframework.stereotype.Component;

/**
 * @program: springcore-case-AOP
 * @Description: //支付时间统计
 * @Author: SOYANGA
 * @Create: 2019-04-17 17:41
 * @Version 1.0
 */
@Component
public class TimeComponent {


    //3-A 支付开始时间
    public long startTime() {
        System.out.println("TimeComponent-start");
        return System.currentTimeMillis();
    }

    //3—-B 支付结束时间
    public long endTime() {
        System.out.println("TimeComponent-end");
        return System.currentTimeMillis();
    }

    public long compute(long start, long end) {
        System.out.println("TimeComponent-computeTime");
        return end - start;
    }
}
