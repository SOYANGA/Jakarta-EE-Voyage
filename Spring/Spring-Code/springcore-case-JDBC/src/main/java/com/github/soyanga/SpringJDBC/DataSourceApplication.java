package com.github.soyanga.SpringJDBC;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;

/**
 * @program: springcore-case-JDBC
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-19 21:17
 * @Version 1.0
 */
public class DataSourceApplication {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

        DataSource dataSource = context.getBean(DataSource.class);
        System.out.println(dataSource.getClass().getCanonicalName());
    }
}
