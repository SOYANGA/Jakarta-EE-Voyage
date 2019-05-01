package com.github.soyanga.springBootDemo1.control;


import com.github.soyanga.springBootDemo1.config.PayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;


/**
 * @program: springBootDemo1
 * @Description: 欢迎页面
 * @Author: SOYANGA
 * @Create: 2019-05-01 12:37
 * @Version 1.0
 */
@RestController
@RequestMapping
public class IndexController {

    @Autowired
    public DataSource dataSource;

    @Autowired
    public PayProperties payProperties;

    @RequestMapping(value = {"", "/index"}, method = RequestMethod.GET)
    public String index() {
        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            return metaData.getDatabaseProductName();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }


    @RequestMapping(value = "/pay", method = {RequestMethod.GET})
    public String payInfo() {
        return payProperties.toString();
    }
}
