package com.github.soyanga.springmvc.control;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;

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

    public static final String CURRENT_USER = "current_user";

    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    //组合注解等价上面的注解
    //@PostMapping(value = "/login", headers = {"Cache-Control"})
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
            HttpSession session,
            HttpServletResponse response) {
        System.out.println("login username=" + username + ", password=" + password);
        //String[] remind = request.getParameterValues("remind");
        System.out.println(Arrays.toString(remind));
        ModelAndView modelAndView = new ModelAndView();
        //如果用户未输入用户名或者密码则用户需要重新登陆
        if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
            session.setAttribute(CURRENT_USER, username);

            //记住密码则将用户名放入Cookie中，响应给浏览器，浏览器进行存储，让登陆过的用户下次进入直接到登陆页面，不会走登陆请求了
            if ("1".equals(remind[0])) {
                Cookie cookie = new Cookie("remind", username);
                //设置cookie持久化时间为半小时
                cookie.setMaxAge(3600);
                response.addCookie(cookie);
            }
            try {
                modelAndView.addObject("username", new String(username.getBytes("ISO-8859-1"), "UTF-8"));
                modelAndView.addObject("password", new String(password.getBytes("ISO-8859-1"), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            modelAndView.setViewName("home");  //WEB_INF/Views/home.jsp
            return modelAndView;
        } else {
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }

    //@RequestMapping(value = "/login", method = {RequestMethod.GET})
    //组合注解等价上面的注解
    @GetMapping(value = "/login")
    // localhost:8080/user/login
    public ModelAndView loginGet(@CookieValue(value = "remind", required = false, defaultValue = "") String username) {
        ModelAndView modelAndView = new ModelAndView();
        if (StringUtils.isEmpty(username)) {
            modelAndView.setViewName("login");  //WEB_INF/Views/index.jsp
        } else {
            modelAndView.addObject("username", username);
            modelAndView.setViewName("home");
        }
        return modelAndView;
    }


    //@RequestMapping(value = "/logout", method = {RequestMethod.GET})
    //组合注解等价上面的注解
    @GetMapping(value = "/logout")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        //打印sessio域中的用户名称
        System.out.println(session.getAttribute(CURRENT_USER));
        //当用户记住密码但是退出的时候，就需要将记住的密码Cookie重置，当用户不点击退出，则用户就一在30分钟内不用重新登陆
        session.removeAttribute(CURRENT_USER);
        Cookie cookie = new Cookie("remind", "");
        response.addCookie(cookie);
        //销毁session
        //session.invalidate();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("welcome");  //WEB_INF/Views/welcome.jsp
        return modelAndView;
    }


    /**
     * 转到上传请求页面
     *
     * @return
     */
    @RequestMapping(value = "/upload", method = {RequestMethod.GET})
    public String upload() {
        return "upload";
    }


    /**
     * 将表单提交的数据("head")存储在内存中何使用 Multipart解析器对 请求数据进行解析并存储
     * 然后将数据进行Base65编码生成字符串
     * 然后将字符串拼接上data:image/%s;base64,%s 数据类型 + base64文件编码 生成Base64编码的完整格式。
     * 最后响应给请求页面
     *
     * @param multipartFile
     * @return
     */
    @RequestMapping(value = "/upload", method = {RequestMethod.POST})
    public ModelAndView upload(@RequestPart(value = "head") MultipartFile multipartFile) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            byte[] data = multipartFile.getBytes();
            String dataStr = Base64.getEncoder().encodeToString(data);
            //data:image/png;base64,{}
            String extend = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
            String base64Value = String.format("data:image/%s;base64,%s", extend, dataStr);
            modelAndView.addObject("head_data", base64Value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        modelAndView.setViewName("upload");
        return modelAndView;
    }
}
