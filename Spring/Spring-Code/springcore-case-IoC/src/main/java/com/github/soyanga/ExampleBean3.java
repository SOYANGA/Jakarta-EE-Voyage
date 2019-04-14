package com.github.soyanga;

import java.beans.ConstructorProperties;

/**
 * @program: springcore-case-IoC
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-10 20:11
 * @Version 1.0
 */
public class ExampleBean3 {
    private int age;
    private String name;

    @ConstructorProperties(value = {"age", "name"})
    public ExampleBean3(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }


    public String getName() {

        return name;
    }

    @Override
    public String toString() {
        return "ExampleBean3{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
