# SpringBoot实战

**重点**

> 1. 了解Spring Boot集成功能
> 2. 了解Spring Boot应用的监控
> 3. 掌握Spring Boot中使用的数据库，RESTl，缓存
> 4. 掌握Spring Boot应用的打包，部署，运维

## 1.常用功能实践

1.1SQL数据库

Spring Boot中使用的数据库相关的功能引入`spring-boot-starter-jdbc`即可。这样Spring Boot在启动的时候会自动配置数据源，前提**是需要在application.properties中配置的数据库的地址，用户名，密码等信息。**

- 在pom.xml中添加spring-boot-statrter-jdbc依赖

  ```xml
  <dependency>    
  	<groupId>org.springframework.boot</groupId>    
  	<artifactId>spring-boot-starter-jdbc</artifactId> 
  </dependency>
  ```

- 使用MySQL数据库则需要额外添加MySQL的**JDBC驱动**

  ```xml
  <dependency>    
  	<groupId>mysql</groupId>    
  	<artifactId>mysql-connector-java</artifactId> 
  </dependency>
  ```

- 在application.properties中添加数据库的配置

  ```properties
  spring.datasource.data-username=root 
  spring.datasource.data-password=root 
  #配置mysql驱动
  spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
  spring.datasource.url=jdbc:mysql://localhost:3306/test
  #配置阿里的数据源信息
  spring.datasource.type = com.alibaba.druid.pool.DruidDataSource
  #如果不配置则默认使用HikariCP这个数据源，还可以进一步对其进行下一步配置 超时时间单位时秒
  spring.datasource.hikari.connection.timeOut = 30s
  ```

在我们未指定数据库连接池的是时候，Spring Boot2.x版本开始默认使用的是[HikariCP光](http://brettwooldridge.github.io/HikariCP/) 日本开发贡献，号称世界上最快的数据库连接池

- 启用了数据库的支持之后，Spring Boot完成自动配置。`JdbcTempleate` Bean就已经在IoC容器哦中存在了，接下来就可以直接通过@Autowired使用**（JdbcTempleate中就需要使用数据源，就已经将数据源自动注入进去了）**

### 1.2NoSQL数据库

Spring框架关于数据库处理方面主要集中在[Spring Date](https://projects.spring.io/spring-data/)这个项目。Spring Data项目分为主要模块和社区模块

主要模块有(主要有Spring IO 组织贡献):

- Spring Data JDBC：基于JDBC的数据库操作
- Spring Data **Redis**: 提供简洁的Redis配置和操作
- Spring Data MongoDB: 基于MongoDB及进行文档对象操作
- 。。。

社区模块(主要有开源社区开发贡献)：

- Spring Data Neo4j :基于Neo4j的图数据库操作
- Spring Data Elasticsearch ：基于Elasticsearch服务的搜索引擎引擎操作
- 。。。

Spring Data项目子模块很多，这里我们挑出**Spring Data Redis**子模块进行讲解。Spring Boot开启[Redis](http://www.redis.cn/)操作，非常简单，基本三步就可以完成配置和操作。

> 备注:准备Redis服务器并且安装，Windows平台下的安装包[windows redis](https://github.com/MicrosoftArchive/redis/releases) 

[安装教程](https://www.runoob.com/redis/redis-install.html) 

如下：

- 在pom.xml中添加spring-boot-starter-data-redis依赖

  ```xml
          <!--Redis Spring-->
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-data-redis</artifactId>
          </dependency>
  ```

- 在application.properties中添加redis的配置（可选，默认信息如下）

  ```properties
  #配置Redis数据库
  spring.redis.database=0
  spring.redis.host=localhost
  spring.redis.port=6379
  ```

当我们添加了spring-boot-starter-data-redis依赖后，如果不配置任何信息，这时候Spring Boot自动配置时就会去使用本地的redis（默认端口号，无认证要求，第0个数据库），如果本地没有安装redis则启动失败。 默认端口号是:6379

- Spring Boot正常启动之后就会在Spring IoC容器中创建一个 `RedisTemplate`的 Bean RedisTemplate即就是我们操作Redis数据库的模板客户端。

如下通过RedisTemplate访问Resis数据库

```java
package com.github.soyanga.springBootBasic.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * @program: springBoot-action
 * @Description: SprinBoot使用 Redis
 * @Author: SOYANGA
 * @Create: 2019-05-04 00:33
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/redis")
public class RedisController {

    /**
     * 自动导入StringRedisTemplate的模板类 --Spirng帮我们已经做好了
     */
    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping(value = "/add")
    public Map<String, String> add(@RequestParam("key") String key, @RequestParam("value") String value) {
        Map<String, String> data = new HashMap<>();
        //添加
        redisTemplate.opsForValue().set(key, value);
        data.put(key, value);
        return data;
    }

    @RequestMapping(value = "/query")
    public Map<String, String> query(@RequestParam("key") String key) {
        Map<String, String> data = new HashMap<>();
        //查询
        String value = redisTemplate.opsForValue().get(key);
        data.put(key, value);
        return data;
    }

    @RequestMapping(value = "delete")
    public Map<String, String> delete(@RequestParam("key") String key) {
        Map<String, String> data = new HashMap<>();
        String value = redisTemplate.opsForValue().get(key);
        //删除
        redisTemplate.delete(key);
        data.put(key, value);
        return data;
    }

    @RequestMapping(value = "/list")
    public Map<String, String> list(@RequestParam("key") String key) {
        Map<String, String> data = new HashMap<>();
        //要去key的类型必须是string类型的
        Set<String> keys = redisTemplate.keys(key);
        if (keys != null) {
            for (String k : keys) {
                if (redisTemplate.type(k) == DataType.STRING) {
                    data.put(k, redisTemplate.opsForValue().get(k));
                }
            }
        }
        return data;
    }
}
```

## 1.3邮件发送

Spring提供了一个简单的发送邮件的工具库，我们只需要依赖它，然后配置邮件服务器就可以发送邮件的操作。通过借用第三方邮箱(比如：网易，qq)需要设置开启客户端发送邮件。

> 准备好测试邮箱和邮件服务

如下：

- 在pom.xml中添加spring-boot-starter-mail依赖

  ```
  
  ```

- 在application.properties中添加发送邮件的服务和账号信息

  ```
  
  ```

- Spring Boot 正常启动之后就会在Spirng IoC容器中创建一个`JavaMailSender Bean`。之后我们就可以使用JavaMailSender发送邮件了。

#### 1.3.1发送简单邮件示例

下面代码示例展示了两种发送简单邮件的方式:

一种直接使用 `Java Mail API` 

第二种使用`Spring`提供的发送邮 件的帮助类,**第二种的API相对比较简洁,使用起来较为容易理解.**

```

```

#### 1.3.2发送复杂邮件示例

下面代码中发送的带附件的邮件和内联资源邮件。

```

```

### 1.4测试

