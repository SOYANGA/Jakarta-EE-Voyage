package com.github.soyanga.boot.control;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: springboot-case
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-05-01 17:41
 * @Version 1.0
 */
@RestController
@RequestMapping
public class IndexController {

    @RequestMapping(value = "")
    public String index() {
        return "Hello Spring Boot World ";
    }

}
