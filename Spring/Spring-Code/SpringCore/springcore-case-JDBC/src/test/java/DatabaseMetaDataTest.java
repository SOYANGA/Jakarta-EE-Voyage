import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.*;

/**
 * @program: springcore-case-JDBC
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-22 19:10
 * @Version 1.0
 */
public class DatabaseMetaDataTest {
    private static ApplicationContext context;

    private static DataSource dataSource;
    private static Logger logger = LoggerFactory.getLogger(DatabaseMetaDataTest.class);

    @BeforeClass
    public static void beforeClass() {
        context = new ClassPathXmlApplicationContext("application-context.xml");
        dataSource = (DataSource) context.getBean("dataSource");
    }

    @Test
    public void test_databaseMetaDate() throws SQLException {
        Connection connection = dataSource.getConnection();

        DatabaseMetaData databaseMetaData = connection.getMetaData();
        System.out.println("supportsANSI92FullSQL " + databaseMetaData.supportsANSI92FullSQL()); //false
        System.out.println("supportsTransactions " + databaseMetaData.supportsTransactions()); //true
        System.out.println("supportsSavepoints " + databaseMetaData.supportsSavepoints());  //true
        System.out.println("supportsTransactionIsolationLevel " + databaseMetaData.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE));//true
    }

    @Test
    public void test_commit() {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataSource.getConnection();
            //关闭自动提交
            connection.setAutoCommit(false);
            //设置隔离级别
//            connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            //创建命令
            statement = connection.createStatement();
            //执行SQL
            int updateEffect = statement.executeUpdate("update soft_bookrack set book_name ='111' where book_isbn = '9787115417305'");

            int deletEffect = statement.executeUpdate("delete from soft_bookrack where book_isbn ='9787115417305'");

            //设置保存点
            Savepoint savepoint = connection.setSavepoint();
            //回滚到保存点上去
            connection.rollback(savepoint);
            //提交事务
            if (updateEffect == 1 && deletEffect == 1) {
                System.out.println("SQL commit");
                connection.commit();

            } else {
                System.out.println("SQL rollback");
                connection.rollback();
            }

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    //回滚事务
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }

        } finally {
            //释放资源
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

    @Test
    public void test_transactionTemplate() {
        //获取编程式事务管理模板
        TransactionTemplate transactionTemplate = (TransactionTemplate) context.getBean("transactionTemplate");


        Object retValue = transactionTemplate.execute(new TransactionCallback<Object>() {

            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                //需要在事务中执行的代码--事务控制
                //进行数据库访问操作
                DataSourceUtils.getConnection(dataSource); //在内部必须使用此方法获取数据库连接 -->Spring中连接管理时基于线程的
                return null;
            }
        });

    }


}
