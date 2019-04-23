package com.github.soyanga.SpringJDBC.Service.impl;

import com.github.soyanga.SpringJDBC.Service.BookService;
import com.github.soyanga.SpringJDBC.jdbc.Book;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: springcore-case-JDBC
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-22 23:51
 * @Version 1.0
 */
@Service
@Transactional
//添加事务管理注解 属性
//1.类级注解，适用于类中所有public的方法
//@Transactional(readOnly = true,timeout = 1)
public class BookServiceImpl implements BookService {
    @Transactional(transactionManager = "transactionManagerA")
    @Override
    public Book queryBookname(int isbn) {
        return null;
    }

    @Override
    //2.提供额外的注解信息，它将覆盖1处的类级别注解
    @Transactional(transactionManager = "transactionManager")
    //添加事务管理
    public boolean addBook(Book book) {
        return false;
    }

    @Override
    public boolean deleteBook(int isbn) {
        return false;
    }
}
