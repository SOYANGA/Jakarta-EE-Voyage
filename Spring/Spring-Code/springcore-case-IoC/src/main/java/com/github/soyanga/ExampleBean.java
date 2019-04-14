package com.github.soyanga;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: springcore-case-IoC
 * @Description: Bean的实例化的练习
 * @Author: SOYANGA
 * @Create: 2019-04-10 18:44
 * @Version 1.0
 */
public class ExampleBean {
    //默认无参构造
    public String currentDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
}
