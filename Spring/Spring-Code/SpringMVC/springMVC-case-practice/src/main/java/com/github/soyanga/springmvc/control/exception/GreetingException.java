package com.github.soyanga.springmvc.control.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @program: springMVC-case-practice
 * @Description: 自定义异常类
 * @Author: SOYANGA
 * @Create: 2019-04-28 16:28
 * @Version 1.0
 */
public class GreetingException extends RuntimeException {
    public GreetingException(String message) {
        super(message);
    }
}
