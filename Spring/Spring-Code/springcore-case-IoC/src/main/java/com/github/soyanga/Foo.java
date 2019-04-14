package com.github.soyanga;

/**
 * @program: springcore-case-IoC
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-10 20:00
 * @Version 1.0
 */
public class Foo {
    private final Bar bar;
    private final Bazz baz;

    public Foo(Bar bar, Bazz baz) {
        this.bar = bar;
        this.baz = baz;
    }

    @Override
    public String toString() {
        return "Foo{" +
                "bar=" + bar +
                ", baz=" + baz +
                '}';
    }
}
