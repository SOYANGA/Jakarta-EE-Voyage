package com.github.soyanga.spel.pojo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: springcore-case-SpEL
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-16 19:35
 * @Version 1.0
 */
@Data
@Component
public class SystemPropertiesBean {

    @Value(value = "#{systemProperties['java.class.path']}")
    private String classPath;

    @Value(value = "#{systemProperties['java.home']}")
    private String javaHome;

    @Value(value = "#{systemProperties['java.version']}")
    private String javaVersion;

    @Value(value = "#{systemProperties['os.name']}")
    private String osName;
}
