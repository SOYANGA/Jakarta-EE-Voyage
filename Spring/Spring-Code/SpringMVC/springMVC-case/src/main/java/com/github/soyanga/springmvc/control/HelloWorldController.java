package com.github.soyanga.springmvc.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: springMVC-case
 * @Description: 控制器方法
 * @Author: SOYANGA
 * @Create: 2019-04-25 15:31
 * @Version 1.0
 */
@Controller
@RequestMapping
public class HelloWorldController {

    @RequestMapping(value = {"", "/index"}, method = {RequestMethod.GET})
    public ModelAndView hello() {
        ModelAndView modelAndView = new ModelAndView();

//        modelAndView.addObject("greetting_message",
//                "Welcome to at" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        modelAndView.setViewName("index");  //WEB_INF/Views/index.jsp
        return modelAndView;
    }


    @RequestMapping(value = {"/login"}, method = {RequestMethod.POST})
    public ModelAndView login(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("login username=" + username + ", password=" + password);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("username", username);
        modelAndView.addObject("password", password);

        modelAndView.setViewName("home");  //WEB_INF/Views/home.jsp
        return modelAndView;
    }


//    @RequestMapping(value = {"", "/login"}, method = {RequestMethod.GET})
//    public ModelAndView loginView() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("login");  //WEB_INF/Views/login.jsp
//        return modelAndView;
//    }
}
