# 初始Spring MVC

**重点**

> 1.了解MVC思想
>
> 2.理解SpringMVC工作流程
>
> 3.掌握SpringMVC项目搭建

## 1.SpringMVC概述

### 1.1Spring简介

大部分java应用都是Web应用，展现层（Presentation Layer）是Web应用最为重要的部分。Spring为**展现层**提供了一个优秀的web框架——Spring MVC。和其他web框架一样，它基于MVC的设计理念，此外，它采用了松散耦合可插拔组件结构，比其他MVC框架更具有扩展性和灵活性。

SpringMVC通过一套MVC注解，让**POJO成为处理请求的控制器**，**无需实现任何接口**，同时，SpringMVC还支持REST风格的URL请求。此外，**SpringMVC在数据绑定，视图解析，本地化处理及静态资源处理**上都有许多不俗的表现。

它在框架设计，扩展性，灵活性等方面全面超过Struts,WebWork等MVC框架，从原来的追赶着一跃称为MVC的领跑者。

SpringMVC框架围绕**DispatcherServlet(前端控制器)**这个核心展开，**==DispatcherServlet==**是SpringMVC框架的总导演，总策划，它**负责截获请求并将其分派给相应的处理器其处理**。

### 1.2SpringMVC特点

- 进行更加简洁的Web层的开发；
- **天生与Spring框架集成（如IoC容器，AOP等）**;
- 提供强大的约定大于配置的**契约式编程支持**；
- 能简单的进行Web层的单元测试
- 支持灵活的URL到页面控制器的映射；
- 非常容易与其他视图技术集成，如Velocity,FreeMarket等，因为模型数据不放在特定的API里，而是放在一个Model里(Map数据结构实现，因此很容以被其他框架使用)；
- 非常灵活的数据验证，格式化数据绑定机制，能够使用任何对象进行数据绑定，不必实现特定框架API;
- 支持灵活的本地化，主题等解析；
- 更加简单的异常处理；
- 对静态资源的支持；
- **支持Restfu风格**。



## 2.MVC设计理念

### 2.1MVC框架模式

**模型-视图-控制器(MVC模式)**是一种非常经典的软件架构模式，在UI框架和UI设计思路中扮演者非常重要的角色。从设计模式的角度来看，MVC模式是一种**复合模式**，它将多个设计模式在一种解决方案中结合起来，用来解决许多设计问题。**MVC模式把用户界面交互拆分到不同的三种角色中**，使应用程序被分成了三个核心部件:`Model(模型)，View(视图)，Control(控制器)`，它们各自处理自己的任务：

- **模型**:  模型持有所有数据、状态和程序逻辑；模型独立于视图和控制器
- **视图**：用来呈现模型。视图通常直接从模型中取得它需要显示的状态和数据。对于相同的信息可以有多个不同的显示形式或视图
- **控制器**：*位于视图和模型中间*，负责接受用户的输入，将**输入进行解析并反馈给模型**，**通常一个视图具有一个控制器**。

![MVC模型框架](D:\婕\前端学习\MVC模型框架.png)



MVC模式将它们分离以提高系统的**灵活性**和**复用性**，不使用MVC模式，用户界面设计往往将这些对象混在一起。MVC模式实现了模型和视图的分离，这带来了几个好处。

- 一**个模型提供不同的多个视图表现形式**，也能够为一个模型创建新的视图而**无须重写模型**。一旦模型的数据发生改变，模型将通知有关的视图，每个视图相应地刷新自己。
- **模型可复用**，因为模型独立于视图，所以可以把一个**模型独立地移植到新地平台工作**。
- **提高开发效率**。在开发界面显示部分时，你仅仅需要考虑的是如何布局一个好的用户界面；开发模型时，你仅仅要考虑的是业务逻辑和数据维护。这样能使得**开发者专注某一方面的开发，提高开发效率。** **分模块开发**

### 2.2Servlet处理Web请求响应

我们在初次接触JavaWeb编程的时候就介绍了Servlet。**Servlet负责在处理用户请求和响应**，**其中数据的处理，页面响应都是在Servlet里面完成的**。如下图是Servlet请求响应示意图：

![1556037618989](D:\婕\前端学习\Servlet处理请求响应.png)

Servlet对应的就是**MVC框架模式中的控制器**

### 2.3SpringMVC请求响应

#### 2.3.1SpringMVC请求处理的流程图(高层次)

![1556037750191](D:\婕\JavaEE学习之路\Spring\picture\SpringMVC处理请求响应.png)

上面这张图是SpringMVC请求处理的比较高层次的流程图（隐含了更多细节），从图上看它与Servlet处理请求的最大不同在于，它将**视图渲染，请求处理，模型创建**分离了。即遵循了MVC框架模式的基本思想。 

#### 2.3.2特定组件的请求流程

![1556037914738](D:\婕\JavaEE学习之路\Spring\picture\SpringMVC特定组件请求流程.png)

上图是将**高层次的流程图**细化到**SpringMVC框架的具体实现**上了，通过上图我们可以看出，SpringMVC的请求响应 经过的**7个阶段**。其中2-6阶段分别对应着SpringMVC框架中的几个概念，分别是：**前端控制器，处理映射器，，控制器(处理器），视图解析器，视图。**

> 绿色部分：需要去手动实现

#### 2.3.3核心类的请求流程

![1556038080847](D:\婕\JavaEE学习之路\Spring\picture\SpringMVC核心类的请求流程.png)

SpringMVC的请求响应步骤如下:

具体步骤:

1. (**发起**)发起请求到前端控制器(DispatcherServlet)
2. (**查找**)前端控制器请求HandlerMapping查找Handler（可以根据xml配置、注解进行查找）
3. (**返回**)处理器映射器HandlerMapping向前端控制器返回Handler,HandlerMapping会把请求映射为HandlerExecutionChain对象(包含一个Handler处理器(页面控制器)对象，多个HandlerInterceptor（拦截器对象），通过这种策略模式，很容易添加新的映射策略)
4. (**调用**)前端控制器调用处理器适配器去执行Handler
5. (**执行**)处理器适配器HandlerAdapter将会根据适配的结果去执行Handler 执行controller方法
6. (**返回**)Handler执行完成给处理器适配器返回ModelAndView
7. (**接收**)处理器适配器向前端控制器返回ModelAndView（ModelAndView是SpringMVC框架的一个底层对象，包括Model和View）
8. (**解析**)前端控制器请求视图解析器去进行视图解析（根据逻辑视图名解析成真正的视图(jsp)）通过这种策略很容易更换其他视图技术，只需要更改视图解析器即可 。
9. (**返回**)视图解析器向前端控制器返回View
10. (**渲染**)前端控制器进行视图渲染（视图渲染将模型数据(在ModelAndView对象中)填充到request域）
11. (**响应**)前端控制器向用户响应结果

## 3.快速开启SpringMVC项目

### 3.1前期准备

tomcat8，IDEA

### 3.2创建Maven项目

- 创建Maven项目
- **创建WEB项目中需要的目录**

<details>
    <summary>展开查看</summary>
    <pre><code>
.
├─src
│  └─main
│      ├─java
│      │  └─com
│      │      └─bittech
│      │          └─springmvc
│      │              ├─entity	//实体类
│      │              ├─dao     //（持久化）数据库层
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
 <!--SpringMVC模块依赖添加-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
        </dependency>
```

### 3.3配置web.xml

`web.xml`文件位于：`src/main/webapp/WEB-INF/web.xml`,该文件可以从Tomcat的目录`apache-tomcat-8.5.30\conf\web.xml`中获取。

```xml
<?xml version="1.0" encoding="utf-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                      http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1" metadata-complete="true">
    <description>This is Java Spring MVC Application</description>


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

	<!--设置欢迎页面-->
    <welcome-file-list>
        <welcome-file>index.html</welcome-file>
    </welcome-file-list>
</web-app>
```

### 3.4配置SpringMVC容器 

**application-servlet.xml中配置** 

#### 3.4.1配置视图

```xml
<!--配置视图解析器-->
<!--/abc  ==>  /WEB_INF/views/abc.jsp-->
<!--/index  ==>  /WEB_INF/views/index.jsp-->
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <property name="prefix" value="/WEB-INF/views/"/>
    <property name="suffix" value=".jsp"/>
</bean>
```

#### 3.4.2配置自动注入

```xml
<!--配置自动注入-->
<!--扫描包 control-->
<context:component-scan base-package="com.github.soyanga.springmvc.control"/>
<!-- 启动springMVC的注解功能，它会自动注册HandlerMapping,HandlerAdapter,ExceptionResolver的相关实例 -->
<mvc:annotation-driven/>
```

### 3.4.3容器继承关系

`ApplicationContext`实例是在Spring的作用域中，而Web MVC框架中每个`DispatcherServlet`有自己的`WebApplicationContext`,其继承所有已经在`Root WebApplicationContext`中定义的Bean。

![1556181862029](D:\婕\JavaEE学习之路\Spring\picture\容器继承关系.png)

### 3.5编写Controller

```java
@Controller
@RequestMapping
public class HelloWorldController {

    @RequestMapping(value = {"", "/index"}, method = {RequestMethod.GET})
    public ModelAndView hello() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("greetting_message",
                "Welcome to at" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
       
        modelAndView.setViewName("index");  //WEB_INF/Views/index.jsp
        return modelAndView;
    }
}
```

### 3.6编写简单的View

 :file_cabinet: index.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Spring MVC</title>
</head>
<body>
<h1>当前时间${greetting_message}</h1>
</body>
</html>
```

### 3.7配置Tomcat部署服务

- 运行Tomcat
- 成功后访问:`http://ip:port/index`地址（设置欢迎页地址后就不用去访问，自动就去访问这个设置的欢迎页地址）

### 3.8SpringMVC涉及的元素

上面展示的了从创建一个SpringMVC项目涉及的各个方面的内容，归纳为如下图所示:

![1556182140466](D:\婕\JavaEE学习之路\Spring\picture\SpringMVC涉及的元素.png)