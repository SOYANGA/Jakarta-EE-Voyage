package com.github.soyanga.springBootBasic.health;


import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;


import java.util.Random;

/**
 * @program: springBoot-action
 * @Description: 自定义HealthIndicator为程序提供健康信息
 * @Author: SOYANGA
 * @Create: 2019-05-04 22:45
 * @Version 1.0
 */
@Component
public class RandomHealth implements HealthIndicator {

    @Override
    public Health health() {
        boolean flag = new Random().nextBoolean();
        if (flag) {
            return Health.up().withDetail("info", "System Ok").build();
        } else {
            return Health.down().withDetail("info", "System error").build();
        }
    }
}
