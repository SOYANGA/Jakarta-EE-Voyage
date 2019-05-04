package com.github.soyanga.springBootBasic.control;

import com.github.soyanga.springBootBasic.component.ExampleBean;
import com.github.soyanga.springBootBasic.config.AppConfig;
import com.github.soyanga.springBootBasic.config.BookProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: springBoot-Basics
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-05-02 17:05
 * @Version 1.0
 */
@RestController
@RequestMapping
public class indexController {

    //自动注入
    @Autowired
    public ExampleBean exampleBean;


    @Autowired
    private AppConfig appConfig;

    //自动注入enviroment Spring自带的bean --->
    @Autowired
    private Environment environment;

    //自动注入bookProperties属性配置
    @Autowired
    private BookProperties bookProperties;


    @RequestMapping(value = "/appconfig", method = {RequestMethod.GET})
    public String appconfig() {
        return appConfig.toString();
    }

    /**
     * 获取enviorment属性 从中获取系统的环境信息
     *
     * @return
     */
    @RequestMapping(value = "/environment", method = {RequestMethod.GET})
    public Map<String, Object> environment() {
        Map<String, Object> map = new HashMap<>();
        map.put("app.config.host", environment.getProperty("app.config.host"));
        map.put("java.home", environment.getProperty("java.home"));
        map.put("usr.dir", environment.getProperty("usr.dir"));
        return map;
    }


    /**
     * 获取book属性 从中获取配置中获取
     *
     * @return
     */
    @RequestMapping(value = "/book", method = {RequestMethod.GET})
    public BookProperties bookProperties() {
        return bookProperties;
    }

}
