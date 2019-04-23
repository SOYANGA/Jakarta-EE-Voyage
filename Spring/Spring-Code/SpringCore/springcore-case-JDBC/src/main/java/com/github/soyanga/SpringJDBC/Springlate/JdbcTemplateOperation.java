package com.github.soyanga.SpringJDBC.Springlate;

import com.github.soyanga.SpringJDBC.jdbc.Book;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @program: springcore-case-JDBC
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-21 17:07
 * @Version 1.0
 */
@Component
@Data
public class JdbcTemplateOperation {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateOperation(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 添加一本书
     */
    public void addBook() {
        int effect = this.jdbcTemplate.update(
                "insert into `soft_bookrack` (book_name, book_author, book_isbn) values (?, ?, ?)"
                , "Spring in Action",
                "Craig Walls",
                "9787115417306"
        );
        System.out.println("Add book result" + effect);

    }

    public void deleteBook() {
        int effect = this.jdbcTemplate.update(
                "delete from soft_bookrack where book_isbn=?",
                "9787115417306"
        );
        System.out.println("Delete book result" + effect);
    }


    /**
     * 更新一本书
     */
    public void updateBook() {
        int effect = this.jdbcTemplate.update("update `soft_bookrack` set book_author=? where book_isbn=?",
                "张卫滨",
                "9787115417306"
        );
        System.out.println("Update book result" + effect);
    }

    /**
     * 查询一本书
     * 封装成一个Map集合
     */
    public void queryBook() {
        //结果集封装一个Map
        List<Map<String, Object>> bookList = this.jdbcTemplate.queryForList(
                "select book_name, book_author, book_isbn from soft_bookrack where book_isbn = ?;",
                "9787115417306"
        );
        System.out.println(bookList);
    }


    /**
     * 查询一本书
     * 封装成一个BookList列表
     */
    public void queryBookForList() {
        List<Book> bookList = this.jdbcTemplate.query(
                "select book_name, book_author, book_isbn from soft_bookrack;",
                new Object[]{}, new RowMapper<Book>() {
                    @Override
                    public Book mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                        Book book = new Book();
                        book.setName(resultSet.getString("book_name"));
                        book.setAuthor(resultSet.getString("book_author"));
                        book.setIsbn(resultSet.getString("book_isbn"));
                        return book;
                    }
                }
        );
        System.out.println(bookList);
    }


    /**
     * 查询数据个数  --int
     *
     * @return int
     */
    public int countBook() {
        int count = this.jdbcTemplate.queryForObject("select count(book_isbn)from soft_bookrack",
                Integer.class);
        System.out.println("count book =  " + count);
        return count;
    }

    /**
     * 根据isbn查询书籍
     * 错误用法，queryForObject返回值是单行单列的
     *
     * @return 返回查找到的书对象
     */
    public List<Map<String, Object>> queryBookByIsbn() {
        List<Map<String, Object>> bookList = this.jdbcTemplate.queryForList("select book_name,book_author,book_isbn from soft_bookrack where book_isbn=?",
                "9787115417305");
        System.out.println("queryBookByIsbn book =  " + bookList);
        return bookList;
    }
}
