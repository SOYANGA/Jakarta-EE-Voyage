package com.github.soyanga.springmvc.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: springMVC-case
 * @Description: 默认控制器(欢迎页面)
 * @Author: SOYANGA
 * @Create: 2019-04-25 15:31
 * @Version 1.0
 */
@Controller
@RequestMapping
public class IndexController {

    @RequestMapping(value = {"","/index"}, method = {RequestMethod.GET})
    public ModelAndView hello() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("welcome");  //WEB_INF/Views/welcome.jsp
        return modelAndView;
    }
}
