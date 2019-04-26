package com.github.soyanga.springmvc.control;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: springMVC-case-practice
 * @Description: @RequestMapping默认的处理方法
 * @Author: SOYANGA
 * @Create: 2019-04-26 21:08
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/demo5")
public class Demo5Controller {

    @RequestMapping
    public String index() {
        return "default method";
    }
}
