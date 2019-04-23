package com.github.soyanga.SpringJDBC.jdbc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @program: springcore-case-JDBC
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-21 16:50
 * @Version 1.0
 */
public class JdbcApplication {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

        JdbcOperation jdbcOperation = (JdbcOperation) context.getBean("jdbcOperation");

        jdbcOperation.addBook();
        jdbcOperation.updateBook();
        jdbcOperation.queryBook();
    }
}
