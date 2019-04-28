package com.github.soyanga.springmvc.control;

import com.github.soyanga.springmvc.control.exception.GreetingException;
import com.github.soyanga.springmvc.control.exception.RequestParamterInvalidException;
import com.github.soyanga.springmvc.control.exception.UploadException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @program: springMVC-case-practice
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-28 16:16
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/excption")
public class ExceptionController {

    /**
     * 将异常映射为HTTP状态码
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/greeting", method = {RequestMethod.GET})
    public String greeting(@RequestParam("username") String username) {
        if (StringUtils.isEmpty(username)) {
            throw new RequestParamterInvalidException();
        }
        return username;
    }


    /**
     * @param username
     * @return
     */
    @RequestMapping(value = "/greeting2", method = {RequestMethod.GET})
    public String greeting2(@RequestParam("username") String username) {
        throw new GreetingException("欢迎" + username + "发生错误");
    }

    @RequestMapping(value = "/greeting3", method = {RequestMethod.GET})
    public String greeting3() {
        throw new UploadException();
    }
    /**
     * 异常处理控制器方法
     * 不指定转用异常类，则在这个controller里面发生的所有异常均会由这个异常处理控制器处理
     * 返回异常显示器视图
     *
     * @param e
     * @return
     */
//    @ExceptionHandler(value = {GreetingException.class})
//    public ModelAndView grettingExceptionHandle(GreetingException e) {
//        String errorMessage = e.getMessage();
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("error_message", errorMessage);
//        modelAndView.setViewName("greeting_error");
//        return modelAndView;
//    }
}
