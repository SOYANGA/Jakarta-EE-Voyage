package com.github.soyanga.springmvc.entity;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @program: springREST-case
 * @Description: 作为查询对象的java实体类 添加@XmlRootElemen就支持将Java类转成XML
 * @Author: SOYANGA
 * @Create: 2019-04-29 16:04
 * @Version 1.0
 */
@Data
@XmlRootElement
public class User {
    private String name;

    private Integer age;

    private String address;
}
