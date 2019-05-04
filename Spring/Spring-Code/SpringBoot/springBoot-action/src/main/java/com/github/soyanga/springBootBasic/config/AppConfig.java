package com.github.soyanga.springBootBasic.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @program: springBoot-Basics
 * @Description: @PropertySource属性源配置信息 属性源
 * @Author: SOYANGA
 * @Create: 2019-05-02 17:19
 * @Version 1.0
 */
@PropertySource(value = {"classpath:appConfig.properties"})
@Data
@Component
public class AppConfig {

    @Value("${app.config.host}")
    private String host;

    @Value("${app.config.port}")
    private Integer port;
}
