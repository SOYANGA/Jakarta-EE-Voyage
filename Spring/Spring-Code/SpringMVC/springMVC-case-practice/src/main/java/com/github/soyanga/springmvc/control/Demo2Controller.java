package com.github.soyanga.springmvc.control;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: springMVC-case-practice
 * @Description: @RequestMapping来处理消息头 headers
 * @Author: SOYANGA
 * @Create: 2019-04-26 19:54
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/demo2", method = RequestMethod.GET)
public class Demo2Controller {

    /**
     * 用Http头信息进一步限制请求的范围，如果投中不含有Content-Type=text/html这个头信息则
     * 会报415  HTTP Status 415 – Unsupported Media Type 不支持参数错误。反之则请求正常处理
     *
     * @return 返回响应内容
     */
    @RequestMapping(value = "/header1", headers = {
            "Content-Type=text/html"
    })
    public String textHtml() {
        return "<h1>Hello world</h1>";
    }


    /**
     * 用Http头信息进一步限制请求的范围，如果投中不含有Content-Type=text/html或者Content-Type=text/plain这个头信息则
     * 会报415  HTTP Status 415 – Unsupported Media Type 不支持参数错误。反之则请求正常处理
     *
     * @return 返回响应内容
     */
    @RequestMapping(value = "/header2", headers = {
            "Content-Type=text/html",
            "Content-Type=text/plain"

    })
    public String textPlainHtml() {
        return "<h1>See world again</h1>";
    }
}
