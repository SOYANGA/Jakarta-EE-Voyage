package com.github.soyanga.springmvc.control;

import com.github.soyanga.springmvc.control.exception.RequestParamterInvalidException;
import com.github.soyanga.springmvc.control.exception.UploadException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * @program: springMVC-case-practice
 * @Description: 全局异常处理
 * @Author: SOYANGA
 * @Create: 2019-04-28 17:16
 * @Version 1.0
 */
@ControllerAdvice
public class AppExecptionHandlerController {

    /**
     * 处理请求异常
     *
     * @return
     */
    @ExceptionHandler(value = RequestParamterInvalidException.class)
    public ModelAndView handlerRequestparameter() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error_message", "无效参数");
        modelAndView.setViewName("error");
        return modelAndView;
    }

    /**
     * 处理上传异常
     *
     * @return
     */
    @ExceptionHandler(value = UploadException.class)
    public ModelAndView handlerUploadException() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error_message", "上传错误");
        modelAndView.setViewName("error");
        return modelAndView;
    }

    /**
     * 全部异常都去处理
     *
     * @return
     */
    @ExceptionHandler()
    public ModelAndView handlerException(Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error_message", e.getMessage());
        modelAndView.setViewName("error");
        return modelAndView;
    }
}
