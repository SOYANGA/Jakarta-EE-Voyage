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
public class MyDataSource2 {

    @Value(value = "${jdbc.url}")
    private String url;

    @Value(value = "${jdbc.username}")
    private String userName;

    @Value(value = "${password}")
    private String passWord;

    @Value(value = "${jdbc.driverclass}")
    private String driverClass;
}
