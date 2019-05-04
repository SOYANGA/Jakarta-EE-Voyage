package com.github.soyanga.springBootBasic.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: springBoot-Basics
 * @Description: 一个类型安全的Bean
 * @Author: SOYANGA
 * @Create: 2019-05-03 14:37
 * @Version 1.0
 */
@Component
@ConfigurationProperties(prefix = "app.book")
@Data
public class BookProperties {

    private String author;

    private String name;

}
