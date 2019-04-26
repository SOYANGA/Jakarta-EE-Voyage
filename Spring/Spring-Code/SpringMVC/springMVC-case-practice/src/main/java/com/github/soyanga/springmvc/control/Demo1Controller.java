package com.github.soyanga.springmvc.control;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: springMVC-case-practice
 * @Description: @RequestMapping来处理生产和消费者
 * @Author: SOYANGA
 * @Create: 2019-04-26 11:54
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/demo1")
public class Demo1Controller {


    /**
     * 生产者 希望服务器响应为 json而格式
     * 期望返回一个JSON格式，所以produces中就是限定返回JSON格式
     *
     * @return JSON字符串
     */
    @RequestMapping(value = "/prod", produces = {
            "application/json"
    })
    @ResponseBody
    public String producer() {
        Map<String, String> map = new HashMap();
        map.put("current_time", new Date().toString());
        map.put("username", "jack");
        return new Gson().toJson(map);
    }


    /**
     * 生产者 希望服务器响应为 json而格式
     * 期望返回一个JSON格式，所以produces中就是限定返回JSON格式 当前返回的是Map格式但是
     * 与我们期望不符合，假如项目中没有添加JSON依赖，则浏览器访问时406 not acception
     * 反之有JSON依赖，将map转换为JSON后进行响应
     *
     * @return JSON字符串
     */
    @RequestMapping(value = "/prod2", produces = {
            "application/json"
    })
    @ResponseBody
    public Map producer2() {
        Map<String, String> map = new HashMap();
        map.put("current_time", new Date().toString());
        map.put("username", "jack");
        return map;
    }


    /**
     * 生产者 希望服务器响应为 json而格式
     * 期望返回一个JSON格式，所以produces中就是限定返回JSON格式 当前返回的是user格式但是
     * 与我们期望不符合，假如项目中没有添加JSON依赖，则浏览器访问时406 not acception
     * 反之有JSON依赖，将user转换为JSON后进行响应
     *
     * @return JSON字符串
     */
    @RequestMapping(value = "/prod3", produces = {
            "application/json"
    })
    @ResponseBody
    public User producer3() {
        User user = new User();
        user.setAge(18);
        user.setName("tom");
        user.setEmail("tom@163.com");
        return user;
    }


    /**
     * consumes消费者：希望客户端返回的是一个json格式
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/cons1", consumes = {
            "application/json"
    }, method = RequestMethod.POST)
    public User consumer1(@RequestBody User user) {
        user.setName("welcom " + user.getName());
        return user;
    }


    /**
     * 静态内部User类
     */
    public static class User {
        private String name;
        private Integer age;
        private String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
