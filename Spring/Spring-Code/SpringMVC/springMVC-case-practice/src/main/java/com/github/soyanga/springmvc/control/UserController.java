package com.github.soyanga.springmvc.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * @program: springMVC-case-practice
 * @Description: 用户控制器
 * @Author: SOYANGA
 * @Create: 2019-04-25 20:19
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    private static final String CURRENT_USER = "current_user";

    //@RequestMapping(value = "/login", method = {RequestMethod.POST})
    //组合注解等价上面的注解
    @PostMapping(value = "/login",headers = {"Cache-Control"})
    //hraders：请求里面必须要有这个"Cache-Control"这个请求头
    //localhost:8080/user/login
    //引入@RequestParam注解，我们更为严格的控制请求，
    // 如果username或者password没有这两个参数则就不予以处理请求 required默认是true
    //没有添加defaultValue参数前：如果没有remind复选框这个参数，由于required设置的是false,所以没有这个参数请求照样执行
    //添加defaultValue后，如果没有remind复选框这个参数则，remind默认是0（不记住密码）
    public ModelAndView loginPost(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "remind", required = false, defaultValue = "0") String[] remind,
            HttpServletRequest request) {
        System.out.println("login username=" + username + ", password=" + password);

        //String[] remind = request.getParameterValues("remind");
        System.out.println(Arrays.toString(remind));

        HttpSession session = request.getSession();
        session.setAttribute(CURRENT_USER, username);

        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.addObject("username", new String(username.getBytes("ISO-8859-1"), "UTF-8"));
            modelAndView.addObject("password", new String(password.getBytes("ISO-8859-1"), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        modelAndView.setViewName("home");  //WEB_INF/Views/home.jsp
        return modelAndView;
    }

    //@RequestMapping(value = "/login", method = {RequestMethod.GET})
    //组合注解等价上面的注解
    @GetMapping(value = "/login")
    // localhost:8080/user/login
    public ModelAndView loginGet() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");  //WEB_INF/Views/index.jsp
        return modelAndView;
    }

    //@RequestMapping(value = "/logout", method = {RequestMethod.GET})
    //组合注解等价上面的注解
    @GetMapping(value = "/logout")
    public ModelAndView logout(HttpServletRequest request) {

        HttpSession session = request.getSession();
        session.removeAttribute(CURRENT_USER);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("welcome");  //WEB_INF/Views/welcome.jsp
        return modelAndView;
    }

}
