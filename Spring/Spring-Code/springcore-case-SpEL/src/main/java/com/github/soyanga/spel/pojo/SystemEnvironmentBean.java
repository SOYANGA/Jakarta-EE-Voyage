package com.github.soyanga.spel.pojo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: springcore-case-SpEL
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-16 19:46
 * @Version 1.0
 */
@Data
@Component
public class SystemEnvironmentBean {

    @Value(value = "#{systemEnvironment['APPDATA']}")
    private String appData;

    @Value(value = "#{systemEnvironment['Path']}")
    private String path;

    @Value(value = "#{systemEnvironment['SystemDirver']}")
    private String systemDriver;
}
