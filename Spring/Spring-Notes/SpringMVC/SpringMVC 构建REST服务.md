# SpringMVC 构建REST服务

[TOC]

**重点**

> 1. 了解REST概念
> 2. 创建REST服务
> 3. 编写REST客户端

## 1.REST简介

### 1.1REST是什么?

REST（Representational State Transfer：表述性状态转译）是Web服务的一种架构风格；使用`HTTP`，`URI`，`XML,JSON，HTML`等广泛流行的标准和协议；轻量级，跨平台，跨语言的架构设计风格。他是一种风格而不是一种标准

- Representational （表述性):REST资源实际上可以用各种形式来进行表述，包括XML，JSON,甚至HTML最适合资源使用者的任意形式。
- State (状态)：当使用REST的时候，我们**更关注资源的状态**，而不是对资源采取的行为
- Transfer(转移) ：REST涉及到转移资源数据，它以某种表述形式从一个应用转译到另一个应用。

### 1.2为什么要使用REST?

![1556465096751](D:\婕\JavaEE学习之路\Spring\picture\REST1.png)

在以前，前后端是融合在一起的，比如：PHP，JSP，ASP。随着移动互联网的飞速发展，各种类型的Client端层出 不穷，就需要通过一套统一的接口分别为：WEB，iOS，Android乃至桌面程序提供服务。像Facebook Platfrom， 新浪微博开发平台，微信公共平台，百度API，快递信息检索等，它**们不需要有显示的前端，只需要一套提供服务 的接口**，于是REST成为了它们更好的选择。 

### 1.3REST架构主要原则- 一切皆资源

- 网络上的所有事务都被抽象为资源
- 每个资源都有一个唯一的资源标识符
- 同一个资源具有多种表现形式(XML，JSON等)
- 对资源的操作不会改变资源的标识符
- 所有操作都是无状态的  (基于HTTP协议)
- 符合REST原则的架构方式即可称谓RESTful

### 1.4什么RESTful

通常一个架构符合REST原则，我们称其为RESTful架构。

## 2.Spring如何支持REST

从3.0版本开始，Spring针对SpringMVC的一些增强功能REST提供了良好的支持。

### 2.1支持的方式

当前4.0版本，Spring支持以下方式来创建REST资源

- 控制器可以处理所有的HTTP方法。包括四个主要的REST方法：` GET PUT DELETE POST `
- 通过 `@PathVariable` 注解，控制器能够处理参数化URL（将变量输入作为URL的一部分） 
- 借助Spring的视图和视图解析器，资源能够以多种方式进行表达，包括将模型数据渲染为 `XML JSON Atom RSS` 的view实现 
- 使用 `ContentNegotiatingViewResolver` (内容协商视图解析器)来**选择合适的客户端的表述** 
- 借助 `@ResponseBody` 注解和各种 `HttpMessageConverter` 实现，能够替换基于视图的渲染方式 
- 借助` @RequestBody` 注解和各种 `HttpMessageConverter `实现，能够将传入的HTTP请求数据转化为传入控制器处理方法的Java对象 
- 借助 `RestTemplate `类Spring应用能方便的使用REST资源—**实际上是客户端**

### 2.2资源转换

Spring提供了两种方法将**资源的Java表述形式转换为发送给客户端的表述方式**:

- 内容协商（Content Negotiation）：选择一个视图，它能够将模型渲染为呈现给客户端的表述形式 **（模型数据+视图转换）**
- 消息转换器（Message Conversion）： 通过一个消息转化器将控制器所返回的对象转换为呈现给客户端的表述形式 **（模型转换）**

### 2.3内容协商

内容协商它是SpringMVC之上**构建的REST资源表述层**，控制代码无需修改。**相同的一台控制器方法能够面向用户产生HTML内容**，也针对不是人类客户端产生的JSON或者XML。

如果**面向人类用户**的接口和**面向非人类客户端**的接口之间有很多重叠的话，那么内容协商是一种很便利的方案。在实践中，面向人类用户的视图与REST API在细节上很少能够处于相同的级别。如果面向人类的用户接口与面向非人类客户端接口之间没有太多重叠的话，那么内容协商的优势就体现出不来了。

内容协商还有一个严重的限制。作为**ViewResolver实现**，**它只能决定资源如何渲染到客户端，并没有涉及到客户端要发送什么样的表述给控制器使用**。如果客户端发送JSON或者XML的话，那么内容协商就无法提供帮助了。==（*只能处理服务端的响应*）==

鉴于以上内容协商的优缺点，**我们在创建基于Spring MVC的资源表述时，更加倾向使用消息转化器。** 

### 2.4消息转换器

消息转化器提供了一种更为直接的方法，**它能够将控制器产生的数据转换为服务与客户端的表述形式**。当使用消息转换的时候，`DispatcherServlet`不需要那么麻烦地将模型数据传送到视图中。实际上，也跟本没有了模型和视图，**只有控制器产生的数据，以及消息转换器(Message Converter)转换数据之后所产生的资源表述。**

**举例:**假设客户端通过请求的`Accept头`信息表明它能够接受`application/json`,并且`Jackson JSON`库在类路径下，那么处理方法返回的对象将交给``MappinglacksonHttpMessageConverter`，并且由它转换为客户端的JSON表述形式，另外，如果请求头的信息表明客户端相应`text/xml`格式，那么`Jaxb2RootElementHttpMessageConverter`将会作为客户端产生`XML`响应

Spring提供了多个HTTP消息转换器，用**于实现资源表述与各种Java类型之间的互相转换**，下面列出几个常用的消息转换器：

- `MappingJackson2HttpMessageConverter` ：在**JSON和类型化对象**或者**非类型化的HashMap**间互相读取和写入，如果*Jackson 2 JSON库在类路径下将进行注册*
- `GsonHttpMessageConverter` ：在J**SON和类型化对象**或者**非类型化的HashMap**间互相读取和写入，如果 *Gson库在类路径下将进行注册* 
- `FormHttpMessageConverter` ：将 `application/x-www-form-urlencoded` 内容读入到 **MultiValueMap<String,String>**中，也会将**MultiValueMap<String,String>**写入到 `application/x-www-formurlencoded` 中或将**MultiValueMap<String,Object>**写入到 `multipart/form-data` 中   *表单提交*
- `StringHttpMessageConverter `：将**所有媒体类型（/）读取为String**，将String写入为text/plain 
- `Jaxb2RootElementHttpMessageConverter` ：在XML（`text/xml或者application/xml`）和**使用JAXB2注解对象间相互读取和写入**，如果JAXB v2库在类路径下将进行注册 
- 等等

尽管Spring支持了多种资源表述形式，以及多种消息转化器。但是在**定义REST API**的时候，不一定全部使用它们。 对于大多数客户端来说，使用 **JSON和XML**来进行表述就足够了。

> 备注：实际在REST API中大多数情况表述都是用JSON格式 

转换过程图概述：

![REST之消息转换器](D:\婕\JavaEE学习之路\Spring\picture\REST之消息转换器.png)

## 3.创建一个REST项目

### 3.1创建Mvaen项目

- 创建Maven Web项目
- 创建Web项目所需要的目录

<details>
<summary>展开查看</summary>
<pre><code>.   
├─src    
│  └─main    
│      ├─java    
│      │  └─com    
│      │      └─bittech    
│      │          └─springrest
|	   |		  	  |-dao     //（持久化）数据库层
|	   |		  	  |-entity	//实体类
│      │              ├─control  //web层    
│      │              └─service  //业务层    
│      │                  └─impl    
│      ├─resources    
│      └─webapp    
│          └─WEB-INF
</code></pre>
</details>

- pom.xml中添加SpringMVC依赖

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <project xmlns="http://maven.apache.org/POM/4.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <modelVersion>4.0.0</modelVersion>
  
      <groupId>com.github.soyanga.springRESTCase</groupId>
      <artifactId>springREST-case</artifactId>
      <version>1.0-SNAPSHOT</version>
      <build>
          <plugins>
              <plugin>
                  <groupId>org.apache.maven.plugins</groupId>
                  <artifactId>maven-compiler-plugin</artifactId>
                  <configuration>
                      <source>8</source>
                      <target>8</target>
                  </configuration>
              </plugin>
          </plugins>
      </build>
      <packaging>war</packaging>
  
      <dependencyManagement>
          <dependencies>
              <dependency>
                  <groupId>org.springframework</groupId>
                  <artifactId>spring-framework-bom</artifactId>
                  <version>4.3.9.RELEASE</version>
                  <type>pom</type>
                  <scope>import</scope>
              </dependency>
          </dependencies>
      </dependencyManagement>
  
  
      <dependencies>
          <!--SpringMVC模块依赖添加-->
          <dependency>
              <groupId>org.springframework</groupId>
              <artifactId>spring-webmvc</artifactId>
          </dependency>
  
          <!--Servlet API-->
          <dependency>
              <groupId>javax.servlet</groupId>
              <artifactId>javax.servlet-api</artifactId>
              <version>3.1.0</version>
              <scope>provided</scope>
          </dependency>
  
          <!--日志依赖-->
          <dependency>
              <groupId>org.slf4j</groupId>
              <artifactId>slf4j-api</artifactId>
              <version>1.7.25</version>
          </dependency>
          <dependency>
              <groupId>ch.qos.logback</groupId>
              <artifactId>logback-classic</artifactId>
              <version>1.2.2</version>
          </dependency>
  
  
          <!-- 第二种方式2.2 Spring中配置Multipart解析器
          上传文件库 Jakarta Commons Fileupload-->
          <dependency>
              <groupId>commons-fileupload</groupId>
              <artifactId>commons-fileupload</artifactId>
              <version>1.3.1</version>
          </dependency>
  
          <!--第一种-->
          <!--MappingJacksonHttpMessageConverter 依赖-->
          <dependency>
              <groupId>com.fasterxml.jackson.core</groupId>
              <artifactId>jackson-core</artifactId>
              <version>2.9.1</version>
          </dependency>
          <dependency>
              <groupId>com.fasterxml.jackson.jaxrs</groupId>
              <artifactId>jackson-jaxrs-json-provider</artifactId>
              <version>2.7.8</version>
          </dependency>
  
  
          <!--第二种-->
          <!--GsonHttpMessageConverter依赖 JSON 处理库-->
          <dependency>
              <groupId>com.google.code.gson</groupId>
              <artifactId>gson</artifactId>
              <version>2.8.2</version>
          </dependency>
  
          <!--Lombok-->
          <dependency>
              <groupId>org.projectlombok</groupId>
              <artifactId>lombok</artifactId>
              <version>1.18.6</version>
          </dependency>
      </dependencies>
  
  </project>
  ```

### 3.2配置web.xml

```xml
    <!--设置根上下文参数配置  与Spring Core容器相关-->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:application-context.xml</param-value>
    </context-param>
    <!--注册ContextLoaderListener-->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>


    <!--前端控制器，注册DispatcherServlet-->
    <servlet>
        <servlet-name>servlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:application-servlet.xml</param-value>
        </init-param>
    </servlet>
    <servlet-mapping>
        <servlet-name>servlet</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>

</web-app>
```

### 3.3配置容器 application-servlet

```xml
    <!--配置自动注入-->
    <!--扫描包 control-->
    <context:component-scan base-package="com.github.soyanga.springmvc.control"/>

    <!-- 启动springMVC的注解功能，它会自动注册HandlerMapping,HandlerAdapter,ExceptionResolver的相关实例 -->
    <mvc:annotation-driven/>

    <!--2.1第二种方式配置- Mutipart解析器-->
    <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <property name="maxUploadSize" value="10240000"/>
        <property name="defaultEncoding" value="utf-8"/>
        <property name="maxInMemorySize" value="4096"/>
    </bean>

</beans>
```

### 3.4编写控制器

```java
/**
 * @program: springREST-case
 * @Description: 服务器信息控制器 REST服务器构建-编写控制器 不需要前端与视图层
 * @Author: SOYANGA
 * @Create: 2019-04-29 15:38
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/system")
public class SystemInfoController {

    /**
     * @return text/plain 文本格式响应给客户端
     * 因为@RequestMapping中就已经包含@RequestBody这个注解，返回的内容将在正文中体现
     */
    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index() {
        return "Hello";//text plain
    }

    /**
     * @return 返回一个服务器的信息，会转换自动转为JSON格式进行返回  -HashMap格式
     */
    @RequestMapping(value = "/info", method = {RequestMethod.GET})
    public HashMap<String, String> info() {
        HashMap<String, String> data = new HashMap<>();
        data.put("date_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        Runtime runtime = Runtime.getRuntime();
        data.put("server_processor", String.valueOf(runtime.availableProcessors()));
        data.put("server_total_memory", runtime.totalMemory() / 1024 / 1024 + " MB");
        data.put("server_max_memory", runtime.maxMemory() / 1024 / 1024 + " MB");
        data.put("server_free_memory", runtime.freeMemory() / 1024 / 1024 + " MB");
        return data;
    }

    /**
     * @param user 自定义实体类
     * @return 自定义java实体类作为响应返回时会自动转化问JSON格式
     */
    @RequestMapping(value = "/user", method = {RequestMethod.POST})
    public User query(@RequestBody User user) {
        return user;
    }
}

```

### 3.5客户端工具进行访问

- 通过REST Client工具测试REST API(这个使用著名的Postman工具)

![1556526067223](D:\婕\JavaEE学习之路\Spring\picture\postman_RESTClient测试工具.png)



## 4.REST客户端

作为客户端，编写与REST资源交互的代码比较乏味，因为基本模式差不多，编写的代码都是样板，比如：处理HTTP请求响应，以及异常。鉴于在资源处理上有大量的样板代码，**Spring提供了RestTemplate来完成样板所作的事情，这点根JdbcTemplate处理JDBC数据访问相似。**

除了Spirng提供的**RestTemplate客户端**之外，常用的REST客户端还有如下内容：

- [okhttp](http://square.github.io/okhttp/) 
- [Unirest](http://unirest.io/)
- [Apache HttpClient ](http://hc.apache.org/)  

### 4.1RestTemplate的操作

RestTemplate涵盖了所有HTTP动作(除Trace外)，除此之外，execute()和exchager()提供了较低层次的通用方法来使用任意的HTTP方法。

RestTemplate定义了11个独立的操作，每个操作都用重载方法。

- delete():在特定的URL上对资源执行HTTP DELETE操作
- exchange() : 在URL上执行特定的HTTP方法，返回包含对象的ResponseEntity,这个对象是从响应体中映射得到的
- execute() ：在URL上执行特定的HTTP方法，返回一个从响应体映射得到的对象
- getForEntity() ：发送一个HTTP GET请求，返回的ResponseEntity包含了响应体所映射成的对象
-  getForObject() ：发送一个HTTP GET请求，返回的请求体将映射成为一个对象 
- headForHeaders() ：发送HTTP HEAD请求，返回包含特定资源URL的HTTP头 
- optionsForAllow() : 发送HTTP OPTIONS请求，返回对特定URL的Allow头信息 
- postForEntity() ： POST数据到一个URL，返回包含一个对象的ResponseEntity,这个对象是从响应体中映射得到的 
- postForLocation() ：POST数据到一个URL，返回新创建资源的URL 
- postForObject() ：POST数据到一个URL，返回根据响应体匹配形成的对象 
- put()：PUT资源到特定的URL

上面大多数操作都以下面三种方式进行**重载**：

- **一个使用 java.net.URI 作为URL格式，不支持参数化URL** 
- **一个使用String作为URL格式，并使用Map指定URL参数** 
- **一个使用String作为URL格式，并使用可变参数列表指明URL参数** 

了解了RestTemplate提供的方法以及各种变体方法之后，就能够以自己的方式编写使用REST资源的客户端了。 

### 4.2RestTemplate练习

#### 4.2.1创建一个Maven项目

#### 4.2.2添加pom.xml依赖

构件`spring-webmvc`是使用RestTemplate的必选构件。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.github.soyanga.springRESTCase</groupId>
    <artifactId>springREST-case</artifactId>
    <version>1.0-SNAPSHOT</version>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>8</source>
                    <target>8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
    <packaging>war</packaging>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-framework-bom</artifactId>
                <version>4.3.9.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>


    <dependencies>
        <!--SpringMVC模块依赖添加 包含Rest客户端-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
        </dependency>

        <!--Servlet API-->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>3.1.0</version>
            <scope>provided</scope>
        </dependency>

        <!--日志依赖-->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.25</version>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.2</version>
        </dependency>


        <!-- 第二种方式2.2 Spring中配置Multipart解析器
        上传文件库 Jakarta Commons Fileupload-->
        <dependency>
            <groupId>commons-fileupload</groupId>
            <artifactId>commons-fileupload</artifactId>
            <version>1.3.1</version>
        </dependency>

        <!--第一种-->
        <!--MappingJacksonHttpMessageConverter 依赖-->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-core</artifactId>
            <version>2.9.1</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.jaxrs</groupId>
            <artifactId>jackson-jaxrs-json-provider</artifactId>
            <version>2.7.8</version>
        </dependency>


        <!--第二种-->
        <!--GsonHttpMessageConverter依赖 JSON 处理库-->
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.8.2</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-oxm</artifactId>
        </dependency>

        <!--Lombok-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.6</version>
        </dependency>
    </dependencies>

</project>
```

#### 4.2.3基于Java Config配置容器



#### 4.2.4使用RestTemplate访问资源



## 总结

