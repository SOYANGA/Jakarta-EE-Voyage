package com.github.soyanga;

/**
 * @program: springcore-case-IoC
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-12 00:27
 * @Version 1.0
 */
public class Customer {
    private final Bar bar;

    public Customer(Bar bar) {
        this.bar = bar;
    }

    public Bar getBar() {
        return bar;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "bar=" + bar +
                '}';
    }
}
