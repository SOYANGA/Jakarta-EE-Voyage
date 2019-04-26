package com.github.soyanga.springmvc.control;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.awt.SunHints;

/**
 * @program: springMVC-case-practice
 * @Description: @RequestMapping 处理动态(模板) URI
 * @Author: SOYANGA
 * @Create: 2019-04-26 20:48
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/demo4")
public class Demo4Controller {

    /**
     * 将请求地址中的某个值作为参数属性获取下来
     * eg:http://127.0.0.1:8080/demo4//fetch/1
     * 则获取下来的id = 1
     * @param id
     * @return
     */
    @RequestMapping(value = "/fetch/{id}", method = RequestMethod.GET)
    public String uriVariable(@PathVariable(value = "id") String id) {
        return "fetch id = " + id;
    }

    /**
     * 将请求地址中的某个值作为参数属性获取下来 -可用正则表达式再次进行限定
     * eg:http://127.0.0.1:8080/demo4//fetch/SOYANGA/blog/111
     * 获取下来 则name = SOYANGA  id = 111
     * 其中id必须是数组否则 会报404 not found错误
     * @param name
     * @param id
     * @return
     */
    @RequestMapping(value = "/fetch/{name}/blog/{id:[0-9]+}", method = RequestMethod.GET)
    public String uriVariable2(
            @PathVariable(value = "name") String name,
            @PathVariable(value = "id") String id) {
        return "fetch name = " + name + "; blog id = " + id;
    }
}
