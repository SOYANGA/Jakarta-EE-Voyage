package com.github.soyanga.springmvc.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @RequestMapping(value = {"", "/index"}, method = {RequestMethod.GET})
    public ModelAndView hello() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("welcome");  //WEB_INF/Views/welcome.jsp
        return modelAndView;
    }

    /**
     * 当在@Controller的类中的方法返回的类型是String类型时，SpringMVC会将你的返回的值在结尾添加.jsp在
     * WEB-INF/views/welcome.jsp 目录下找作为响应的view，但是当你用@ResponseBody或者类是用@RestController修饰时
     * 返回值将将作为单纯的字符串进行返回
     *
     * @return 字符串/或者view
     */
    @RequestMapping(value = "/welcome", method = {RequestMethod.GET})
    @ResponseBody
    public String welcome() {
        return "welcome"; //WEB-INF/views/welcome.jsp
    }
}
