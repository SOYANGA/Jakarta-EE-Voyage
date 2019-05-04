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
          <!--Spring Mail邮箱服务依赖-->
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-mail</artifactId>
          </dependency>
  ```

- 在application.properties中添加发送邮件的服务和账号信息

  ```properties
  #配置Mail客户端配置
  spring.mail.protocol=smtp
  spring.mail.host=smtp.qq.com
  spring.mail.port=465
  spring.mail.password=jixhgwgnpxpwbgei
  spring.mail.username=321830735
  spring.mail.properties.mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory
  ```

- Spring Boot 正常启动之后就会在Spirng IoC容器中创建一个`JavaMailSender Bean`。之后我们就可以使用JavaMailSender发送邮件了。

#### 1.3.1发送简单邮件示例

下面代码示例展示了两种发送简单邮件的方式:

一种直接使用 `Java Mail API` 

第二种使用`Spring`提供的发送邮 件的帮助类,**第二种的API相对比较简洁,使用起来较为容易理解.**

```java
@RestController
@RequestMapping(value = "/mail")
public class MailController {
    @Autowired
    private JavaMailSender mailSender;

    @RequestMapping(value = "/simpleMail1")
    public String mail1() {
        try {
            sendSimpleMail1();
            return "success";
        } catch (MessagingException e) {
            return "failed";
        }
    }

    /**
     * 第一种方式放松简单邮件
     * java类中的发送邮件方法
     * <p>
     * 构建发送邮件内容，以及发送方，发出方，邮件标题
     *
     * @throws MessagingException
     */
    public void sendSimpleMail1() throws MessagingException {
        //构建邮件主体
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //填充发送给谁
        mimeMessage.setRecipient(
                Message.RecipientType.TO,
                new InternetAddress("1765268431@qq.com")
        );
        //填充发送方
        mimeMessage.setFrom(new InternetAddress("321830735@qq.com"));
        //填充邮件标题
        mimeMessage.setSubject("SpringBoot send email by style1");
        //填充邮件内容
        mimeMessage.setText("Hello this is a simple mail");
        //发送邮件
        mailSender.send(mimeMessage);
    }


    @RequestMapping(value = "/simpleMail2")
    public String mail2() {
        try {
            sendSimpleMail2();
            return "success";
        } catch (MessagingException e) {
            return "failed";
        }
    }

    /**
     * 第二种方式放松简单邮件  --->构建简单，推荐使用
     * Spring中发送邮件的方法
     * <p>
     * 构建邮件主体：发出方，发送方，邮件标题，邮件内容
     *
     * @throws MessagingException
     */
    public void sendSimpleMail2() throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom("321830735@qq.com");
        helper.setTo("1765268431@qq.com");
        // 密送
        helper.setBcc("872041146@qq.com");
        //抄送
        helper.setCc("SOYANGA@126.com");
        helper.setSubject("SpringBoot send email by style2");
        helper.setText("Hello this is a simple mail to Jennie");
        mailSender.send(mimeMessage);
    }
}
```



#### 1.3.2发送复杂邮件示例

下面代码中发送的带附件的邮件和内联资源邮件。

```java
    @RequestMapping(value = "/multipartMail")
    public String mail3() {
        try {
            sendAttachments();
            return "success";
        } catch (MessagingException e) {
            return "failed";
        }
    }

    /**
     * 发送带附件邮件
     *
     * @throws MessagingException
     */
    public void sendAttachments() throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("321830735@qq.com");
        helper.setTo("1765268431@qq.com");
        // 密送
        helper.setBcc("872041146@qq.com");
        //抄送
        helper.setCc("SOYANGA@126.com");
        helper.setSubject("附件");
        helper.setText("Check out this image!");
        //添加附件以流的形式
        ClassPathResource resource = new ClassPathResource("感悟.png");
        helper.addAttachment("感悟.png", resource);
        mailSender.send(mimeMessage);
    }


    @RequestMapping(value = "/innerMail")
    public String mail4() {
        try {
            seendInnerResource();
            return "success";
        } catch (MessagingException e) {
            return "failed";
        }
    }

    /**
     * 带有内联资源邮件发送
     *
     * @throws MessagingException
     */
    public void seendInnerResource() throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("321830735@qq.com");
        helper.setTo("1765268431@qq.com");
        // 密送
        helper.setBcc("872041146@qq.com");
        //抄送
        helper.setCc("SOYANGA@126.com");
        helper.setSubject("内联资源");

        //资源引用处设置cid,资源标识 ---- >相当于网页
        helper.setText(
                "<html>" +
                        "<body>" +
                        "<h1>加油(ง •_•)ง 💪</h1>" +
                        "<img src='cid:img123'>" +
                        "</body>" +
                        "</html>", true);
        ClassPathResource resource = new ClassPathResource("TIM.png");
        //添加资源时指定cid,资源标识符
        helper.addInline("img123", resource);
        mailSender.send(mimeMessage);
    }
```

### 1.4测试

Spring Boot提供很多有用的测试应用的工具。`spring-boot-starter-test`的Starter提供Spring Test, JUnit, Hamcrest和Mockito的依赖。在spring-boot核心模块 `org.springframework.boot.test` 包下也有很多有用的测试工具。

如果使用 `spring-boot-starter-test `的 `Starter POM `（在test作用域内),将会提供下面库：

- Spring Test ：对Spring应用的集成测试支持 
- JUnit ：用于Java应用的单元测试，事实上的标准。
- Hamcrest: 一个匹配对象的库(也称为约束或前置条件)允许assertThat等JUnit等类型的断言
- MocKito：一个Java模拟框架。这也有一些我们写测试用例时经常用到的库

如果它们不能满足你的要求，你可以随意添加其他的测试用的依赖库。 

#### 1.4.1测试Spring Boot应用

一个Spring Boot应用只是一个`Spring ApplicationContext`，所以在测试它时除了正常情况下处理一个 `vanilla Spring context `外不需要做其他特别事情。唯一需要注意的是，如果你使用SpringApplication创建上下文，外部配置，日志和Spring Boot的其他特性只会在默认的上下文中起作用。

 Spring Boot提供一个``@SpringBootTest`注解用来替换标准的`spring-test  @ContextConﬁguration`注解。如果使用 @SpringBootTest来设置你的测试中使用的ApplicationContext，它终将通过SpringApplication创建，并且你将获取到Spring Boot的其他特性。

```java
import com.github.soyanga.springBootBasic.component.ExampleBean;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: springBoot-action
 * @Description: SpringBoot 测试
 * @Author: SOYANGA
 * @Create: 2019-05-04 17:46
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ExampleBeanTest {
    @Autowired
    private ExampleBean exampleBean;
    
    @Test
    public void test1() {
        Assert.assertEquals("jack", exampleBean.getName());
    }

    @Test
    public void test2() {
        Assert.assertEquals("jack2", exampleBean.getName());
    }
}
```

#### 1.4.2测试运行WEB服务

如果想让一个WEB应用启动，并且监听它的正常端口，可以使用HTTP来测试它（比如：使用`TestRestTemplate`）, 并且使用`@SpringBootTest`注解标识测试类。

```java
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SpringBootActionWebTest {
    @Autowired
    private TestRestTemplate testrestTemplate;

    @Test
    public void test1() {
        ResponseEntity<String> stringResponseEntity = testrestTemplate.getForEntity("http://localhost:80/mail/simpleMail1", String.class);
        Assert.assertEquals(HttpStatus.OK, stringResponseEntity.getStatusCode());
        Assert.assertEquals("success", stringResponseEntity.getBody());
    }

    @Test
    public void test2() {
        ResponseEntity<String> stringResponseEntity = testrestTemplate.getForEntity("http://localhost:80/mail/simpleMail2", String.class);
        Assert.assertEquals(HttpStatus.OK, stringResponseEntity.getStatusCode());
        Assert.assertEquals("success", stringResponseEntity.getBody());
    }

    @Test
    public void test3() {
        ResponseEntity<String> stringResponseEntity = testrestTemplate.getForEntity("http://localhost:80/mail//multipartMail", String.class);
        Assert.assertEquals(HttpStatus.OK, stringResponseEntity.getStatusCode());
        Assert.assertEquals("success", stringResponseEntity.getBody());
    }

    @Test
    public void test4() {
        ResponseEntity<String> stringResponseEntity = testrestTemplate.getForEntity("http://localhost:80/mail/innerMail", String.class);
        Assert.assertEquals(HttpStatus.OK, stringResponseEntity.getStatusCode());
        Assert.assertEquals("success", stringResponseEntity.getBody());
    }
}

```

上面代码中我们使用了@SpringBootTest的webEnvironment属性，取值为RANDOM_PORT，其中 webEnvironment取值有四种，分别代表了不同的测试环境。 

- **MOCK**：加载WebApplicationContext和提供一个模拟的Servlet环境，嵌入式Servlet容器不启动；如果 Servlet API不在classpath，这种模式会创建一个非WEB的ApplicationContext; 它可以 与 @AutoConﬁgureMockMvc一起使用，用于对应用程序进行基于mockmvc的测试。（默认值） 
- **RANDOM_PORT**: 加载ServletWebServerApplicationContext和提供真实的Servlet环境，嵌入式Servlet容器 启动和监听随机端口 
- **DEFINED_PORT**: 加载ServletWebServerApplicationContext和提供真实的Servlet环境，嵌入式Servlet容器 启动和监听端口号来自application.properties定义或者使用默认端口8080 
- **NONE**: 通过使用SpringApplication加载ApplicationContext，但是不提供任何的Servlet环境
  注意：Spring测试框架在每次测试时会缓存应用上下文。因此，只要测试共享相同的配置，不管实际运行多 少测试，开启和停止服务器只会发生一次。 

> ==**注意**==：**Spring测试框架在每次测试时会缓存应用上下文。因此，只要测试共享相同的配置，不管实际运行多少测试，开启和停止服务器只会发生一次。**

## 2.应用发布部署

SpringBoot应用的构建方式比较多，比如:基于Maven插件，基于AntLib模块等等，由于Maven目前是java项目构建工具中最为流行的，也是Spring Boot项目本身主选的构建工具，所以我们主讲基于Maven插件的发布部署。

### 2.1配置Maven插件

在pom.xml文件中添加Maven Plugin依赖

```xml
    <!--配置应用发布部署的依赖 Maven Plugin依赖-->
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

spring-boot-maven-plugin插件包含了如下几个goal(目标): 

- spring-boot:help : 插件的帮助目标,可以查看插件的基本信息以及各个目标的描述信息 
- spring-boot:build-info : 基于当前的Maven项目,生成build-info.properties信息 
- spring-boot:repackage : 基于已经构建存在的jar重新打包成fatjar,如果有主类就可以执行 
- spring-boot:run : 运行应用 
- spring-boot:start : 启动应用,用于集成测试 
- spring-boot:stop : 停止应用,用于集成测试 

### 2.2打包可执行fatjar运行

打包可执行jar的前提是我们需要将项目的打包格式设置为jar，如下设置：

```xml
    <groupId>com.github.soyanga</groupId>
    <artifactId>springBoot-action</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>jar</packaging><!-- 默认就是jar-->
    <name>springBoot-Basics</name>
    <description>Demo project for Spring Boot</description>
```

- 执行`mvn package`命令，构建可执行jar
- 切换目录到`target`下，执行`jar -jar spring-boot-1.0.0.jar`运行程序

### 2.3打包war文件，部署到容器中

我们知道当引入spring-boot-starter-web这个starter的时候, web容器的依赖已经添加到项目中去了。打包成war 文件(包)部署到容器中的时候，我们需要注意以下三点： 

- war包不能包含WEB容器的jar包（因为一旦包含部署到容器中运行会出现jar冲突，比如：javax.servlet-api） 
- 打包格式需要是war类型。 修改主类，继承SpringBootServletInitializer

下面我们对pom.xml文件做响应的调整，使得满足构建包是war包类型，并且可以部署到容器中正常运行。

- 修改打包类型

  ```xml
      <groupId>com.github.soyanga</groupId>
      <artifactId>springBoot-action</artifactId>
      <version>0.0.1-SNAPSHOT</version>
      <packaging>war</packaging>
      <name>springBoot-Basics</name>
      <description>Demo project for Spring Boot</description>
  ```

- 使得web容器依赖的jar的scope的范围为provided

  ```xml
          <!--Tomcat-->
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-tomcat</artifactId>
              <scope>provided</scope>
          </dependency>
  ```

- 修改启动类

  ```
  
  ```

  > 注意：如果WEB容器不是tomcat,那么需要修改的是对应的容器依赖starter(可能是：spring-boot-starterjetty，spring-boot-starter-undertow) 

修改完pom.xml文件和主类之后，我们就可以继续进行打包，部署

- 执行 mvn package 命令，构建可执行war 
- 切换目录到 target 下，将 spring-boot-1.0.0.war 部署到Tomcat的安装目录下的webapp即可完成部署。 

## 3.Proudction-ready特性

SpringBoot包含很多其他特性，它们可以帮助我们监控和管理发布到生产环境的应用。我们可以选择使用Http端点，JMX或者远程Shell（SSH或者Telnet）来**管理和监控应用程序。审计(Auditing),健康(Health)和数据采集(Metrics Gathering)**会自动应用到应用中去。

### 3.1产品特性

`spring-boot-actuator`模块提供了Spring Boot所有的product-ready特性。。启用该特性的简单方式就是添加 `spring-boot-actuator` 的 Starter POM 的依赖。

**`Actuator`（执行器）**的定义：执行器是一个制造业术语，指的是用于移动或控制东西的一个机械装置。一个很小的改变就能让执行器产生大量的运动。

基于Maven的项目想要添加执行器只需要添加下面的 `Starter` 依赖：

```xml
        <!--Spring 执行器依赖 Actuator-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```

### 3.2端点

执行器端点运行你监控应用及应用进行交互。*Spring Boot包含很多内置的端点，你也可以添加自己的*。例如， health端点提供了应用的基本健康信息。 内置很对HTTP接口（端点）

**端点暴露的方式取决于你采用的技术类型**。大部分应用选择HTTP监控，端点的ID映射到一个URL。*例如，默认情况下，health端点将被映射到 `/actuator/health`。* 

spring-boot-actuator提供的如下可用端点：

| ID          | 描述                                                         | 敏感（Sensitive） |
| ----------- | ------------------------------------------------------------ | ----------------- |
| autoconfig  | 显示一个auto-configuration的报告，该报告展示所有auto-configuration候选者及他们被应用或者为被应用的报告 | true              |
| beans       | 显示一个应用中所有Spring Beans的完整列表                     | true              |
| configprops | 显示一个所有@ConfigurationProperties                         | true              |
| dump        | 执行一个线程转储                                             | true              |
| env         | 暴露来自Spring ConfigurableEnvironment的属性                 | true              |
| health      | 展示应用的健康信息(当使用一个未认证连接访问时显示一个简单的`status`，使用认证连接访问则显示全部信息详情) | false             |
| info        | 显示任意的应用信息                                           | false             |
| metrics     | 展示当前应用的‘指标’信息                                     | true              |
| mappings    | 显示一个所有@RequestMapping路劲的整理列表                    | true              |
| shutdown    | 允许应用以优雅的方式关闭(默认情况下不允许)                   | true              |
| trace       | 显示trace信息(默认为最新的一些HTTP请求)                      | true              |

> 注：根据一个端点暴露的方式，sensitive参数可能会被用作一个安全提示。例如，在使用HTTP访问sensitive端点时需要提供用户名/密码(如果没有启用web安全，可能会简化为禁止访问该端点)。

启用Web端点

```properties
#包含所有的Web的端点 
management.endpoints.web.exposure.include=* 
#排除beans的端点 
management.endpoints.web.exposure.exclude=beans

```

#### 3.2.1自定义端点

使用Spring属性可以自定义端点。你可以设置端点是否开启（enabled），是否敏感（sensitive），甚至它的id。 例如，下面的application.properties改变了敏感性和beans端点的id，也启用了shutdown。

sensitive(敏感)：true 如果启动时需要密码

shutdown(停止)：true 可以在运行中直接停止

```properties
endpoints.beans.id=springbeans 
endpoints.beans.sensitive=false 
endpoints.shutdown.enabled=true
```

> 前缀 endpoints + . + name 被用来唯一的标识被配置的端点。

默认情况下，除了shutdown外的所有端点都是启用的。如果希望指定选择端点的启用，可以使用 `management.endpoint.<id>.enabled `属性。

下面示例是启用shutdown的endpoint:

```properties
management.endpoint.shutdown.enabled=true
```

下面的配置禁用了除info外的所有端点：

```properties
management.endpoints.enabled-by-default=false 
anagement.endpoint.info.enabled=true
```

#### 3.2.2健康信息 

健康信息可以用来检查应用的运行状态。它经常被健康软件用来提醒人们生产系统是否停止。health端点暴露的默认信息取决于端点是如何被访问的。**对于一个非安全，未认证的连接只返回一个简单的status 信息。对于一个安全或者认证过的连接其他详情信息也会展示。**

健康信息是从`ApplicationContext`中定义的所有`HealthIndicator beans`收集过来的。Spring Boot包含很多autoconﬁgured的HealthIndicators，开发者也可以自定义`HealthIndicator`。 

- **安全与HealthIndicators**

HealthIndicators返回的信息通常性质上有点敏感。例如：你可能不想将数据库服务器的详情发布到外面。因此， 在使用一个未认证的HTTP连接时，默认会暴露健康状态（Health Status）。

暴露健康状态：

```properties
//取值如下：when_authorized, never, always 
//when_authorized： 认证用户可以访问 
//never：健康状态不可查看 
//always：健康状态总是可以查看
management.endpoint.health.show-details=always
```

![1556980831706](D:\婕\JavaEE学习之路\Spring\SpringBoot\健康检查状态.png)

为防止**拒绝服务（Denial of Service 简称：DoS）**攻击，**Health响应会被缓存**。你可以使用 `management.endpoint.health.cache.time-to-live`属性改变默认的缓存时间（比如：1000ms）。 

- **自动配置的HealthIndicators** 

Spring Boot会在合适的时候自动配置下面的HealthIndicators。

![1556980927602](D:\婕\JavaEE学习之路\Spring\SpringBoot\HealIndicators自动配置.png)

- **自定义HealthIndicators** 

如果想为应用程序自定义健康信息，需要注册一个实现 `org.springframework.boot.actuate.health.HealthIndicator `接口的`Spring Beans`。需要提供一个 `health() `方法的实现，**并返回一个Health响应**。**Health响应需要包含一个``status`和可选的用于展示的详情。`details`**

```java
/**
 * @program: springBoot-action
 * @Description: 自定义HealthIndicator为程序提供健康信息
 * @Author: SOYANGA
 * @Create: 2019-05-04 22:45
 * @Version 1.0
 */
@Component
public class RandomHealth implements HealthIndicator {

    @Override
    public Health health() {
        boolean flag = new Random().nextBoolean(); //随机返回
        if (flag) {
            return Health.up().withDetail("info", "System Ok").build();
        } else {
            return Health.down().withDetail("info", "System error").build();
        }
    }
}
```

除了Spring Boot预定义的`Status[up.down]`类型，Health也可以返回一个代表新的系统状态的**自定义Status**。在这种情况 下，需要提供一个**HealthAggregator接口的自定义实现**，或使用`management.health.status.order`属性配置默认 的实现。
例如，假设一个新的，代码为FATAL的Status被用于你的一个HealthIndicator实现中。**为了配置严重程度**，你需要将下面的配置添加到application属性文件中：

```properties
management.health.status.order=FATAL, DOWN, OUT_OF_SERVICE, UNKNOWN, UP
```



## 总结

|      |      |
| ---- | ---- |
|      |      |
|      |      |

