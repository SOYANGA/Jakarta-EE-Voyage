package com.github.soyanga.springBootBasic.component;

/**
 * @program: springBoot-Basics
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-05-02 16:58
 * @Version 1.0
 */
public class ExampleBean2 {


    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //@Bean中的属性 bean的初始化方法
    public void init() {

    }

    //@Bean中的属性 bean的销毁方法
    public void destroy() {

    }
}
