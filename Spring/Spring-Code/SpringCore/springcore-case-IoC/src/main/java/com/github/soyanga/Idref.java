package com.github.soyanga;

/**
 * @program: springcore-case-IoC
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-10 23:37
 * @Version 1.0
 */
public class Idref {
    private String id;

    public Idref(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Idref{" +
                "id='" + id + '\'' +
                '}';
    }
}
