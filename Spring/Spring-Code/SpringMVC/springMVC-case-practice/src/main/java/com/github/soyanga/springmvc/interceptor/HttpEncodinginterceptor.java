package com.github.soyanga.springmvc.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: springMVC-case-practice
 * @Description: 自定义一个拦截器，设置响应编码为UTF-8
 * @Author: SOYANGA
 * @Create: 2019-04-28 21:21
 * @Version 1.0
 */
public class HttpEncodinginterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        response.setCharacterEncoding("UTF-8");
        System.out.println("HttpEncodinginterceptor preHandle");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

    }
}
