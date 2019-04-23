package com.github.soyanga.SpringJDBC.Service;

import com.github.soyanga.SpringJDBC.jdbc.Book;
import org.springframework.stereotype.Service;

/**
 * @program: springcore-case-JDBC
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-22 23:49
 * @Version 1.0
 */

public interface BookService {

    Book queryBookname(int isbn);

    boolean addBook(Book book);

    boolean deleteBook(int isbn);
}
