package com.github.soyanga.springmvc.interceptor;

import com.github.soyanga.springmvc.control.UserController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @program: springMVC-case-practice
 * @Description: 自定义一个拦截器，检测是否登陆，限制未登录者的访问权限 HandlerInterceptorAdapter空实现了HandlerInterceptor接口 我们就可以选择需要的方法去编写
 * @Author: SOYANGA
 * @Create: 2019-04-28 21:30
 * @Version 1.0
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //可以通过自己的方式去拦截
        String uri = request.getRequestURI();
        if ("".equals(uri) || "/".equals(uri)) {
            System.out.println("当前访问的是欢迎页");
            return true;
        }
        HttpSession session = request.getSession();
        Object user = session.getAttribute(UserController.CURRENT_USER);
        System.out.println(user);
        if (user == null) {
            System.out.println("AuthInterceptor false");
            return false;
        } else {
            System.out.println("AuthInterceptor true");
            return true;
        }
    }
}
