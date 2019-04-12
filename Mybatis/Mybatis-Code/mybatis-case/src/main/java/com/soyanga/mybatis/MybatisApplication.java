package com.soyanga.mybatis;

import com.soyanga.mybatis.entity.ScottGroup;
import com.soyanga.mybatis.mapper.ScottGroupMapper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

/**
 * @program: mybatis-case
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-03-28 21:10
 * @Version 1.0
 */
public class MybatisApplication {

    private static SqlSessionFactory sqlSessionFactory;

    public static void buildSqlSessionFactoryWithXML() {
        //        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(MybatisApplication.class.getResourceAsStream("mybatis-config.xml"));

        try {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(
                    Resources.getResourceAsStream("mybatis-config.xml")
            );
//            System.out.println(sqlSessionFactory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void buildSqlSessionFactoryWithJava() {
        Configuration configuration = new Configuration();
        DataSource dataSource = new PooledDataSource(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost:3306/scott",
                "root",
                "123456789"
        );
        Environment environment = new Environment("div", new JdbcTransactionFactory(), dataSource);
        configuration.setEnvironment(environment);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        System.out.println(sqlSessionFactory);
    }


    public static final Logger LOGGER = LoggerFactory.getLogger(MybatisApplication.class);

    public static void main(String[] args) {
//        DataSource dataSource = new PooledDataSource();
//        TransactionFactory transactionFactory = new JdbcTransactionFactory();
//        Environment environment = new Environment("development", transactionFactory, dataSource);
//        Configuration configuration = new Configuration(environment);
//        configuration.addMapper(ScottGroupMapper.clss);
//        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
//        buildSqlSessionFactoryWithJava();
        buildSqlSessionFactoryWithXML();
        SqlSession sqlSession = sqlSessionFactory.openSession(true); //true是自动提交  反之需要自己提交
        ScottGroup scottGroup = new ScottGroup();
        scottGroup.setDeptno(1314);
        scottGroup.setDname("soyanga");
        scottGroup.setLoc("homeTone");
//        int effect = sqlSession.insert("com.soyanga.mybatis.mapper.ScottGroupMapper.insertScottGroup", scottGroup);
//        System.out.println(effect);
//        sqlSession.close();

//        插入
        ScottGroupMapper scottGroupMapper = sqlSession.getMapper(ScottGroupMapper.class);
        int effect = scottGroupMapper.insertScottGroup(scottGroup);
        System.out.println(effect);
        sqlSession.close();
        //查询
//        List<ScottGroup> scottGroupList = scottGroupMapper.query();
////        System.out.println(scottGroupList);
//        LOGGER.debug("查询结果是:{}", scottGroupList);
        sqlSession.close();
    }
}
