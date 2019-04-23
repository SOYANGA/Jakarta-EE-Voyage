package com.github.soyanga;

/**
 * @program: springcore-case-IoC
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-11 00:09
 * @Version 1.0
 */
public class ExampleEmail {
    private String email;
    private String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ExampleEmail{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
