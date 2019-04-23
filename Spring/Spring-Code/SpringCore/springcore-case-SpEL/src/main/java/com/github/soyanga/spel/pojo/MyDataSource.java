package com.github.soyanga.spel.pojo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: springcore-case-SpEL
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-16 20:11
 * @Version 1.0
 */
@Component
@Data
public class MyDataSource {

    @Value(value = "#{properties['url']}")
    private String url;

    @Value(value = "#{properties['username']}")
    private String userName;

    @Value(value = "#{properties['password']}")
    private String passWord;

    @Value(value = "#{properties['driverclass']}")
    private String driverClass;
}
