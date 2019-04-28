package com.github.soyanga.springmvc.control;

import lombok.Data;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @program: springMVC-case-practice
 * @Description: 2.1控制器方法签名
 * @Author: SOYANGA
 * @Create: 2019-04-27 22:18
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/controller")
public class ControllerMethodController {


    /**
     * 2.1.1@RequestHeader
     * 通过@RequestHeader获取请求头的Header对应的头信息并且作为参数
     *
     * @param encoding  "Accept-Encoding" 请求头参数
     * @param aliveTime "Keep-Alive"
     * @return
     */
    @RequestMapping("/header")
    public String header(
            @RequestHeader(value = "Accept-Encoding") String encoding,
            @RequestHeader(value = "Keep-Alive", required = false, defaultValue = "0") long aliveTime
    ) {
        return String.format("Accept-Encoding:%s,Keep-Alive:%d", encoding, aliveTime);
    }

    /**
     * 2.1.2@CookileValue
     * 通过@CookileValue获取session的name，value并且作为参数
     *
     * @param sessionId
     * @return
     */
    @RequestMapping("/cookie")
    public String cookie(
            @CookieValue(value = "JSESSIONID", required = false, defaultValue = "hahaha") String sessionId
    ) {
        return "Cookie JSESSIONID = " + sessionId;
    }

    /**
     * 定义两个静态内部类POJO类
     */
    @Data
    public static class User {
        private int id;
        private String name;
        private Address address;
        //省略getter setter
    }

    @Data
    public static class Address {
        private String name;
        private String code;
        //省略getter setter
    }


    /**
     * 2.1.3POJO类作为参数
     * http://localhost:8080/controller/pojo?id=1&name=SOYANGA&address.name=XIAN&address.code=7100
     * 将URL中的参数进行抽象及封装成一个POJO类返回对应
     * ControllerMethodController.User(id=1, name=SOYANGA, address=ControllerMethodController.Address(name=XIAN, code=7100))
     *
     * @param user
     * @return
     */
    //-->
    @RequestMapping("/pojo")
    public String pojo(User user) {
        //处理逻辑    
        return user.toString();
    }


    /**
     * 2.1.4ServletAPI主要是指控制器方法参数类型为Java Servlet API 中的类。
     * SpringMVC会自动进行request以及 response ,session的获取
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("servlet_handle_01")
    public String requestServletHandle01(
            HttpServletRequest request,
            HttpServletResponse response) {
        //处理逻辑    
        return "success";
    }

    @RequestMapping("servlet_handle_02")
    public String requestServletHandle02(HttpSession session) {
        //处理逻辑
        return "success";
    }

    /**
     * 2.3 控制器方法支持的返回值类
     * 第一步：
     *
     * <p>
     * 访问控制器中的任何一个请求处理方法栈，SpringMVC会先执行该方法
     * 并将返回值以user为键添加到模型中   即讲user作为模型初始化且暂存起来
     */
    //内部隐藏一个model<String,Object> 将 "user"做为key，而user对象作为value进行填充
    @ModelAttribute(value = "user")
    public User getUser() {
        User user = new User();
        user.setId(100);
        user.setName("Jack");
        Address address = new Address();
        address.setName("XIAN");
        address.setCode(String.valueOf(710000));
        user.setAddress(address);
        return user;
    }

    /**
     * 第二步：
     * 模型数据会赋值给User的入参，然后再根据HTTP请求的消息进一步填充覆盖User对象
     * 即进一步对user这个模型对象进行补充,或者更改
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/user")
    public String user(@ModelAttribute("user") User user) {
        user.setName("SOYANGA");
        return user.toString();
    }


    /**
     * SpringMVC作为导员，只做了将url映射到这个方法中，对服务器的请求处理以及响应的封装全部又我们手动完成
     * 用户极端情况下，SpringMVC无法做到某些事情的时候
     * @param request
     * @param response
     */
    @RequestMapping(value = "/servlet")
    public void servletApi(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charaset=utf-8");

        try {
            PrintWriter writer = response.getWriter();
            writer.append("<h1>hello<h1>");
            //请求转发
//            response.sendRedirect("/welcome");
        } catch (IOException e) {
            e.printStackTrace();
        }

//        //请求重定向
//        try {
//            request.getRequestDispatcher("/welcome").forward(request, response);
//        } catch (ServletException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


}


