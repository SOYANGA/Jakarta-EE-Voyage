package com.github.soyanga.SpringJDBC.jdbc;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: springcore-case-JDBC
 * @Description: javaSE-JDBC
 * @Author: SOYANGA
 * @Create: 2019-04-21 16:14
 * @Version 1.0
 */
@Component
public class JdbcOperation {
    private final DataSource dataSource;

    public JdbcOperation(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 添加一本书
     */
    public void addBook() {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            //创建连接
            connection = dataSource.getConnection();
            //创建命令
            statement = connection.prepareStatement
                    ("insert into `soft_bookrack` (book_name, book_author, book_isbn) values (?, ?, ?)");
            //赋值参数
            statement.setString(1, "Spring in Action");
            statement.setString(2, "Craig Walls");
            statement.setString(3, "9787115417305");
            //执行语句
            int effect = statement.executeUpdate();
            System.out.println("Execute Result " + effect);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 更新一本书
     */
    public void updateBook() {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            //创建连接
            connection = dataSource.getConnection();
            //创建命令
            statement = connection.prepareStatement
                    ("update `soft_bookrack` set book_author=? where book_isbn=?");
            //赋值参数
            statement.setString(1, "张卫滨");
            statement.setString(2, "9787115417305");
            //执行语句
            int effect = statement.executeUpdate();
            System.out.println("Execute Result " + effect);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 查询一本书
     */
    public void queryBook() {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Book book = null;
        List<Book> bookList = new ArrayList<>();
        try {
            //创建连接
            connection = dataSource.getConnection();
            //创建命令
            statement = connection.prepareStatement
                    ("select book_name, book_author, book_isbn from soft_bookrack where book_isbn = ?");
            //赋值参数
            statement.setString(1, "9787115417305");
            //执行语句-返回结果集
            resultSet = statement.executeQuery();
            //讲结果集添加到List中去，打印结果集
            while (resultSet.next()) {
                book = new Book();
                book.setName(resultSet.getString("book_name"));
                book.setAuthor(resultSet.getString("book_author"));
                book.setIsbn(resultSet.getString("book_isbn"));
                bookList.add(book);
            }
            System.out.println(bookList);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally { //释放资源
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
