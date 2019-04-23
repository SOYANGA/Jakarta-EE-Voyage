package com.github.soyanga.SpringJDBC.Springlate;

import com.github.soyanga.SpringJDBC.jdbc.JdbcOperation;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @program: springcore-case-JDBC
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-21 16:50
 * @Version 1.0
 */
public class SpringJdbcApplication {
    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        JdbcTemplateOperation operation = (JdbcTemplateOperation) context.getBean("jdbcTemplateOperation");
        operation.addBook();
        operation.updateBook();
        operation.queryBook();
        operation.queryBookForList();
        operation.countBook();
        operation.queryBookByIsbn();
        operation.deleteBook();
    }
}
