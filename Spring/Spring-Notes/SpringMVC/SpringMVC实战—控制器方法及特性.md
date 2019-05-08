# SpringMVC实战—控制器方法及特性

[TOC]

## 2.Spring的控制器

### 2.1控制器方法签名

在控制器方法签名，主要是**将HTTP请求信息绑定到方法的相应入参中**。

- @RequestParam

使用@RequestParam注解可以**将请求参数传递给请求方法**，其中**value是参数名**，**required是参数是否必须**，**默认ture表示请求参数必须包含对应参数**，若不存在，将**抛出异常**。

```java
@RestController   
@RequestMapping("/home")   
public class IndexController {
    
    @RequestMapping(value = "/name")      
  	String getName(@RequestParam(value = "person", required = false) String personName)  {          
    	return "Required element of request param";      
    }   
}
```

- @ReqestHeader

使用@RequestHeader注解可以将**请求头中的属性值绑定到处理方法的入参中**

```java
/**
* 2.1.1@RequestHeader
* 通过@RequestHeader获取请求头的Header对应的头信息并且作为参数
*
* @param encoding  "Accept-Encoding" 请求头参数
* @param aliveTime "Keep-Alive"
* @return
*/
@RequestMapping("/header")
public String header(
       @RequestHeader(value = "Accept-Encoding") String encoding,
       @RequestHeader(value = "Keep-Alive", required = false, defaultValue = "0") long aliveTime
    ) {
        return String.format("Accept-Encoding:%s,Keep-Alive:%d", encoding, aliveTime);
}
```

encoding是请求头中的“Accept-Encoding”参数；keepAlive是请求头当中“Keep-Alive”参数。

- CookileValue

使用@CookileValue注解可以将某个Cookie绑定到处理方法的入参中。

```java
/**
* 2.1.2@CookileValue
* 通过@CookileValue获取session的name，value并且作为参数
*
* @param sessionId
* @return
*/
@RequestMapping("/cookie")
public String cookie(
       @CookieValue(value = "JSESSIONID", required = false, defaultValue = "hahaha") String sessionId
    ) {
        return "Cookie JSESSIONID = " + sessionId;
}
```

- PathVariable

  使用@PathVariable注解可以从URI模板中获取路径参数值。

- POJO对象

SpringMVC框架会按照请求参数名和POJO属性名进行自动匹配，自动为该对象填充属性值，且支持级联属性，例如:address.name,address.code

```java
    /**
     * 定义两个静态内部类POJO类
     */
    @Data
    public static class User {
        private int id;
        private String name;
        private Address address;
        //省略getter setter
    }
    
    @Data
    public static class Address {
        private String name;
        private String code;
        //省略getter setter
    }

    /**
     * 2.1.3POJO类作为参数
     * http://localhost:8080/controller/pojo?id=1&name=SOYANGA&address.name=XIAN&address.code=7100
     * 将URL中的参数进行抽象及封装成一个POJO类返回对应
     * ControllerMethodController.User(id=1,name=SOYANGA,address=ControllerMethodController.Address(name=XIAN, code=7100))
     *
     * @param user
     * @return
     */
    //-->
    @RequestMapping("/pojo")
    public String pojo(User user) {
        //处理逻辑    
        return user.toString();
    }
```

- Servlet API

  ServletAPI主要是指控制器方法参数类型为Java Servlet API中的类。

  ```java
      /**
       * 2.1.4ServletAPI主要是指控制器方法参数类型为Java Servlet API 中的类。
       * SpringMVC会自动进行request以及 response ,session的获取
       * @param request 
       * @param response
       * @return
       */
      @RequestMapping("servlet_handle_01")
      public String requestServletHandle01(
              HttpServletRequest request,
              HttpServletResponse response) {
          //处理逻辑    
          return "success";
      }
      
      @RequestMapping("servlet_handle_02")
      public String requestServletHandle02(HttpSession session) {
          //处理逻辑
          return "success";
      }
  ```




### 2.2控制器方法支持的参数类型

1. 请求和响应对象(Servlet API),例如:HttpServletRequest,HttpServletResponse
2. 会话对象(Servlet API)例如:HttpSession
3. org.springframework.web.context.request.WebRequest
4. org.springframework.web.context.request.NativeWebRequest
5. java.util.Locale 当前请求的区域
6.  java.util.TimeZone(java 6+)/java.time.ZoneId(java 8) j
7. ava.io.InputStream/java.io.Reader 访问请求内容 
8. java.io.OutputStream/java.io.Writer 生成响应内容 
9. org.springframework.http.HttpMethod 
10. java.util.Map/org.springframework.ui.ModelMap 
11. 更多 

### 2.3控制器方法支持的返回值类型

- ModeAndView

控制器处理方法返回值ModelAndView其既包含视图信息，又包含模型数据信息，这样SpringMVC就可以将模型数据对视图进行渲染。可以简单的就讲那个模型数据看作是Map<String,Object>。

- Map以及Model

SpringMVC在内部使一个`org.springframework.ui.Model`接口存储模型数据，功能类似`java,util,Map`,但是比Map易用。`org.springframework,ui,ModelMap`

实现了Map接口，而` org.springframework.ui.ExtendedModelMap` 扩展了ModelMap的同时实现了Model接口。

**SpringMVC在调用方法前会创建一个隐含的模型对象，作为模型数据的存储容器，我们称之为“隐含模型”**。**如果处理方法的入参数为Map或者Model类型，则SpringMVC会将隐含的引用传递给这些入参。**

```java
   /**
     * 2.3 控制器方法支持的返回值类
     * 第一步：
     *
     * <p>
     * 访问控制器中的任何一个请求处理方法栈，SpringMVC会先执行该方法
     * 并将返回值以user为键添加到模型中   即讲user作为模型初始化且暂存起来
     */
    //内部隐藏一个model<String,Object> 将 "user"做为key，而user对象的属性作为value进行填充
    @ModelAttribute(value = "user")
    public User getUser() {
        User user = new User();
        user.setId(100);
        user.setName("Jack");
        Address address = new Address();
        address.setName("XIAN");
        address.setCode(String.valueOf(710000));
        user.setAddress(address);
        return user;
    }

    /**
     * 第二步：
     * 模型数据会赋值给User的入参，然后再根据HTTP请求的消息进一步填充覆盖User对象
     * 即进一步对user这个模型对象进行补充,或者更改
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/user")
    public String user(@ModelAttribute("user") User user) {
        user.setName("SOYANGA");
        return user.toString();
    }
```

- String：逻辑视图名或者通过@ResponseBody注解标注之后作为响应内容

```java
    /**
     * 当在@Controller的类中的方法返回的类型是String类型时，SpringMVC会将你的返回的值在结尾添加.jsp在
     * WEB-INF/views/welcome.jsp 目录下找作为响应的view，但是当你用@ResponseBody或者类是用@RestController修饰时
     * 返回值将将作为单纯的字符串进行返回
     *
     * @return 字符串/或者view
     */
    @RequestMapping(value = "/welcome", method = {RequestMethod.GET})
    @ResponseBody
    public String welcome() {
        return "welcome"; //WEB-INF/views/welcome.jsp
    }
```



- void：如果使用ServletResponse/HttpServletResponse处理响应

  ```java
      /**
       * SpringMVC作为导员，只做了将url映射到这个方法中，对服务器的请求处理以及响应的封装全部又我们手动完成
       * 用户极端情况下，SpringMVC无法做到某些事情的时候
       * @param request
       * @param response
       */
      @RequestMapping(value = "/servlet")
      public void servletApi(HttpServletRequest request, HttpServletResponse response) {
          response.setContentType("text/html;charaset=utf-8");
  
          try {
              PrintWriter writer = response.getWriter();
              writer.append("<h1>hello<h1>");
              //请求转发
  //            response.sendRedirect("/welcome");
          } catch (IOException e) {
              e.printStackTrace();
          }
  
  //        //请求重定向
  //        try {
  //            request.getRequestDispatcher("/welcome").forward(request, response);
  //        } catch (ServletException e) {
  //            e.printStackTrace();
  //        } catch (IOException e) {
  //            e.printStackTrace();
  //        }
      }
  ```

- 等等。。。。。。

## 3.SpringMVC常用特性

### 3.1 上传文件

在Web应用中，允许用户上传内容是很常见的需求，一般表单提交所形成的请求结果都很简单，，以“&”分割符的多个name-value对。而上传文件则需要处理multipart形式的数据。

#### 3.1.1Multipart形式数据请求详解

如图是一个上传文件的Http请求:

![1556421369044](C:\Users\32183\Desktop\Http上传文件请求报文.png)

- 请求头

最重要的内容是`content-Type`,指定了内容类型(划线部分)

> 1. 第一个参数`mutipart/form-data`是必须的，指提供表单中有附件(文件)
> 2. 第二个参数`boundary`表示分隔线，请求体中会用来分割不同的请求参数

- 请求体

请求体表示发服务端的内容(画框部分)

> 1. 第一行：是`------`和`boundary`
> 2. 第二行：固定的`content-Disposition`
> 3. 第三行：文件类型
> 4. 第四行：空行
> 5. 接下来就是上传文件的内容
> 6. 最后一个行是是`------`和`boundary`+`--`拼接而成表示结束

#### 3.1.2Spring中配置Mutipart解析器

`DispatcherServlet`没有实现任何Mutipart请求数据的功能。他将该任务委托给了Spring中的`MutipartResolver`策略接口实现。通过实现该接口来解析Mutipart请求中的内容。Sring3.1开始内置了两个`MutipartResolver`的实现供我们选择

- `commonsMultipartResolver`：使用用[Jakarta Commons Fileupload](http://commons.apache.org/proper/commons-fileupload/)库 解析Multipart请求 
- `StandardServletMultipartResolver`:依赖于Servlet3.0对Multipart请求的支持(始于Spring3.1)

一般来讲，在这两者之间` StandardServletMultipartResolver `作为优选方案。它使用Servlet提供的功能支持，**不需要依赖任何其它第三方库**。但是，如果应用部署到Servlet3.0之前的容器中，或者没有使用Spring3.1+版本，则需要选择 CommonsMultipartResolver 。
在Servlet的WebApplicationContext的配置文件中配置如下：

```xml
 <!--1.1第一种方式配置 Mutipart解析器 文件上传-->
    <bean id="multipartResolver" class="org.springframework.web.multipart.support.StandardServletMultipartResolver"/>


    <!--2.1第二种方式配置- Mutipart解析器-->
    <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <property name="maxUploadSize" value="10240000"/>
        <property name="defaultEncoding" value="utf-8"/>
        <property name="maxInMemorySize" value="4096"/>
    </bean>
```

使用第一种方式需要在web.xml中的控制器servlet中配置multipart-config

```xml
    <!--前端控制器，注册DispatcherServlet-->
    <servlet>
        <servlet-name>servlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:application-servlet.xml</param-value>
        </init-param>

        <!--1.2第一种方式配置Multipart解析器
         配置multipartconfig-->
        <multipart-config>
            <max-file-size>20848820</max-file-size>
            <max-request-size>418018841</max-request-size>
            <file-size-threshold>1048576</file-size-threshold>
        </multipart-config>
        
    </servlet>
    <servlet-mapping>
        <servlet-name>servlet</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>

```

使用第二种方式需要在pom.xml添加Jakarta Commons Fileupload库

```xml
        <!-- 第二种方式2.2 Spring中配置Multipart解析器
        上传文件库 Jakarta Commons Fileupload-->
        <dependency>
            <groupId>commons-fileupload</groupId>
            <artifactId>commons-fileupload</artifactId>
            <version>1.3.1</version>
        </dependency>
```



#### 3.1.3编写上传文件的控制器

首先我们需要**创建一个视图（页面）**，**提供上传文件的表单**；其次**创建一个能够处理上传文件的控制器**

- 创建一个视图

  ```jsp
  <%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  
  <html>
  <head>
      <title>主页</title>
  </head>
  <body>
  <form method="post" action="/user/upload" enctype="multipart/form-data">
      <label for="head">设置头像</label>
      <input id="head" name="head" type="file" value="上传头像"/>
      <input type="submit" value="提交"/>
  </form>
  <c:if test="${head_data!=null}">
      <img src="${head_data}">
  </c:if>
  </body>
  </html>
  
  ```

  视图页面使用了jsp的JSTL便签库，在pom.xml中添加依赖

  ```xml
          <!--引入JSP的jstl标签库-->
          <dependency>
              <groupId>jstl</groupId>
              <artifactId>jstl</artifactId>
              <version>1.2</version>
          </dependency>
          <dependency>
              <groupId>taglibs</groupId>
              <artifactId>standard</artifactId>
              <version>1.1.2</version>
          </dependency>
  ```

- 创建处理器

  ```java
      /**
       * 
       * 转到上传请求页面
       * @return
       */
      @RequestMapping(value = "/upload", method = {RequestMethod.GET})
      public String upload() {
          return "upload";
      }
  
  
      /**
       * 将表单提交的数据("head")存储在内存中何使用 Multipart解析器对 请求数据进行解析并存储
       * 然后将数据进行Base65编码生成字符串
       * 然后将字符串拼接上data:image/%s;base64,%s 数据类型 + base64文件编码 生成Base64编码的完整格式。
       * 最后响应给请求页面
       *
       * @param multipartFile
       * @return
       */
      @RequestMapping(value = "/upload", method = {RequestMethod.POST})
      public ModelAndView upload(@RequestPart(value = "head") MultipartFile multipartFile) {
          ModelAndView modelAndView = new ModelAndView();
          try {
              byte[] data = multipartFile.getBytes();
              String dataStr = Base64.getEncoder().encodeToString(data);
              //data:image/png;base64,{}
              String extend = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
              String base64Value = String.format("data:image/%s;base64,%s", extend, dataStr);
              modelAndView.addObject("head_data", base64Value);
          } catch (IOException e) {
              e.printStackTrace();
          }
          modelAndView.setViewName("upload");
          return modelAndView;
      }
  ```

### 3.2异常处理

在JavaSE课程中我们学习了异常处理相关知识，现在我们在编写各种控制器的时候，都是基于一个假设，我们的程序都能够正常运行。那么，如果在处理请求的时候，发生了错误，抛出了异常该怎么处理呢？
在Servlet请求的输出是一个Servlet的响应，如果在请求处理的时候，出现了异常，那么它的输出依然会是Servlet 响应，**异常必须以某种方式转换为响应**。

Spring提供了多种方式将**异常转化为响应**：

- 特定的Spring异常将会自动映射为指定的HTTP状态码
- 异常上添加`@ResponseStatus`注解，从而将其映射为某一个HTTP状态码
- 方法上添加`@ExceptionHandler`注解，使其用来处理异常

#### 3.2.1异常映射为状态码

默认情况下，Spring会将自身的一些异常自动转化为**合适的状态码**，如下：

![1556434649162](C:\Users\32183\Desktop\异常映射的状态码.png)

表中的异常一般都是由Spring自身抛出，作为DisoathcerServlet处理过程中或者执行校验是出现问题的结果。

例如：如果DispatcherServlet无法找到合适处理请求的控制器方法，那么将会抛出`NOSuchRequestHandlingMethodException`异常，最终的结果就是404状态码的响应(Not Found)。

尽管内置的映射很有用，但是对于应用程序所抛出的异常，他们确是无能为力了。Spring的灵活性这时候体现出来了，Spring提供了一种机制，能够通过@ResponseStatus注解将异常映射为HTTP状态码。

下面示例展示如何将异常映射为HTTP状态码

- 定义一个控制器的处理方法，如果参数`username`为空`null或者""`抛出自定义异常

  ```java
      /**
       * 将异常映射为HTTP状态码
       *
       * @param username
       * @return
       */
      @RequestMapping(value = "/greeting", method = {RequestMethod.GET})
      public String greeting(@RequestParam("username") String username) {
          if (StringUtils.isEmpty(username)) {
              throw new RequestParamterInvalidException();
          }
          return username;
      }
  ```

- 定义一个异常类，使用`@ResponseStatus`注解将异常映射为HTTP状态码

  ```java
  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "username参数有问题")
  public class RequestParamterInvalidException extends RuntimeException {
  
  }
  ```

  当调用gretting接口时，如果参数·`username`为空，这个时候就会得到404响应，并且在响应的原因上提醒`Parameter Invalid`。

3.2.2异常处理方法

很多场景下，将异常映射为状态码是很简单的方案，并且功能已经足够使用了，但是如果我们想在响应中不仅包括状态码，还要包含所产生的错误信息，如何处理呢?这时我们就需要按照处理请求的方式来处理异常。

- 异常处理控制器方法

  ```java
  
      /**
       * @param username
       * @return
       */
      @RequestMapping(value = "/greeting2", method = {RequestMethod.GET})
      public String greeting2(@RequestParam("username") String username) {
          throw new GreetingException("欢迎" + username + "发生错误");
      }
  
      /**
       * 异常处理控制器方法
       * 不指定转用异常类，则在这个controller里面发生的所有异常均会由这个异常处理控制器处理
       * 返回异常显示器视图
       *
       * @param e
       * @return
       */
      @ExceptionHandler(value = {GreetingException.class})
      public ModelAndView grettingExceptionHandle(GreetingException e) {
          String errorMessage = e.getMessage();
          ModelAndView modelAndView = new ModelAndView();
          modelAndView.addObject("error_message", errorMessage);
          modelAndView.setViewName("greeting_error");
          return modelAndView;
      }
  ```

- 异常显示视图

  ```jsp
  <%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  
  <html>
  <head>
      <title>错误信息页面</title>
  </head>
  <body>
  <c:if test="${error_message!=null}">
      <h1>页面发生异常</h1>
      <h2>异常信息如下:</h2>
      <div>
          <p style="color: #ff3e2a">${error_message}"</p>
      </div>
  </c:if>
  
  </body>
  </html>
  
  ```

- 调用结果

  当调用localhost:8080/exception/gretting2时则会抛出异常，进而返回错误信息页面

  ![1556442178225](D:\婕\JavaEE学习之路\Spring\picture\SpringMVC-异常页面.png)

对于@ExceptionHandler注解标注的方法来说，**它能处理同一个控制器中所有处理器方法所抛出的异常。如果设置其value参数的能够处理那些异常类，其就只对相应的异常来处理。**
这里我们又引伸出一个问题，既然@ExceptionHandler标注的方法能够处理同一个控制器类中所有处理器方法的异常，那么是否有一种能够处理所有控制器中处理器方法所抛出的异常。**控制器通知方法**，正好解决该问题。

#### 3.2.3异常处理方法

如果控制器的特定切面能够运用到**整个应用程序的所有控制器中**，那么这将会便利很多。

Spring3.2引入一个新的解决方案：**控制器通知**，控制器通知(Controller Advice)是任意带有`@ControllerAdvice`注解的类，这个类可以包含一个或者多个如下类型的方法：

- `@ExceptionHandler` 注解标注的方法 
- `@InitBinder` 注解标注的方法 
- `@ModelAndAttribute` 注解标注的方法

在带有有`@ControllerAdvice`注解的类中，以上所述的这些方法会应用到整个应用程序所有控制器中带有 `@RequestMapping`注解的方法上。此外`@ControllerAdvice`注解本身已经标注了`@Component`注解，因此其标注 的类将会自动被组件扫描获取到。

`@ControllerAdvice` 最为实用的一个场景就是将所有`@ExceptionHandler`方法**收集到一个类中**，这样**所有控制器的异常就能在一个地方进行一致的处理**。

```java
@ControllerAdvice
public class AppExecptionHandlerController {

    /**
     * 处理请求异常
     *
     * @return
     */
    @ExceptionHandler(value = RequestParamterInvalidException.class)
    public ModelAndView handlerRequestparameter() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error_message", "无效参数");
        modelAndView.setViewName("error");
        return modelAndView;
    }

    /**
     * 处理上传异常
     *
     * @return
     */
    @ExceptionHandler(value = UploadException.class)
    public ModelAndView handlerUploadException() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error_message", "上传错误");
        modelAndView.setViewName("error");
        return modelAndView;
    }

    /**
     * 全部异常都去处理
     *
     * @return
     */
    @ExceptionHandler()
    public ModelAndView handlerException(Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error_message", e.getMessage());
        modelAndView.setViewName("error");
        return modelAndView;
    }
}

```

定义了控制器通知之后，任意控制器方法抛出`UploadException`或者`RequestParameterIException` 异常，都将有对应的``@ExceptionHandler`标注的方法处理。 

### 3.3静态资源处理

因为DispatcherServlet请求映射配置为`/`,则SpringMVC将不获Web容器的所有请求，包括静态资源的请求，Spring MVC会将他们当作一个普通的请求处理，因找不到对应的处理器而导致错误404。

如何让Spring框架能够捕获的所有URL请求，同时又将静态资源的请求转由Web容器（容器的Web服务能力）处理，是将DisparcherServlet的请求映射配置为`/`的前提。Spring提供了**两种静态资源的处理解决方案。**

#### 3.3.1采用默认Servlet 由web容器进行处理

在 **application-servlet.xml** 中配置 **`<mvc:default-servlet-handler default-servlet-name="default"/> `**后，会在Spring MVC 容器中定义一个 org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler ，它将充当一个检查员的角色，对进入DispatcherServlet的URL进行筛查。**如果发现是静态资源的请求，就将该请求转由Web应用服务器默认的Servlet处理**；如果不是静态资源的请求，则由DispatcherServlet继续处理。

```xml
    <!--3.3.1采用默认Servlet处理静态资源 由Web容器处理-->
    <mvc:default-servlet-handler default-servlet-name="default"/>
```

注意：一般Web应用服务器（比如：Tomcat， Jetty, Glassﬁsh，Resin，WebLogic）默认的Servlet名称都是 default ,因此 DefaultServletHttpRequestHandler 可以找到它。如果所使用的Web应用服务器的默认Servlet 名称不是 default ,则需要通过 default-servlet-name 属性显式指定。

#### 3.3.2采用Spring的Resources

**`<mvc:default-servlet-handler/>`** 将静态资源的处理经由Spring MVC框架交回Web应用服务器。而**`\<mvc:resources> `**更进一步，由Spring MVC框架自己处理静态资源，并添加一些有用的附件功能。
首先，**允许静态资源放置到任何地方**，比如：WEB-INF目录，类路径，静态文件打包到Jar中。**通过location属性指定静态资源的位置**，由于location属性是Resource类型，因此可以使用如： classpath: 等资源前缀指定资源位 置。传统的Web容器的静态资源只能放到项目的根路径下， **`<mvc:resources/>`** 则完全打破了这个限制。
其次，**可以设置缓存周期，这样能够充分利用浏览器的缓存**。

在接收到静态资源的获取请求时，会检查请求头的 Last-Modiﬁed值，**如果静态资源没有发生变化，则直接返回303的响应状态码**，指示客户端使用浏览器缓存的数据，**则可以充分节省宽带，提高程序的性能**。

- 配置，请求assets前缀的静态资源，由webapp目录下的assets目录中资源响应

```xml
    <!--3.3.2采用Spring的Resources  处理静态资源-->
    <!--请求映射的前缀为                mapping = /assets的URL为静态资源的访问
        可以是多个路径，用逗号隔开       location = /assets/ 的地址下去找：/为Web存放的目录
                                        cache-period = 缓存周期10分钟 单位秒  -->
    <mvc:resources mapping="/assets/**" location="/assets/" cache-period="600"/>
```

- 目录结构

  ![1556456042001](D:\婕\JavaEE学习之路\Spring\picture\目录节结构.png)

- 请求地址URL

  **`http://localhost:8080/assets/image/spring.png`** 

真正的企业级别的网站，他们的图片不是存储在本服务器中的，而是专门存储在某个图片服务器上去的，而且是一个全路径。

京东网站图片是在360buy的路径下，京东前名360buy

### 3.4拦截器

在我们JavaWeb中有Filter过滤器，SpringMVC中为我们提供了拦截器，当收到请求时，DispatcherServlet请求交给处理器映射(HandlerMapping),让我们找出对应请求HandlerExecutionChain对象。

HandlerExecutionChain顾名思义是**一个执行链**，**它包含一个处理该请求的处理器（Handler），同时包含若干个对该请求实施拦截的拦截器（HandlerInterceptor）**。当HandlerMapping返回HandlerExecutionChain后， DispatcherServlet将请求交给定义在HandlerExecutionChain中的拦截器和处理器一并处理。如下图第三步骤

![1556038080847](D:\婕\JavaEE学习之路\Spring\picture\SpringMVC核心类的请求流程.png)



![1556457099846](D:\婕\JavaEE学习之路\Spring\picture\SpringMVC拦截器.png)

HandlerExecutionChain是负责处理请求并返回ModelAndView的处理执行链，**请求在被Handler执行的前后链中装配的HandlerInterceptor会实施拦截操作。** 

*如下:自定义一个拦截器，设置响应编码为UTF-8*

```java
package com.github.soyanga.springmvc.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: springMVC-case-practice
 * @Description: 自定义一个拦截器，设置响应编码为UTF-8
 * @Author: SOYANGA
 * @Create: 2019-04-28 21:21
 * @Version 1.0
 */
public class HttpEncodinginterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        response.setCharacterEncoding("UTF-8");
        System.out.println("HttpEncodinginterceptor preHandle");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

    }
}

```

每个拦截器可以配置一个拦截路径，可以配置多个拦截器。

**在application-servlet中配置拦截器**

```xml
    <!--3.4配置自定义拦截器-->
    <mvc:interceptors>
        <mvc:interceptor>
        <!--拦截所有地址-->
        <mvc:mapping path="/**"/>
        <!--引入拦截器的Bean-->
        <bean class="com.github.soyanga.springmvc.interceptor.HttpEncodinginterceptor"/>
        </mvc:interceptor>

        <mvc:interceptor>
            <!--拦截所有地址-->
            <mvc:mapping path="/**"/>
            <!--排除登陆和退出,主页，注册,静态图片-->
            <!--<mvc:exclude-mapping path="/"/>无法排除拦截/ -->
            <mvc:exclude-mapping path="/user/login"/>
            <mvc:exclude-mapping path="/assets/**"/>
            <mvc:exclude-mapping path="/user/logout"/>
            <mvc:exclude-mapping path="/index"/>
            <!--引入拦截器的Bean-->
            <bean class="com.github.soyanga.springmvc.interceptor.AuthInterceptor"/>
        </mvc:interceptor>
    </mvc:interceptors>
```



自定义一个拦截器，可以检测用户是否登陆，限制未登录用户的使用权限 拦截器配置如上

```java
package com.github.soyanga.springmvc.interceptor;

import com.github.soyanga.springmvc.control.UserController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @program: springMVC-case-practice
 * @Description: 自定义一个拦截器，检测是否登陆，限制未登录者的访问权限 HandlerInterceptorAdapter空实现了HandlerInterceptor接口 我们就可以选择需要的方法去编写
 * @Author: SOYANGA
 * @Create: 2019-04-28 21:30
 * @Version 1.0
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //可以通过自己的方式去拦截
        String uri = request.getRequestURI();
        if ("".equals(uri) || "/".equals(uri)) {
            System.out.println("当前访问的是欢迎页");
            return true;
        }
        HttpSession session = request.getSession();
        Object user = session.getAttribute(UserController.CURRENT_USER);
        System.out.println(user);
        if (user == null) {
            System.out.println("AuthInterceptor false");
            return false;
        } else {
            System.out.println("AuthInterceptor true");
            return true;
        }
    }
}

```



## 总结

| 知识块        | 知识点                                     |
| ------------- | ------------------------------------------ |
| SpringMVC注解 | 1.控制器2.请求映射3.控制器方法             |
| SpringMVC特性 | 1.上传文件2.异常处理3.静态资源处理4.拦截器 |



