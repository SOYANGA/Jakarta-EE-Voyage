# Mybatis 简介和开始

**重点**

==了解Mybatis框架==  

==Mybatis快速入门== 

# 1.Mybatis是什么？

![Mybatis](\1553772749804.png)

MyBatis(3.X以前的版本叫ibatis)

==**支持自定义SQL,存储过程和高级映射的持久化框架**==

MyBatis**几乎消除了所有的JDBC代码**，也基本**不需要手工去设置参数和获取检索结果**。Mybatis能够使用**简单的XML格式或者注解进来配置**，能够映射基本数据结构，Map接口和POJOs（普通java对象）到数据库中的记录。

**JDBC**—--->  将面向对象的语句转化问面向过程的SQL语句（我们只关注SQL语句的结果）。	

​	<——   将对象转化为SQL语句存储在数据库中持久化处理。

**阻抗不匹配**：持久化存储数据所采用的数据模型（无论是文件系统或者数据库管理系统）在编写程序时跟我们采用的数据模型有差异，就称为阻抗不匹配。

我们通过JDBC转化将**数据库中的数据模型**与**java中的数据模型进行转化**。

持久化框架：事务的ACID特性 D：持久化 A：原子性 C：一致性  I：隔离性

**ORM框架（Object Relational Mapping）**采用**元数据**来描述对象--关系映射细节，元数据一般采用XML格式，并且存放在专门的对象映射文件中。

[Hibernate,iBATIS,MyBatis,EclipseLink,JFinal。]



采用的`Mybatis-3.4.5` 

Mybatis框架时JavaEE是最容易掌握的框架。



### 1.1 Mybatis VS JDBC SQL

| 对比     | Mybatis   | JDBC SQL                                                     |
| -------- | --------- | ------------------------------------------------------------ |
| 连接     | 托管      | 编码 Connection connection = DriverManager.getConnection(url) |
| SQL      | 隔离/集中 | 混合/分散  java中有SQL语句                                   |
| 缓存     | 两级缓存  | 不支持                                                       |
| 结果映射 | 自动映射  | 硬编码                                                       |
| 维护性   | 高        | 低                                                           |

缓存：提高访问效率



JDBC的执行流程

1. 加载数据库驱动建立连接/获得数据源  建立连接
2. 创建操作命名
3. 执行SQL语句–->返回结果集
4. 对返回的结果集进行处理（sql-->Java对象）
5. 资源回收（关闭结果集，关闭操作命令，关闭连接）



- 加在数据库驱动建立连接/获得数据源建立连接

  ```java
  Class.forName("com.mysql.jdbc.Driver");
  ```

- 建立数据库连接

  ```java
  Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/memo?
  user = root & password = root & useUnicode = true & characterEncoding = "UTF-8");
  ```

  > //MySQL数据库连接得url参数格式如下：
  >
  > jdbc:mysql://服务器地址:端口/数据库名称？参数名 = 参数值

- 创建操作命令(statement)

  ```java
  Statement statemnt = connection.createStatement();
  ```

- 执行SQL语句

  ```java
  ResultSet resultSet = statement.executeQuery(
  	  "select id,group_id,title,content,is_protected, background,is_remind,remind_time,created_time,modify_time from memo_info");
  ```

- 处理结果集

  ```java
  while (resultSet.next()) {    
      int id = resultSet.getInt("id");    
      String title = resultSet.getString("title");    
      String content = resultSet.getString("content");    
      Date createTime = resultSet.getDate("created_time");    System.out.println(String.format("Memo: id=%d, title=%s, content=%s, createTime=%s", id, title, content, createTime.toString())); 
  }
  ```

- 释放资源（关闭结果集，命令，连接）

  ```java
  //关闭结果集
  if (resultSet != null) {    
  	try {        
  		resultSet.close();    
  	} catch (SQLException e) {        
  		e.printStackTrace();    
  	} 
  }  
  //关闭命令
  if (statement != null) {    
  	try {        
  		statement.close();    
  	} catch (SQLException e) {        
  		e.printStackTrace();    
  	} 
  }  
  //关闭连接命令
  if (connection != null) {    
  	try {        
  		connection.close();    
  	} catch (SQLException e) {        
  		e.printStackTrace();    
  	} 
  }
  ```

## 2.开始使用Mybatis

### 2.1准备工作

- IDEA安装Mybatis插件（Free Mybatis Plugin）
  - IDEA Settings->Plugins->Browse repositorites->Free Mybatis Plugin
  - 安装完成之后重启IDEA
- 准备mybatis项目
  - 创建一个Maven的Application项目
  - pom.xml中添加Mybatis依赖 org.mybatis:mybatis:3.4.5
  - pom.xml中添加JDBC驱动依赖,如:mysql:mysql-connector-java:5.1.43
- 准备数据库
  - 创建数据库
  - 创建数据库表

### 2.2构建SqlSessionFactory

#### 2.1.1XML构建(**重点**)

每个基于Mybatis的应用都是以一个SqlSessionFactory的实例为中心的。SqlSessionFactory的实例可以通过SqlSessionFactoryBuilder获得。而SqlSessionFactoryBuilder则可以从XML配置文件或者一个预先定制的Configuration的实例构建出SqlSessionFactory的实例。

- 创建Mybatis配置文件(`mybatis-config.xml`)归档到`src/main/resources/mybatis-comfig.xml`)
- 利用Resouce类来获取xml文件名解析创建成一个SqlSessionFactory实例

#### 2.1.2编码构建（了解）

如果你愿意直接从java程序而不是XML文件中创建configuration,或者创建你自己的configuraton构建器，MyBatis也提供了完整的配置类，提供所有和XML文件相同功能的配置项

```java
DataSource dataSource = new PooledDataSource();//mybatis提供的数据库连接池
TransactionFactory transactionFactory = new JdbcTransactionFactory();
Environment environment = new Environment("development", transactionFactory, dataSource);
Configuration configuration = new Configuration(environment);
configuration.addMapper(MemoGroupMapper.clss);
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
```

configuration添加了一个映射器类（mapper class)。映射器类是java类，他们包含SQL映射语句的注解，从而避免了XML文件的依赖，不过由于java注解的一些限制加之某些MyBatis映射的复杂性，XML映射对于大多数高级映射（比如：嵌套Join映射）来说仍然是必须的。有鉴于此，如果存在一个对等的XML配置文件的话。MyBatis会自动查找并加载它（这种情况下，MemoGroupMapper.xml将会基于类路径和MemGruopMapper.class的类名被加载进来）。

## 2.2使用SqlSession

我们可以通过SqlSession的实例。SqlSession完全包含了面向数据库执行SQL命令所需的所有方法，你可以通过SqlSession实例来执行已经映射的SQL语句。例如

```java
sqlSession.getMapper(.class);
```



基于Mybatis项目

1. 创建Maven项目
   1. 添加依赖 mybatis org.mybatis:3.4.5
   2. 添加依赖 mysql驱动 mysql:mysql-connector-java:5.1.43驱动
   3. 准备数据库
2. 创建mybatis配置
   1. sec/main/resources  -> mybatis-config.xml
   2. 配置文件中需要配置数据库信息 driver url username password
3. 编码
   1. entity 包 实体类 -> 数据库表
   2. mapper 包 mapper接口 -> 数据库操作的方法
   3. src/main/resources/mapper 创建mapper.xml映射文件 定义信息
   4. 编码 创建SqlSessionFactory SqlSession Mapper Interface
   5. 测试一下代码