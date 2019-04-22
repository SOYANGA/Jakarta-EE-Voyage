# Spring数据库访问

**重点**

> - 理解Spring的数据库访问模块化
> - 掌握Spring中使用的JDBC
> - 掌握Spring中的事务管理

## 1.Spring的数据库访问

企业级应用离不开数据库的访问操作，我们在处理数据库访问的时候**必须经过初始化数据库的驱动，获得连接，执行SQL，处理各种异常和关闭连接**。这样重复操作过程中出现任何问题，都有可能造成数据的损坏。因此，Spring框架为了解决此类问题，自带了一组数据库访问框架。

### 1.1数据库访问哲学

Spring的目标之一就是允许我现在开发应用时，能够遵循OO原则中的“针对接口编程”。Spring数据库访问正是如此。

为了避免持久化的逻辑分散到应用的各个组件，**最好的方式就是将数据库访问的功能放到一个或者多个专注于此象任务的组件中**，这样的组件通常被称为：**数据库访问对象(Data Asscess Object DAO)**或者Reposutory**，为了避免应用于特定的数据访问策略耦合在一起，编写良好的Repository应该以接口的方式暴露功能。**

![1555660892791](D:\婕\JavaEE学习之路\Spring\picture\数据库访问哲学-接口.png)



- 服务对象本身不会处理数据访问，而是将数据访问委托给Respository
- Repository接口确保其于服务对象的松耦合

**服务对象通过接口访问Respority有以下两种好处：**

​	==**便于服务对象测试 **== 

1. 使得服务对象易于测试，服务对象不再与特定的数据访问实现绑定在一起。实际上可以为这些数据访问接口创建**Mock(模拟)实现**,这样**无需来连接数据库就能测试服务对象**，而且显著**提升了单元测试的效率**，并**排除因数据不一致所造成的测试的失败**。

   ==**便于改动**== 数据库访问与持久化技术 无关 持久化技术独立出来，易于扩展

2. **访问数据库访问层是以 持久化技术无关 的方式进行访问的**。持久化方式的选择独立于Resporitory，同时只有**数据库访问相关的方法才能通过接口进行暴露**，这样实现灵活的设计，**并且切换持久化框架对应用程序其他部分带来的影响最小。**  

### 1.2数据库访问异常体系

#### 1.2.1JDBC异常

JDBC中代码需要强制捕获SQLException.SQLException表示在尝试访问数据库时出现了问题，但是这个异常并没有告诉我们哪里处理问题，以及如何处理。

可能抛出SQLExecption的常见问题包括：

- **应用程序无法连接到数据库** 
- **要执行的查询存在语法错误 ** 
- **查询中使用的表或列不存在 ** 
- **试图插入或者更新的数据违反了数据库表约束** 

SQLException的问题在于捕获到它的时候如何处理。事实上，能够触发SQLException的问题。**通常是不能在catch代码块中解决的** 无法在catch时无法得知由什么引发了异常。大多数抛出SQLException情况表明了发生了致命性错误。比如:应用程序不能连接到数据库，这意味着应用不能继续使用了。 

JDBC异常面临的两个问题： 

- **对所有数据库访问问题都会抛出SQLException  ** 
- **不是针对每种可能的问题有不同的异常类型** 

JDBC异常类(部分异常):

![JDBC异常体系](D:\婕\JavaEE学习之路\Spring\picture\JDBC异常体系.png)



#### 1.2.2平台无关的持久化异常

SpringJDBC提供的数据库访问异常体系解决了以上两个问题。不同于JDBC,Spring提供了多个数据库访问异常，分别描述了他们抛出时所对应的问题。

SpringJDBC异常类（部分异常）

![1555662197704](D:\婕\JavaEE学习之路\Spring\picture\SpringJDBC异常类部分异常.png)

尽管Spring的异常体系比JDBC简单的SQLException丰富的多，但**是它并没有于特定的持久化方式相关联**。这意味 着我们可以使用Spring抛出一致的异常，而**不用关心所选择的持久化方案**。这有助于我们将所选择**持久化机制与数据库访问层隔离开来**。

**不再使用catch代码块** 

### 1.3数据访问模板化

Spring将数据库访问过程中**固定的和可变的**部分明确划分为两个不同的类：**模板(template)**和**回调(callback)。**

模板管理过程中固定的风各，而回掉处理自定义的数据访问代码。

![1555664570561](D:\婕\JavaEE学习之路\Spring\picture\Spring数据访问模板化.png)

**Spring的数据访问模板负责通用的数据访问功能，对于应用程序特定任务，则会调用==自定义回掉方==法。** 

如图所示，Spring的模板类处理的数据库访问的固定部分—-**事务控制**，**管理资源以及处理异常**。同时，**应用程序相关的数据访问—-语句，绑定参数以及处理结果集在回调的实现中处理**。事实证明，这是一个优雅的架构，我们只**需要关注自己的数据访问逻辑**即可。

![1555664726519](D:\婕\JavaEE学习之路\Spring\picture\Spring数据访问模板.png)

重点学习**Jdbc Template模板**。在使用Spring提供的各种模板类的**前提条件是依赖数据源**。**在声明模板和Respository之前**，需要在**Spring中配置数据源来连接数据库。**

## 2.配置数据源

无论选择Spring的那种数据访问方式，都需要配置一个数据源的引用。**Spring提供了Spring上下文中配置数据源Bean的多种方式**，包括：

- **通过JDBC驱动程序定义数据源**
- 通过JNDI查找的数据库
- **连接池的数据源**

2.1数据源连接池

Spring为提供数据源连接池的实现，我们可以通过其他开源实现来进行数据源的配置，下面是常见的几种数据源的连接池的开源实现

- Apache Commons DBCP2 (http://commons.apache.org/proper/commons-dbcp/download_dbcp.cgi) 
- c3p0 (http://www.mchange.com/projects/c3p0/) 
- BoneCP (http://jolbox.com/) 
- **HikariCP** (https://github.com/brettwooldridge/HikariCP)
-  **Druid** （https://github.com/alibaba/druid）

这些数据库连接池大多都能配置为Spring的数据源，在一定程度上与Spring自带的DriverManagerDataSource或者SingleConnectionDataSource很类似。

```xml
        <!--阿里 druid数据库连接池-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.2</version>
        </dependency>
```

```xml
<!--获取数据源-->
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
        <property name="driverClassName" value="${jdbc.driverClass}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>

        <!--连接池的连接配置 初始化大小， 最小，最大-->
        <property name="initialSize" value="10"/>
        <property name="maxActive" value="20"/>
        <property name="minIdle" value="10"/>
        <property name="queryTimeout" value="5"/>

        <!--配置获取连接等待超时的时间-->
        <property name="maxWait" value="10000"/>
        <!--配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒-->
        <property name="timeBetweenEvictionRunsMillis" value="60000"/>
        <!--配置一个连接在连接池中最生存时间，单位是毫秒-->
        <property name="minEvictableIdleTimeMillis" value="300000"/>

        <property name="validationQuery" value="SELECT 'x' FROM DUAL"/>
        <property name="testWhileIdle" value="true"/>
        <property name="testOnBorrow" value="false"/>
        <property name="testOnReturn" value="false"/>

        <!--打开PSCache ,并且指定每个连接上·PSCache的大小，如果Oracle,则把poolPreparedstatements
        配置为true,mysql可以配置为false-->
        <property name="poolPreparedStatements" value="false"/>
        <property name="maxPoolPreparedStatementPerConnectionSize" value="20"/>

    </bean>
</beans>
```

### 2.2JDBC驱动的数据源

在Spring中，通过JDBC驱动定义数据源是最简单的配置方式，SpringJDBC位于Spring-jdbc模板下：

```
<!--Spring JDBC-->
<dependency>
     <groupId>org.springframework</groupId>
     <artifactId>spring-jdbc</artifactId>
</dependency>
```

Spring提供了三个这样的数据源（位于包:`org.Spirngframework,jdbc.dataSource`下）。

- `DriverMangerDataSource`：在每个连接请求时都会返回一个新建的连接。与Druid的DruidDataSource不同，由DriverMangerDataSource提供的连接**并没有进行池化管理**。

- `SimpleDriverDataSource`:与DriverManagerDataSource的工作方式类似，但是**直接使用JDBC驱动，来解决在特定环境下的类加载问题**，这样的环境包括**OSGI容器**。
- `SingleConnectionDataSource`：在每个连接请求时都会返回同一个连接，尽管SingleConnectionDataSource不是严格意义上的连接池数据源，但是可以将其视为**只有一个连接的池**。

DriverManagerDataSource配置：

```xml
    <!--通过驱动获取数据源-->
    <bean id="driverManagerDataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="${jdbc.driverClass}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>
```

与具备池功能的数据源相比，唯一区别在于这**些数据源Bean都没有提供连接池的功能，所以没有可配置的池相关的属性**。

## 3Spring中使用JDBC

### 3.1JDBC代码--JavaSE中学习的JDBC

```sql
-- 创建数据库 create database if not exists `library` default character set utf8; 
-- 创建表 create table if not exists `soft_bookrack` (  
`book_name` varchar(32) NOT NULL,  
`book_author` varchar(32) NOT NULL,  
`book_isbn` varchar(32) NOT NULL primary key 
) ;
```



```java
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
                    ("update `soft_bookrack` set book_author=? where book_isbn=?;");
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
                    ("select book_name, book_author, book_isbn from soft_bookrack where book_isbn = ?;");
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

@Data
public class Book {
    private String name;
    private String author;
    private String isbn;
}

```

上面冗长的代码，甚至非常复杂。其中只有20%的代码真正用于业务功能，其余80%的代码都是样板代码。不过， 这些样板代码非常重要，清理资源和处理错误确保了数据访问的健壮性，避免了资源的泄露。
基于上面的原因，我们才需要框架来确保这些样板代码只写一次而且是正确的。 

### 3.2使用JDBC模板

为了简化JDBC代码。Spring的JDBC框架承担了**资源管理和异常处理**的工作，从而简化了JDBC代码，让我们只需要编写从数据库读写数据的必须的代码。

下面我们把JDBC代码改造成为使用JdbcTemplate访问数据。

配置JdbcTemplate的Bean:

```java
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




```

看到改造之后的代码，第一感觉就是简洁，**代码都是围绕业务编写的**。但是需要明确一点是，虽然看不到样板代码了，但不代表其不存在，这样样板代码只是巧妙地隐藏到**JDBC模板类**中了

## 4.Spring的事务管理

在使用Spring开发应用时，Spring的事务管理使用频率高，应用最广的功能。Spring不但提供了和底层事务源无关的**事务抽象**，还提供了**声明式事务的功能**，可以让程序从事务代码中解放出来。

### 4.1数据库事务回顾

#### 4.1.1什么式数据库的事务

“一荣俱荣，一损俱损”体现了事务的思想，很复杂的事务要分步进行，但是它们组成了**一个整体**，要**么整体生效，要么整体失效**。这种思想反映到数据库上，就是一组SQL语句，要么所有执行成功，要么所有执行失败。

#### 4.1.2数据库事务的特性(ACID)

- 原子性(Atomic)
- 一致性(Consistency)
- 隔离性(Isolation)
- 持久性(Durabiliy)

#### 4.1.3事务的隔离级别

ANX/ISO SQL92标准定义了4各等级的事务的隔离级别，在相同的数据环境下，使用相同的输入，执行相同的工作，根据不用的隔离级别，可能导致不同的结果。**不同的事务的隔离级别能够解决数据库并发问题的能力不同。**

![事务隔离级别](D:\婕\JavaEE学习之路\Spring\picture\事务隔离级别.png)

事务的隔离级别和数据库的并发性式对立的。一般来说，使用`READ UNCOMMITED`隔离级别的数据库拥有最高的并发性和吞吐量，而使用`SERIAILIZABLE`隔离级别的数据库并发性最低。

SQL92推荐使用`REPEATABLE READ`以保证数据库的读一致性，不过用户可以根据应用的需要选择合适的隔离等级。

> 备注:MySQL数据库的默认隔离级别等级是`REPEATABLE READ` 

#### 4.1.4JDBC对事务的支持

- 查询数据库的支持情况

并不是所有数据库都支持事务，即支持事务的数据库也并非支持事务的隔离级别。用户可以通过`Connection#getMetaData()`方法获取`DatabaseMetaData`对象，并通过该对象的`supportsTransactionIsolationLevel(int level)`方法查看底层数据库的事务支持情况。

检查示例代码：

```java
    @Test
public void test_databaseMetaDate() throws SQLException {
    Connection connection = dataSource.getConnection();

    DatabaseMetaData databaseMetaData = connection.getMetaData();
    System.out.println("supportsANSI92FullSQL " + databaseMetaData.supportsANSI92FullSQL()); //false
    System.out.println("supportsTransactions " + databaseMetaData.supportsTransactions()); //true
    System.out.println("supportsSavepoints " + databaseMetaData.supportsSavepoints());  //true
    System.out.println("supportsTransactionIsolationLevel " + databaseMetaData.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE));//true
}
```

- 编程式处理事务逻辑

  Connection默认情况下是自动提交的，即每条执行SQL语句都对应一个事务。为了将一组SQL语句当成一个事务执行，必须先通过`Connection#setAutoCommit(false)`阻止`Connection`自动提交，并且通过`Connection#setTransactionIsoation()`设置事务的隔离级别(注:Connection定义对应的SQL92标准4个事务隔离界别的常量)。通过Connection#commit()提交事务，Connection#rollback()回滚事务。

  编程式控制事务代码：

  ```java
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
  }
  ```



### 4.2事务管理器的实现

#### 4.2.1事务管理概述

- Spring为事务管理提供了**一致性的编程模板**，在高层次建立了统一的事务抽象。也就是说，不管选择Spring JDBC，Hibernatem，JAP还是Mybatis，Spring都可以让用户通过**统一的编程模型进行事务管理**。
- 像Spring DAO为不同的持久化实现提供了模板类一样，Spring事务管理继承了这一风格，也提供了事务模板类的**TransactionTemplate**。通过TransactionTemplate并配合使用事务回调**TransactionCall**指定的具体的持久化操作，就可以通过编程式实现事务的管理(即:**编程式事务管理**)
- Spring事务管理的亮点在于**声明式事务管理**。Spring允许通过声明方式，在IoC容器配置中**指定事务边界，事务属性**，Spring自动在指定的事务边界上应用事务属性。
- Spring的事务处理高明之处在于无论使用那种持久化技术，是否使用JTA事务，都**采用相同的事务管理模型**。这种统一的处理方式所带来的好处很大，开发者完全可以抛开事务管理的问题编写程序，**并在Spring中通过配置完成事务的管理**工作。

#### 4.2.2事务管理关键抽象

Spring的事务管理SPI(Service Provider Interface)的抽象层主要包括3个接口，分别是`PlatformTransactionManager,TransactionDefinition,TransactionStatus`它们位于`org.springframework.transaction`包中属于Spring的`org.springframework:spring-tx:${version}`模块。3个接口的关系如下图：

![1555942511905](D:\婕\JavaEE学习之路\Spring\picture\事务管理模块图.png)

> 备注：结合源码了解事务管理的关键抽象接口

- **TransactionDefinition**:定义Spring兼容的**事务属性**，这些属性对事务管理控制的若干个方面进行配置

  - **事务隔离级别**：当前事务和其他事务的隔离程度
  - **事务传播**：通常在一个事务中执行的所有代码都会位于同一个事务上下文中，Spring提供了几个可选的事务传播类型，在Spring事务管理中，事务传播行为是一个重要概念（**重要**）
  - **事务超时**：事务在超时前能够运行多久，超时时间后，事务回滚。有些事务管理器不支持事务过期的功能。
  - 只读状态：只读事务不修任何数据，事务管理器可以针对**可读事务应用一些优化措施，提高运行性能。**

  Spring允许**通过XML或者注解**元数据的方式为一个**有事务要求的服务类方法**配置事务属性，这些信息作为Spring事务框架的输入，Spring会自动按事务属性信息的指示，为目标方法提供相应的事务支持。

- **TransactionStatus**:代表一个**事务的具体运行状态**。事务管理器可以通过该接口获**取事务运行期间的状态信息**，也可以通过该接口**间接的回滚事务**，它相比于抛出异常时回滚事务的方式更具有**可控性。**

- PlatformTransactionManager：通过JDBC的事务管理知识可以知道，**事务只能被提交或者回滚**（回滚到某个保存点后提交），Spring高层事务抽象接口 。`org.springframework.transaction.PlatformTransactionManager `很好地描述了事务管理这个概念。 

#### 4.2.3事务管理器实现类

Spring将事务管理委托给底层具体的持久化实现框架来完成。因此，Spring为**不同的持久化框架**提供了 `PlatformTransactionManager `接口的**实现类**。

![持久化框架](D:\婕\JavaEE学习之路\Spring\picture\持久化框架.png)

这些事务管理器都是对特定事务实现框架的代理，这样就可以通过Spring所提交的高级抽象**对不同种类的事务实现使用相同的方式进行管理，而不用关系具体的实现**。



**以Spring JDBC和Mybatis基于DataSource数据源进行持久化的技术 为例子配置事务管理器**

```xml
<!-- 第一步：配置数据源 --> 
<bean id="druidDataSource" class="com.alibaba.druid.pool.DruidDataSource" initmethod="init" destroy-method="close">    
	<property name="url" value="jdbc:mysql://127.0.0.1:3306/china"/>    
	<property name="username" value="root"/>    
	<property name="password" value="root"/> 
</bean>
 
<!-- 第二步：配置基于数据源的事务管理器 --> 
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">    
	<!-- 第三步：配置引用数据源 -->    
	<property name="dataSource" ref="dataSource"/> 
</bean>    

    <!--配置事务管理器
        1.配置数据源
        2.配置基于数据源的事务管理器
        3.配置引用数据源
    -->
```

配置好后，之后调用DataSource的Connection的commit().rollback()等方法管理事务，其实就是DataSourceTransactionManager在幕后做管理。

#### 4.2.4事务传播行为

当我们调用一个基于Spring的Service接口方法（如：UserServer#addUser()）时，它将运行于Spring管理的事务环境中，Service接口方法可能会在内部调用其它的Service接口方法以共同完成一个完整的业务操作，因此就会**产生服务接口方法嵌套调用的情况**。**Spring通过事务传播行为控制当前的事务如何传播到被嵌套调用的目标服务接口方法中。**

下面是Spring在`TransactionDeﬁnition`接口中规定的7中事务传播行为类型：

![Spring事务传播行为类型](D:\婕\JavaEE学习之路\Spring\picture\Spring事务传播行为类型.png)

### 4.3编程式事务（了解）

在实际应用中，很少需要通过编程来进行事务管理。即便如此，Spring还是为编程式事务管理提供了模板类`org.springframework.transaction.support.TransactionTemplate` ，以满足一些特殊场合的需要。TransactionTemplate和持久化模板了一样是**线程安全**的，因此可以在**多个业务类中共享**TransactionTemplate实例进行事务管理。

```xml
<!--事务模板类-->
    <bean id="transactionTemplate" class="org.springframework.transaction.support.TransactionTemplate">
        <property name="transactionManager" ref="transactionManager"/>
        <!--设置隔离级别-->
        <property name="isolationLevelName" value="ISOLATION_REPEATABLE_READ"/>
    </bean>
```

```java
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
```

**使用该方法的弊端：**

由于Spring事务管理基于``TransactionSynchronizationManager `进行工作，所以如果在回调接口方法中需要显式访问底层数据连接，则必须通过资源获取工具类（比如:`org.springframework.jdbc.datasource.DataSourceUtils `）得到**线程绑定的数据库连接**。这是Spring事务管理的底层协议不容违反。如果 memoGroupDao 是基于Spring提供的模板类构建，由于模板类已经在内部使用了 资源获取工具类获取数据库连接，所以就不必关系底层数据库连接的获取问题。 



### 4.4声明式事务

大多数Spring用户会选择声明式事务管理的功能，这种方式对代码的侵入性小，可以让事务管理代码完全从业务代码中移除，非常符合非侵入式轻量级容器的理念。

Spring的声明式事务管理是通过**SpringAOP实现的**，通过事务的声明信息，**Spring负责将事务管理增强逻辑动态织入到业务方法的相应连接点中**。这些逻辑包括获取线程绑定资源，开始提交事务，提交/回滚事务，进行异常转换和处理工作。

声明式事务通过Spring AOP实现，我们通过**切面**的方式配置需要添加相应的依赖

```
<!--Spring AOP-->
  <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-aspects</artifactId>
</dependency>
```

4.4.1XM配置声明式事务

Spring引用Aspect语言，通过aop/tx命名空间进行事务配置

```xml
<?xml version="1.0" encoding="UTF-8"?> 
<beans xmlns="http://www.springframework.org/schema/beans"       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"       xmlns:contex="http://www.springframework.org/schema/context"       xmlns:aop="http://www.springframework.org/schema/aop"       xmlns:tx="http://www.springframework.org/schema/tx"       xsi:schemaLocation="       http://www.springframework.org/schema/beans       http://www.springframework.org/schema/beans/spring-beans.xsd       http://www.springframework.org/schema/context       http://www.springframework.org/schema/context/spring-context.xsd       http://www.springframework.org/schema/aop       http://www.springframework.org/schema/aop/spring-aop.xsd       http://www.springframework.org/schema/tx       http://www.springframework.org/schema/tx/spring-tx.xsd">        
    <!-- 启用注解扫描，自动注入 -->    
    <contex:component-scan base-package="com.bittech.jdbc"/>
 
    <!-- 0. 配置数据源 -->    
    <bean id="dataSource" class="org.apache.commons.dbcp2.BasicDataSource" destroymethod="close">        
        <property name="driverClassName" value="com.mysql.jdbc.Driver"/>        
        <property name="url" value="jdbc:mysql://localhost:3306/memo"/>        
        <property name="username" value="root"/>        
        <property name="password" value="root"/>        
        <property name="maxTotal" value="20"/>        
        <property name="maxIdle" value="10"/>    
    </bean>
 
    <!-- 1.事务管理器 -->    
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">        
        <property name="dataSource" ref="dataSource"/>    
   </bean>
    <!-- 2. 事务增强 -->    
    <tx:advice id="txAdvice" transaction-manager="transactionManager">        
        <!-- 2.1 事务属性定义 -->        
        <tx:attributes>            
            <tx:method name="get*" read-only="true"/>            
            <tx:method name="query*" read-only="true"/>           
            <tx:method name="update*" rollback-for="Exception"/>            <tx:method name="add*" rollback-for="Exception"/>        
        </tx:attributes>    
    </tx:advice>
 
    <!-- 3. 使用强大的切点表达式轻松定义目标方法 -->    
    <aop:config>        
        <!-- 3.1 通过AOP定义事务的增强切面 -->        
        <aop:pointcut id="serviceMethod" expression="execution(* com.bittech.jdbc.service.*.*.*(..))"/>        
        <!-- 3.2 引用事务增强 -->       
        <aop:advisor advice-ref="txAdvice" pointcut-ref="serviceMethod"/>    </aop:config> 
</beans>
```

上述配置`<aop:config>`中的配置式切面的XML配置，事务配置的重点在于事务属性`<tx:attributes>`中的`<tx:method>`的配置

下面式`<tx:method>`的属性信息描述

![1555950267860](D:\婕\JavaEE学习之路\Spring\picture\tx-method配置.png)

![1555950390404](D:\婕\JavaEE学习之路\Spring\picture\tx-method下.png)

**基于aop/tx配置的声明式事务管理是实际应用中最常见的使用事务管理方式**，它的表达能力最强且最为灵活。

#### 4.4.2注解声明事务

除了基于XML的事务配置，Spring还提供了基于注解的事务配置，即通过`@Transactional`对需要事务增强的Bean**接口**，**实现类或方法进行标注**；在容器中配置**基于注解的事务增强驱动**，即可启用基于注解的声明式事务。一般**开启注解装配**之后多才采用这种方式。

##### 4.4.2.1配置例子

- 配置中启用事务注解自动处理

  ```xml
  <!-- 启用事务注解驱动 --> 
  <tx:annotation-driven transaction-manager="transactionManager"/>
  ```

- 在实现类上标注`@Transactional`注解

  ```java
  @Service
  @Transactional(readOnly = true, timeout = 1)
  //添加事务管理注解 属性
  //@Transactional(readOnly = true,timeout = 1)
  public class BookServiceImpl implements BookService {
      @Override
      public Book queryBookname(int isbn) {
          return null;
      }
  
      @Override
      @Transactional
      //添加事务管理
      public boolean addBook(Book book) {
          return false;
      }
  
      @Override
      public boolean deleteBook(int isbn) {
          return false;
      }
  }
  
  ```

##### 4.4.2.2@Transactional注解属性

基于`@Transactional`注解的配置和基于XML的配置方式一样，它拥有一组普适性很强的默认事务属性，通常直接使用这些默认属性即可。当然也可以通过手工设定属性值覆盖默认值下面是`@Transactional`注解属性说明（更多内容可以查看源代码）

![1555951131727](D:\婕\JavaEE学习之路\Spring\picture\@Transaction注解属性说明.png)

##### 4.4.2.3@Transaction注解位置

@Transaction注解可以被应用于:

- 接口定义
- 接口方法
- **类定义**  建议
- 类的public方法

但是Spring建议在业务**实现类上**使用``@Transactional`注解。**因为注解不能被继承**，所以在业务接口中标注的@Transactional注解不会被业务实现了继承。如果通过以下**配置启用子类代理**：

```xml
<tx:annotation-driven transaction-manager="transactionManager" proxy-target-class="true"/>
```

那么**业务类不会添加事务增强，照样工作在非事务环境下**。因此**标注到实现类上，无论是否启用子类代理都能够正常启用事务机制。**

##### 4.4.2.4在方法处使用注解

**方法处的注解会覆盖类定义处的注解**。如果有写方法需要使用特殊的事务属性，则可以在类注解的基础上提供方法注解。

```java
@Service
@Transactional
//添加事务管理注解 属性
//1.类级注解，适用于类中所有public的方法
//@Transactional(readOnly = true,timeout = 1)
public class BookServiceImpl implements BookService {
    @Override
    public Book queryBookname(int isbn) {
        return null;
    }

    @Override
    //2.提供额外的注解信息，它将覆盖1处的类级别注解 
    @Transactional(transactionManager = "transactionManager", readOnly = true)
    //添加事务管理
    public boolean addBook(Book book) {
        return false;
    }

    @Override
    public boolean deleteBook(int isbn) {
        return false;
    }
}
```

##### 4.4.2.5使用不同的事务管理器

一般情况下，一个应用仅有一个事务管理器，如果希望在不同地方使用不同的事务管理器，则可以通过在@Transactional注解的`transactionManager`或`value`属性上设置事务管理器名称

```java
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
```

相应的在XML配置中应当添加相应的事务管理器。

## 总结

| AOP思想  | 1.异常体系2.数据库访问模板类   |
| -------- | ------------------------------ |
| 事务管理 | 1.事务管理器2.编程和声明式事务 |

