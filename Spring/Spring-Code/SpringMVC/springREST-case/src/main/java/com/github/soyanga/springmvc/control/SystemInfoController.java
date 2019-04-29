package com.github.soyanga.springmvc.control;

import com.github.soyanga.springmvc.entity.User;
import com.google.gson.JsonObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: springREST-case
 * @Description: 服务器信息控制器 REST服务器构建-编写控制器 不需要前端与视图层
 * @Author: SOYANGA
 * @Create: 2019-04-29 15:38
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/system")
public class SystemInfoController {


    /**
     * @return text/plain 文本格式响应给客户端
     * 因为@RequestMapping中就已经包含@RequestBody这个注解，返回的内容将在正文中体现
     */
    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index() {
        return "Hello";//text plain
    }

    /**
     * @return 返回一个服务器的信息，会转换自动转为JSON格式进行返回  -HashMap格式
     */
    @RequestMapping(value = "/info", method = {RequestMethod.GET})
    public HashMap<String, String> info() {
        HashMap<String, String> data = new HashMap<>();
        data.put("date_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        Runtime runtime = Runtime.getRuntime();
        data.put("server_processor", String.valueOf(runtime.availableProcessors()));
        data.put("server_total_memory", runtime.totalMemory() / 1024 / 1024 + " MB");
        data.put("server_max_memory", runtime.maxMemory() / 1024 / 1024 + " MB");
        data.put("server_free_memory", runtime.freeMemory() / 1024 / 1024 + " MB");
        return data;
    }

    /**
     * @param user 自定义实体类
     * @return 自定义java实体类作为响应返回时会自动转化问JSON格式
     */
    @RequestMapping(value = "/user", method = {RequestMethod.POST})
    public User query(@RequestBody User user) {
        return user;
    }

    @RequestMapping(value = "/user", method = {RequestMethod.GET})
    public User queryByuserId() {
        User user = new User();
        user.setName("Soyanga");
        user.setAge(21);
        user.setAddress("XIAN");
        return user;
    }



}
