package com.github.soyanga.SpringJDBC.template;

/**
 * @program: springcore-case-AOP
 * @Description: JDBC模板例子 抽象类
 * @Author: SOYANGA
 * @Create: 2019-04-19 17:24
 * @Version 1.0
 */
public abstract class OneDay {

    public abstract void wash();

    public abstract void breakfast();

    public final void study() {
        System.out.println("Morning 204-java");
    }

    public abstract void lunch();

    public final void work() {
        System.out.println("noon 204-JavaSE-Mybatis-Spring");

    }

    public abstract void dinner();

    public abstract void sleep();

    public final void onDayThing() {
        this.wash();
        this.breakfast();
        this.study();
        this.lunch();
        this.work();
        this.dinner();
        this.sleep();
    }

}
