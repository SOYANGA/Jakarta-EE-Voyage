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