package com.github.soyanga.springmvc.control.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @program: springMVC-case-practice
 * @Description: 定义一个异常类处理且与404绑定 错误信息为 "username参数有问题
 * @Author: SOYANGA
 * @Create: 2019-04-28 16:20
 * @Version 1.0
 */
//@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "username参数有问题")
public class RequestParamterInvalidException extends RuntimeException {

}
