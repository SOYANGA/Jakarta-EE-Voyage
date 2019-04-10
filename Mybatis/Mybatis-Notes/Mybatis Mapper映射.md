# Mybatis的Mapper映射

> 重点
>
> 1. 掌握Mybatis的基本SQL映射
> 2. 掌握Mybatis中CRUD的SQL编写

## 1 Mapper映射概念

Mapper XML文件MyBatis的真正强大在于它的映射语句，也是解决**JDBC**大量重复代码



## 2 Mapper文件结构

SQL映射文件有很少得到几个顶级元素（按照他们应该被定义的顺序）：

- cache-给定命名空间的缓存配置

- cache-ref-其他命名空间缓存配置的引用

- resultMap-最复杂也是最强的元素，用来描述如何从数据库结果集中映射

  - java对象中的属性名   数据库列名称 跟 jdbcType属性类型 javaType类型 一一对应起来
  - id对应数据库的主键 

- sql-可以被其他语句引用的可重用语句块

  - 简化查询 可重用的语句块 

- insert-映射插入语句

- update-映射更新语句

- delete-映射删除语句

- select-映射查询语句

  增删改查中的id对象Mapper接口中的方法名

参考一个完整的Mapper示例：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.soyanga.mybatis.mapper.ScottGroupMapper">
    <resultMap id="ScottGroup" type="ScottGroup">
        <id property="deptno" column="deptno" jdbcType="INTEGER"/>
        <result property="dname" column="dname" jdbcType="VARCHAR"/>
        <result property="loc" column="loc" jdbcType="VARCHAR"/>
        <!--<select deptno="selectScottGroup" resultMap="scottGroup">-->
        <!--select deptno,dname,loc from scott-->
        <!--where deptno = #{deptno}-->
        <!--</select>-->
    </resultMap>
    
    <!--SQL片段-->
    <sql id="basic_column">
        id,dname,loc
    </sql>

    <!--插入信息-->
    <insert id="insertScottGroup" useGeneratedKeys="true" keyProperty="deptno" keyColumn="deptno">
        insert into dept (deptno,loc)
                values (#{deptno},#{loc})
    </insert>
        <!--修改更新信息-->
    <update id="updateScottInfo">

    </update>

    <delete id="deleteScottInfoById">
        delete from dept
        where  id = #{id}
    </delete>
    
    <select id="query" resultMap="ScottGroup">
        select 
        <include refid="basic_column"/> 
        from dept where id=#{id}
    </select>
</mapper>
```

> 插入操作中的Tips:
>
> 插入信息中 useGeneratedKeys = “true”主键自增长 
>
> keyProperty="deptno" ：对应映射对象的属性名
>
> keyColumn="deptno"	 对应表中主键名称
>
> 
>
> 查询操作中的Tips：
>
> id = mapper接口中得对应的查询方法，resultMap 对应的查询后的结果的类型 一般返回的都是java对应得数据库的对象的List集合
>
> 

## 3 select命令

查询语句是MyBatis最常用的元素章之一。通常查询比修改频繁。查询方式多种

简单查询如下：

```xml
<select id="selectPerson" paramterType="int" resultType="hashmap">
	select* form person where id=#{id}
</select>
```

这个语句被称作selectPerson,接收一个int(或者Integer)类型的参数，并通过HashMap类型的对象，其中键是列名，值便是结果型中的对应值。参数符#{id}MyBatis创建一个预处理的语句参数，通过JDBC这样的一个参数在SQL中会由，并被传递到一个新的预处理语句中，这跟JBC中的SQL语句中的？类作业类似。

## 4.Insert Update Delete

Insert Update Delete都属于数据变更语句，他们的基本实现类似

```xml
<insert
	id="insertAuthor"
	parametrType="domain.bolg.Author"
	flushCache="true"
	statementType="PREPARED"
	keyProperty=""
	keyColumn=""
	useGeneratedKeys=""
	timeout="20">
</insert>
    
<update
      id="updateAuthor"
    parametrType="domain.bolg.Author"
	flushCache="true"
	statementType="PREPARED"
	timeout="20">
</update>

<select
        
      
	
```

MyBatis插入时自动生成主键有两种方式

- 数据库自动生成主键
- 通过SQL语句生成主键，然后使用到主键字段

**在实际开发的项目中，主键字段通常会有一定的规则，所以该两种方式实际上，仅仅体现MyBatis的扩展能力。**

## 5 sql（复用SQL语句）

sql标签可以被用来定义可重用SQL代码片段，可以包含在其他语句中。

```xml
<sql id="userColumns">${alias}.id,${alias}.username,${alias}.pssword>
</sql>

<sql id="basic_column">   				  		   id,group_id,title,content,is_prptected,background,remind_time,create_time,modify_time
</sql>
```

SQL片段可以被包含在SQL语句中，例如：

```xml
<select id="selectUsers" resultType="map">
    select
    <include refid="userColumns">
        <property name="alias" value="t1"/>
    </include>
    <include refid="userColumns">
        <property name="alias" value="t2"/>
    </include>
    from som_table t1 cross join some_table t2
</select>

<select id="query" resultMap="ScottGroup">
        select 
        <include refid="basic_column"/> 
        from dept where id=#{id}
</select>

```



## 6 parameter(参数)

Mybatis的参数类型通常有两种类型：一种时基本类型，一种是引用类型。

### 6.1基本类型

```xml
<select id="selectUsers">
    select id,username,password
    form users
    where id = #{id}   参数类型为基本类型
</select>
```

### 6.2引用类型

```xml
<select id="insertUser" parameterType="User">
	insert into users (id,username,password) values(#{id},#{username},#{password})  
    写的是这个对象的属性名（id,username,password）
</select>
```

上面User类型的参数对象传递到语句中，id,username 和password属性中然后将他们的值传入预处理语句的参数中，这点相对于语句中传参是比较好的

且User的这些属性必须要有geter setter方法。

### 6.3字符串

默认情况下，使用#{}格式的语法会导致Mybatis创建PerparedStatement参数设置参数（就像使用？）。这样做更安全，更迅速，通常也是首选做法，不会

出一些错误，想在直接在SQL语句中插入一个不转义的字符串。比如，想ORDER BY,你可以直接用

```sql
order by ${columnName}
```

这里MyBatis会修改或转义字符串。需要额外注意的是:用这种方式接受用的将其用于语句中的参数是不安全的，会导致潜在SQL注入攻击，因此不要使用这些字段，要么自行转译并检验。

## 7.resultMap(查询结果映射)

resultMap元素是MyBatis种最强大的元素，可以节省%90的JDBC代码量，并在一情形下允许你做一些JDBC不支持的事情。对复杂语句进行联合映射的时候，它很可能代替数千行的同等功能的代码，简化代码量的设计思想是，简单的原句不需要明确的结果映射，而复杂的语句只需要建立联系即可。

简单的映射，未指定明确的resultmap比如：

```xml
<select id="selectUsers" resultType="map">
	select id,username,hashedPassword
    form some_table
    where id = #{id}
</select>
```

上述语句只是简单的将所有的列映射到HashMap的建上，这由resultType的参数指定，然而在大部分情况下都够用，但是HashMap不是一个很好的领域模型。你的使用JavaBean或POJO(Plain Old Java Objects,普通Java对象)作为领域模型对两者都支持。

Bean映射，指定resultmap比如：

```java
//指定user Bean
package com.soyanga.mode1;
public class User{
    private int id;
    private String username;
    private String hashedPassword;
    //省略getter setter方法
}
```

基于JavaBean的规范，上面这个类有3个属性：id,username和hashedPassword属性会对应到select语句中列名。这个的一个JavaBean可以被映射到Result映射到HashMap一样简单。

```xml
<select id="selectUsers" resultType="com.soyanga.mode1.User">
	select id,username,hashedPassword
    form som_table
    where id = #{id}
</select>
```

这样情况下，MyaBatis会在幕后自动创建一个ResultMap,再基于属性名称映射在javaBean的属性上。如果，列名和属性名没有精确匹配**，可以再select语句中的sql的as来起一个别名（sql中起别名–-SQL特性）来匹配标签**。比如：

```xml
<select id="selectUsers" resultType="com.soyanga.mode1.User">
	select 
    user_id  as"id"
    user_name as "userName",
    hashed_password as"hashedPassword"
    form some_table
    where id = #{id}
</select>
```

ResultMap方式 极大的简化繁琐的配置，resultMap和resultType只能二选一

```xml
<resultMap id="UserResultMap" type="com.soynga.model.User">
        <id property="id" column="user_id" jdbcType="INTEGER"/>
        <result property="userName" column="user_name" jdbcType="VARCHAR"/>
        <result property="hashedPassword" column="hashed_password" jdbcType="VARCHAR"/>
</resultMap>
```

## 8.cache(缓存)

### 8.1一级缓存

MyBatis默认开启了一级缓存，一级缓存是SqlSession层面进行缓存的。即同一个SqlSession，多次调用同一个Mapper和同一个方法的同一参数，只会进行一次数据库查询，然后把数据缓存到缓冲中，以后直接先从缓存中取出数据，不会直接去查数据库

默认**开启一级缓存**是在Mybatis的配置文件的settings元素中定义

```xml
<settings>
<!--mapper映射的二级缓存默认开启，需要mapper文件设置，以及实体类的支持序列化-->
	<setting name = "cachaeEnabled" value="true"/>
<!--session级别的一级缓存默认开启-->
	<setting name="localCacheScope" value="SESSION"/>
</settings>
```

### 8.2二级缓存

默认情况下Mapper中是没有开启缓存的，除了局部的session缓存，可以增强变现而且处理循环依赖也是必须的，要开启二级缓存，需要在你SQL映射文件中添加一行： 实体类需要实现**Serializable接口支持序列化**

```xml
<cache/>
```

从cache字面上看就是这样。这个简单语句的效果如下：

- 映射语句文件中的所有**select语句**将会被缓存

- 映射语句文件中的所有**insert,update和delete语句**会刷新缓存

- 缓存会使用**Least Recently Used**（**LRU,最近最少使用的**）算法来收回

- 根据时间表(比如 **NO Flush interval,没有刷新间隔**)，缓存不会以任何时间顺序来刷新

  ```xml
  <cache flushInterval="60000">  //单位是ms
  ```

- 缓存会存储**列表集合或对象**(无论查询方式返回什么)的**1024个引用**

- 缓存会被视为是**read/write(可读/可写)的缓存**，意味着对象检索不是共享的，而且可以安全地被调用者修改，而不是干扰其他调用者或线程所做的潜在修改。（**两个策略**）

**缓存命中率：**Cache Hit Ratio(缓存命中)  关心缓存命中率

### 8.3缓存属性

```xml
<cache eviction="FIFO" flushInterval="60000" size="512" redOnly="true"/>
```

配置了一个FIFO回收策略的缓存。每隔60秒刷新，存储结果对象或列表的512隔引用，而且返回的对象被认为是只读的，因此在不同线程中的调用者之间修改他们会导致冲突。

eviction（回收策略）：

- LRU  -（默认值）最近最少使用的：移除最长时间不被使用的对象。
- FIFO - （先进先出） ：按对象进入缓存的顺序来移除它们。
- SOFT -（软引用）：移除基于垃圾回收器状态和软引用规则的对象。
- WEAK - （弱引用）：更积极地移除基于垃圾回收器状态和弱引用规则地对象。

flushInerval(刷新间隔)：

- 可以被设置为任意正整数，而且他们代表一个合理的毫秒形式的时间段
- 默认情况是不设置，也就是没有刷新间隔的，缓存仅仅调用语句时刷新

size（引用数目）

- 可以被设置为任意正整数，要记住你缓存的对象数目和运行环境的可用内存资源数目。
- 默认值是1024。

readOnly（只读）

- 可以设置为 true 或 false
- 只读缓存会给调用者返回缓存对象的相同实例，**因此这些对象不能被修改，者提供了很重要的性能优势。**
- 可读写的**缓存会返回缓存对象的拷贝**(**通过序列化)**，这样会慢一点，的但是安全，因此默认是false.

### 8.4自定义缓存

除了使用自定义缓存的方式，还可以通过实现自己的缓存或者为其他第三方方案创建适配器来完全覆盖缓存行为。

```xml
<cache type="com.soyanga.mybaits.plugin.MyCache"
```

这个实例展示了如何使用一个自定义的缓存实现。type属性指定的类必须实现`org.mybatis.cache.Cache`接口。这个接口是MyBatis框架中很多复杂接口之一，但是简单实现即可

```java
public interface Cache{
    String getId();
    int getSize();
    void putObject(Object key,Object value);
    Object getObject(Object key);
    boolean haskey(Object key);
    Object removeObject(Object key);
    void clear();
}
```

自定义实现一个简单的Cache

```java
package com.soyanga.mybatis.plugin;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * @program: mybatis-case
 * @Description: MyCache 自定义缓存利用MyBatis的接口
 * @Author: SOYANGA
 * @Create: 2019-04-05 11:22
 * @Version 1.0
 */
/*
 MyBatis会为，每一个nameSpace配置一个Cache实例 将其中的id属性(nameSpace字符串)作为为cache的一个唯一标识
 */
public class MyCache implements Cache {

    /**
     * 日志信息
     */
    private final Logger logger = LoggerFactory.getLogger(MyCache.class);

    /**
     * nameSpace中的id 作为cache的标识符
     */
    private final String id;

    /**
     * 开辟容量
     */
    private final Integer capacity = 1024;

    /**
     * 设置最大缓存数目用户缓存回收策略--（即满即清空）
     */
    private Integer maxSize = 1024;

    /**
     * 将key和value一一对应起来 内部存储数据结构（线程安全的Map）
     */
    private final ConcurrentHashMap<Object, Object> cacaheData = new ConcurrentHashMap<Object, Object>(capacity);  //默认开辟1024个


    public MyCache(String id) {
        this.id = id;
    }


    /**
     * 获取Cache的标识id
     *
     * @return The identifier of this cache
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * 将key和value一一对应起来  (HashMap/数据库)
     *
     * @param key   Can be any object but usually it is a {@link CacheKey}
     * @param value The result of a select.
     */
    @Override
    public void putObject(Object key, Object value) {
        //即满即清空 回收策略
        if (this.cacaheData.size() >= this.getMaxSize()) {
            this.clear();
        }
        logger.debug("putObject key={} value={}", key, value);
        this.cacaheData.put(key, value);
    }

    /**
     * 获取缓存中的对象
     *
     * @param key The key
     * @return The object stored in the cache.
     */
    @Override
    public Object getObject(Object key) {
        Object value = cacaheData.get(key);
        logger.debug("getObject key={} value={}", key, value);
        return value;
    }


//    As of 3.3.0 this method is only called during a rollback for any previous value that was missing in the cache.
//    This lets any blocking cache to release the lock that may have previously put on the key.
//    A blocking cache puts a lock when a value is null and releases it when the value is back again.
//    This way other threads will wait for the value to be available instead of hitting the database.
//

    /**
     * 删除缓存中的某个对象
     *
     * @param key The key
     * @return Not used
     */
    @Override
    public Object removeObject(Object key) {
        Object value = cacaheData.remove(key);
        logger.debug("removeObject key={} value={}", key, value);
        return value;
    }

    /**
     * Clears this cache instance
     */
    @Override
    public void clear() {
        logger.debug("clear cache");
        cacaheData.clear();
    }

    /**
     * 并不是核心方法，可以不要去实现
     *
     * @return
     */
    @Override
    public int getSize() {
        return 0;
    }

    /**
     * 并不是核心类，可以不去实现
     *
     * @return
     */
    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }
}

```

缓存配置和缓存实例绑定在SQL映射文件（mapper文件）的命名空间（nameSpace）很重要的。因此，所有在相同命名空间的语句,正如绑定的缓存一样。**语句可以修改和缓存交互方式**，**或在语句的基础上使用两种简单的属性来完全排除它们**。默认情况下，语句可以这样来配置。

```xml
<select ... flushCache="false" usedCache="true"/>
<insert ... flushCache="true"/>
<update ... flushCache="true"/>
<delete ... flushCache="true"/>
```

如果想改变默认的缓存行为，可以通过flushCache和useCache属性。比如在一情况下需要通过一个查询语句来刷新新缓存。

### 8.5 第三方缓存

Mybatis的Cache实现比较简单，真正使用二级缓存的时候，都会选择专业的缓存框架。EHcache是在Java领域广泛使用的分布式缓存解决方案框架。通过Mybatis集成EHcache来实现二级缓存。

memcache k - v 内存缓存

**redis**         k - v  list map set skipList  缓存系统

- 添加Mybatis集成Ehcache依赖

  ```xml
  <dependency>
        <groupId>org.mybatis.caches</groupId>
        <artifactId>mybstis-ehcache</artifactId>
        <version>1.1.0</version>
  </dependency>
  ```

- 添加缓存配置

  ```xml
  <cache type="org.mybatis.caches.ehcache.EhcacheCache">
  	<property name="timeToIdleSeconds" value="3600"/><!--hour-->
      <property name="timeToLiveSeconds" value="3600"/><!--hour-->
      <property name="maxEntriesLocalHeap" value="1000"/>
      <property name="maxEntriesLocalDisk" value="10000000"/>
      <property name="memoryStoreEvictionPolicy" value="LRU" />
  </cache>
  ```

- 添加Ehcache的配置文件（`classPath:src/main/resources/ehcache.xml`）

  ```xml
  <ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../config/ehcache.xsd">
  <!--
  	java.io.tmpdir  ：默认的temp文件目录
  	maxElementsInMemory :内存中最大缓存对象数
  	maxElementsOnDisk :磁盘中最大缓存对象数，若是0表示无穷大
  	eternal :Element是否永久有效，一但设置了，timeout将不起作用
  	overflowToDisk ;配置此属性，当内存中Element数量达到maxElementsInMemory时，Ehcahche将会Element写到磁盘中
  	timeToIdleSeconds :设置Element在失效前的允许闲置时间。仅当element不是永久有效时使用，可选属性，默认属性值是0，也就是可闲置时间无穷大
  	timeToLiveSeconds ：设置Element在失效前允许存活时间。最大时间介于创建时间和失效时间之间。仅当element不是永久有效时使用，默认是0，也就是element存活时间无穷大
  	diskExpiryThreadTntervalSeconds :磁盘失效线程运行时间间隔，默认是120秒-->
      <!--diskStore path="java.io.tmpdir/mybatis-cache"/存储临时目录-->
  	<diskStore path="java.io.tmpdir/mybatis-cache"/>
      <defaultCache
                    name="default"
                    maxElementsInMemory="1000"
                    maxElementsOnDisk="10000000"
                    eternal="false"
                    overflowToDisk="false"
                    timeToIdleSeconds="120"
                    timeToLiveSeconds="120"
                    diskExpiryThreadIntervalSeconds="120"
                    memoryStoreEvictionPolicy="LRU"></defaultCache>
  </ehcache>
  ```

  代码实验：

  ```java
      @Test
      public void test_queryScottEmpAll() {
          //第一次打开SqlSession
          SqlSession sqlSession = sqlSessionFactory.openSession(true);
          ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
          List<ScottEmp> scottEmpList = scottEmpMapper.queryScottEmpAll();
          logger.info("test_queryScottEmpAll  FirstResult: {}", scottEmpList);
          sqlSession.close();
  
          //第二次打开SqlSession
          sqlSession = sqlSessionFactory.openSession(true);
          scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
          List<ScottEmp> scottEmpList2 = scottEmpMapper.queryScottEmpAll();
          logger.info("test_queryScottEmpAll SecondResult: {}", scottEmpList2);
          sqlSession.close();
  
          //第二次打开SqlSession
          sqlSession = sqlSessionFactory.openSession(true);
          scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
          List<ScottEmp> scottEmpList3 = scottEmpMapper.queryScottEmpAll();
          logger.info("test_queryScottEmpAll ThirResult: {}", scottEmpList3);
          sqlSession.close();
      }
  ```



​	返回结果日志：

```java
16:33:00.385 [main] DEBUG org.apache.ibatis.logging.LogFactory - Logging initialized using 'class org.apache.ibatis.logging.slf4j.Slf4jImpl' adapter.
16:33:00.723 [main] DEBUG org.apache.ibatis.logging.LogFactory - Logging initialized using 'class org.apache.ibatis.logging.slf4j.Slf4jImpl' adapter.
16:33:00.778 [main] DEBUG org.apache.ibatis.datasource.pooled.PooledDataSource - PooledDataSource forcefully closed/removed all connections.
16:33:00.778 [main] DEBUG org.apache.ibatis.datasource.pooled.PooledDataSource - PooledDataSource forcefully closed/removed all connections.
16:33:00.778 [main] DEBUG org.apache.ibatis.datasource.pooled.PooledDataSource - PooledDataSource forcefully closed/removed all connections.
16:33:00.778 [main] DEBUG org.apache.ibatis.datasource.pooled.PooledDataSource - PooledDataSource forcefully closed/removed all connections.
16:33:01.058 [main] DEBUG net.sf.ehcache.config.ConfigurationFactory - Configuring ehcache from ehcache.xml found in the classpath: file:/D:/IDEA%20project/mybatis-case/target/classes/ehcache.xml
16:33:01.058 [main] DEBUG net.sf.ehcache.config.ConfigurationFactory - Configuring ehcache from URL: file:/D:/IDEA%20project/mybatis-case/target/classes/ehcache.xml
16:33:01.061 [main] DEBUG net.sf.ehcache.config.ConfigurationFactory - Configuring ehcache from InputStream
16:33:01.090 [main] DEBUG net.sf.ehcache.config.BeanHandler - Ignoring ehcache attribute xmlns:xsi
16:33:01.090 [main] DEBUG net.sf.ehcache.config.BeanHandler - Ignoring ehcache attribute xsi:noNamespaceSchemaLocation
16:33:01.092 [main] DEBUG net.sf.ehcache.config.DiskStoreConfiguration - Disk Store Path: C:\Users\32183\AppData\Local\Temp\/mybatis-cache
16:33:01.114 [main] DEBUG net.sf.ehcache.CacheManager - Creating new CacheManager with default config
16:33:01.119 [main] DEBUG net.sf.ehcache.util.PropertyUtil - propertiesString is null.
16:33:01.132 [main] DEBUG net.sf.ehcache.config.ConfigurationHelper - No CacheManagerEventListenerFactory class specified. Skipping...
16:33:01.900 [main] DEBUG net.sf.ehcache.Cache - No BootstrapCacheLoaderFactory class specified. Skipping...
16:33:01.901 [main] DEBUG net.sf.ehcache.Cache - CacheWriter factory not configured. Skipping...
16:33:01.903 [main] DEBUG net.sf.ehcache.config.ConfigurationHelper - No CacheExceptionHandlerFactory class specified. Skipping...
16:33:01.938 [main] DEBUG net.sf.ehcache.store.MemoryStore - Initialized net.sf.ehcache.store.MemoryStore for com.soyanga.mybatis.mapper.ScottEmpMapper
16:33:01.951 [main] DEBUG net.sf.ehcache.DiskStorePathManager - Using diskstore path C:\Users\32183\AppData\Local\Temp\mybatis-cache
16:33:01.951 [main] DEBUG net.sf.ehcache.DiskStorePathManager - Holding exclusive lock on C:\Users\32183\AppData\Local\Temp\mybatis-cache\.ehcache-diskstore.lock
16:33:01.955 [main] DEBUG net.sf.ehcache.store.disk.DiskStorageFactory - Failed to delete file com%002esoyanga%002emybatis%002emapper%002e%0053cott%0045mp%004dapper.index
16:33:01.967 [main] DEBUG net.sf.ehcache.store.disk.DiskStorageFactory - Matching data file missing (or empty) for index file. Deleting index file C:\Users\32183\AppData\Local\Temp\mybatis-cache\com%002esoyanga%002emybatis%002emapper%002e%0053cott%0045mp%004dapper.index
16:33:01.968 [main] DEBUG net.sf.ehcache.store.disk.DiskStorageFactory - Failed to delete file com%002esoyanga%002emybatis%002emapper%002e%0053cott%0045mp%004dapper.index
16:33:01.977 [main] DEBUG net.sf.ehcache.Cache - Initialised cache: com.soyanga.mybatis.mapper.ScottEmpMapper
16:33:01.977 [main] DEBUG net.sf.ehcache.config.ConfigurationHelper - CacheDecoratorFactory not configured for defaultCache. Skipping for 'com.soyanga.mybatis.mapper.ScottEmpMapper'.
16:33:02.017 [main] DEBUG com.soyanga.mybatis.mapper.ScottEmpMapper - Cache Hit Ratio [com.soyanga.mybatis.mapper.ScottEmpMapper]: 0.0
16:33:02.023 [main] DEBUG org.apache.ibatis.transaction.jdbc.JdbcTransaction - Opening JDBC Connection
Fri Apr 05 16:33:02 CST 2019 WARN: Establishing SSL connection without server's identity verification is not recommended. According to MySQL 5.5.45+, 5.6.26+ and 5.7.6+ requirements SSL connection must be established by default if explicit option isn't set. For compliance with existing applications not using SSL the verifyServerCertificate property is set to 'false'. You need either to explicitly disable SSL by setting useSSL=false, or set useSSL=true and provide truststore for server certificate verification.
16:33:02.342 [main] DEBUG org.apache.ibatis.datasource.pooled.PooledDataSource - Created connection 98394724.
16:33:02.344 [main] DEBUG com.soyanga.mybatis.mapper.ScottEmpMapper.queryScottEmpAll - ==>  Preparing: select empno as 'empno', ename as 'ename', job as 'job', mgr as 'mgr' , sal as 'sal', comm as 'common', deptno as 'deptno', hiredate as 'hiredate' from emp 
16:33:02.376 [main] DEBUG com.soyanga.mybatis.mapper.ScottEmpMapper.queryScottEmpAll - ==> Parameters: 
16:33:02.413 [main] DEBUG com.soyanga.mybatis.mapper.ScottEmpMapper.queryScottEmpAll - <==      Total: 15
16:33:02.414 [main] INFO com.soyanga.mybatis.mapper.ScottEmpMapperTest - test_queryScottEmpAll  FirstResult: [ScottEmp{empno='7369', ename='SMITH', job='CLERK', mgr=7902, hiredate=Wed Dec 17 00:00:00 CST 1980, sal=800.00, comm=null, deptno=20}, ScottEmp{empno='7499', ename='ALLEN', job='SALESMAN', mgr=7698, hiredate=Fri Feb 20 00:00:00 CST 1981, sal=1600.00, comm=null, deptno=30}, ScottEmp{empno='7521', ename='WARD', job='SALESMAN', mgr=7698, hiredate=Sun Feb 22 00:00:00 CST 1981, sal=1250.00, comm=null, deptno=30}, ScottEmp{empno='7566', ename='JONES', job='MANAGER', mgr=7839, hiredate=Thu Apr 02 00:00:00 CST 1981, sal=2975.00, comm=null, deptno=20}, ScottEmp{empno='7654', ename='MARTIN', job='SALESMAN', mgr=7698, hiredate=Mon Sep 28 00:00:00 CST 1981, sal=1250.00, comm=null, deptno=30}, ScottEmp{empno='7698', ename='BLAKE', job='MANAGER', mgr=7839, hiredate=Fri May 01 00:00:00 CST 1981, sal=2850.00, comm=null, deptno=30}, ScottEmp{empno='7782', ename='CLARK', job='MANAGER', mgr=7839, hiredate=Tue Jun 09 00:00:00 CST 1981, sal=2450.00, comm=null, deptno=10}, ScottEmp{empno='7788', ename='SCOTT', job='ANALYST', mgr=7566, hiredate=Sun Apr 19 00:00:00 CDT 1987, sal=3000.00, comm=null, deptno=20}, ScottEmp{empno='7839', ename='KING', job='PRESIDENT', mgr=null, hiredate=Tue Nov 17 00:00:00 CST 1981, sal=5000.00, comm=null, deptno=10}, ScottEmp{empno='7844', ename='TURNER', job='SALESMAN', mgr=7698, hiredate=Tue Sep 08 00:00:00 CST 1981, sal=1500.00, comm=null, deptno=30}, ScottEmp{empno='7876', ename='ADAMS', job='CLERK', mgr=7788, hiredate=Sat May 23 00:00:00 CDT 1987, sal=1100.00, comm=null, deptno=20}, ScottEmp{empno='7900', ename='JAMES', job='CLERK', mgr=7698, hiredate=Thu Dec 03 00:00:00 CST 1981, sal=950.00, comm=null, deptno=20}, ScottEmp{empno='7902', ename='FORD', job='ANALYST', mgr=7566, hiredate=Thu Dec 03 00:00:00 CST 1981, sal=3000.00, comm=null, deptno=20}, ScottEmp{empno='7934', ename='MILLER', job='CLERK', mgr=7782, hiredate=Sat Jan 23 00:00:00 CST 1982, sal=1300.00, comm=null, deptno=10}, ScottEmp{empno='1314', ename='SOYANGA', job='BOSS', mgr=10000, hiredate=Wed Apr 03 22:12:39 CST 2019, sal=8888.88, comm=null, deptno=null}]
16:33:02.415 [main] DEBUG net.sf.ehcache.store.disk.Segment - put added 0 on heap
16:33:02.418 [main] DEBUG org.apache.ibatis.transaction.jdbc.JdbcTransaction - Closing JDBC Connection [com.mysql.jdbc.JDBC4Connection@5dd6264]
16:33:02.419 [main] DEBUG org.apache.ibatis.datasource.pooled.PooledDataSource - Returned connection 98394724 to pool.
16:33:02.419 [main] DEBUG com.soyanga.mybatis.mapper.ScottEmpMapper - Cache Hit Ratio [com.soyanga.mybatis.mapper.ScottEmpMapper]: 0.5
16:33:02.419 [main] INFO com.soyanga.mybatis.mapper.ScottEmpMapperTest - test_queryScottEmpAll SecondResult: [ScottEmp{empno='7369', ename='SMITH', job='CLERK', mgr=7902, hiredate=Wed Dec 17 00:00:00 CST 1980, sal=800.00, comm=null, deptno=20}, ScottEmp{empno='7499', ename='ALLEN', job='SALESMAN', mgr=7698, hiredate=Fri Feb 20 00:00:00 CST 1981, sal=1600.00, comm=null, deptno=30}, ScottEmp{empno='7521', ename='WARD', job='SALESMAN', mgr=7698, hiredate=Sun Feb 22 00:00:00 CST 1981, sal=1250.00, comm=null, deptno=30}, ScottEmp{empno='7566', ename='JONES', job='MANAGER', mgr=7839, hiredate=Thu Apr 02 00:00:00 CST 1981, sal=2975.00, comm=null, deptno=20}, ScottEmp{empno='7654', ename='MARTIN', job='SALESMAN', mgr=7698, hiredate=Mon Sep 28 00:00:00 CST 1981, sal=1250.00, comm=null, deptno=30}, ScottEmp{empno='7698', ename='BLAKE', job='MANAGER', mgr=7839, hiredate=Fri May 01 00:00:00 CST 1981, sal=2850.00, comm=null, deptno=30}, ScottEmp{empno='7782', ename='CLARK', job='MANAGER', mgr=7839, hiredate=Tue Jun 09 00:00:00 CST 1981, sal=2450.00, comm=null, deptno=10}, ScottEmp{empno='7788', ename='SCOTT', job='ANALYST', mgr=7566, hiredate=Sun Apr 19 00:00:00 CDT 1987, sal=3000.00, comm=null, deptno=20}, ScottEmp{empno='7839', ename='KING', job='PRESIDENT', mgr=null, hiredate=Tue Nov 17 00:00:00 CST 1981, sal=5000.00, comm=null, deptno=10}, ScottEmp{empno='7844', ename='TURNER', job='SALESMAN', mgr=7698, hiredate=Tue Sep 08 00:00:00 CST 1981, sal=1500.00, comm=null, deptno=30}, ScottEmp{empno='7876', ename='ADAMS', job='CLERK', mgr=7788, hiredate=Sat May 23 00:00:00 CDT 1987, sal=1100.00, comm=null, deptno=20}, ScottEmp{empno='7900', ename='JAMES', job='CLERK', mgr=7698, hiredate=Thu Dec 03 00:00:00 CST 1981, sal=950.00, comm=null, deptno=20}, ScottEmp{empno='7902', ename='FORD', job='ANALYST', mgr=7566, hiredate=Thu Dec 03 00:00:00 CST 1981, sal=3000.00, comm=null, deptno=20}, ScottEmp{empno='7934', ename='MILLER', job='CLERK', mgr=7782, hiredate=Sat Jan 23 00:00:00 CST 1982, sal=1300.00, comm=null, deptno=10}, ScottEmp{empno='1314', ename='SOYANGA', job='BOSS', mgr=10000, hiredate=Wed Apr 03 22:12:39 CST 2019, sal=8888.88, comm=null, deptno=null}]
16:33:02.420 [main] DEBUG com.soyanga.mybatis.mapper.ScottEmpMapper - Cache Hit Ratio [com.soyanga.mybatis.mapper.ScottEmpMapper]: 0.6666666666666666
16:33:02.420 [main] INFO com.soyanga.mybatis.mapper.ScottEmpMapperTest - test_queryScottEmpAll ThirResult: [ScottEmp{empno='7369', ename='SMITH', job='CLERK', mgr=7902, hiredate=Wed Dec 17 00:00:00 CST 1980, sal=800.00, comm=null, deptno=20}, ScottEmp{empno='7499', ename='ALLEN', job='SALESMAN', mgr=7698, hiredate=Fri Feb 20 00:00:00 CST 1981, sal=1600.00, comm=null, deptno=30}, ScottEmp{empno='7521', ename='WARD', job='SALESMAN', mgr=7698, hiredate=Sun Feb 22 00:00:00 CST 1981, sal=1250.00, comm=null, deptno=30}, ScottEmp{empno='7566', ename='JONES', job='MANAGER', mgr=7839, hiredate=Thu Apr 02 00:00:00 CST 1981, sal=2975.00, comm=null, deptno=20}, ScottEmp{empno='7654', ename='MARTIN', job='SALESMAN', mgr=7698, hiredate=Mon Sep 28 00:00:00 CST 1981, sal=1250.00, comm=null, deptno=30}, ScottEmp{empno='7698', ename='BLAKE', job='MANAGER', mgr=7839, hiredate=Fri May 01 00:00:00 CST 1981, sal=2850.00, comm=null, deptno=30}, ScottEmp{empno='7782', ename='CLARK', job='MANAGER', mgr=7839, hiredate=Tue Jun 09 00:00:00 CST 1981, sal=2450.00, comm=null, deptno=10}, ScottEmp{empno='7788', ename='SCOTT', job='ANALYST', mgr=7566, hiredate=Sun Apr 19 00:00:00 CDT 1987, sal=3000.00, comm=null, deptno=20}, ScottEmp{empno='7839', ename='KING', job='PRESIDENT', mgr=null, hiredate=Tue Nov 17 00:00:00 CST 1981, sal=5000.00, comm=null, deptno=10}, ScottEmp{empno='7844', ename='TURNER', job='SALESMAN', mgr=7698, hiredate=Tue Sep 08 00:00:00 CST 1981, sal=1500.00, comm=null, deptno=30}, ScottEmp{empno='7876', ename='ADAMS', job='CLERK', mgr=7788, hiredate=Sat May 23 00:00:00 CDT 1987, sal=1100.00, comm=null, deptno=20}, ScottEmp{empno='7900', ename='JAMES', job='CLERK', mgr=7698, hiredate=Thu Dec 03 00:00:00 CST 1981, sal=950.00, comm=null, deptno=20}, ScottEmp{empno='7902', ename='FORD', job='ANALYST', mgr=7566, hiredate=Thu Dec 03 00:00:00 CST 1981, sal=3000.00, comm=null, deptno=20}, ScottEmp{empno='7934', ename='MILLER', job='CLERK', mgr=7782, hiredate=Sat Jan 23 00:00:00 CST 1982, sal=1300.00, comm=null, deptno=10}, ScottEmp{empno='1314', ename='SOYANGA', job='BOSS', mgr=10000, hiredate=Wed Apr 03 22:12:39 CST 2019, sal=8888.88, comm=null, deptno=null}]

Process finished with exit code 0
```



> 结论：缓存命中率提高 0 ->0.5–>0.666666所有配置缓存是生效的，且除了第一次以外的其他两次查询没有生成SQL语句是直接在缓存中进行查询的



小Tips：

> Ehcache本身不支持Mybatis的Cache接口，MyBatis创建了一个新的接口（org\mybatis\caches\ehcache）来将Ehcache和Mybatis的Cache适配起来   –------**适配器设计模式**

