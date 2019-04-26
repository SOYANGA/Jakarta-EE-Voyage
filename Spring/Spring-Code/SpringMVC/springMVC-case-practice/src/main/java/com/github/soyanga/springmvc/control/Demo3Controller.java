package com.github.soyanga.springmvc.control;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: springMVC-case-practice
 * @Description: @RequestMapping来处理请求参数params
 * @Author: SOYANGA
 * @Create: 2019-04-26 20:23
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/demo3")
public class Demo3Controller {

    /**
     * 请求param中必须要有"group=10"还有"color=XXX且id!=1否则400 Bad Request
     * eg：http://127.0.0.1:8080/demo3/param1?color=red&group=10&id=2
     * 或者没有id这个参数即可: http://127.0.0.1:8080/demo3/param1?color=red&group=10
     *
     * @param color
     * @return
     */
    @RequestMapping(value = "/param1", params = {
            "group=10", "id!=1"
    }, method = RequestMethod.GET)
    public String param1(@RequestParam("color") String color) {
        return color;
    }
}
