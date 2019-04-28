# SpringMVC实战—请求映射

[TOC]

**重点**

> 1. 掌握SpringMVC注解
> 2. 掌握SpringMVC的控制器编写
> 3. 掌握SpringMVC的常用特性

## 1.SpringMVC注解

### 1.1@Controller

@Controller注解标注是一个类是Web控制器，等价@Component注解等价，只不过Web层使用，其便于区分类的作用。经常结合配置@RequestMapping注解使用。

```java
//定义了一个控制器 
@Controller @RequestMapping(value = "/account") 
public class AccountController {     
}
```

### 1.2@RequestMapping

是Spring Web应用程序中最常用被用到的注解之一。这个注解会将**HTTP请求**映射到**MVC**和**REST**控制器的处理方法上。

在对SpringMVC进行配置的时候，需要**指定请求与处理方法**之间的映射关系，这时候就需要使用**@RequsetMapping**注解。该注解可以在**控制器类级别和其他方法级别**上使用。

在**类级别上的注解**会将一个**特定请求或者请求模式映射**到一个**控制器**之上。之后你可以**另外添加方法级别的注解**来进一步**指定处理方法**的**特定映射关系**。

```java
//先不用关注RestController，REST课程会单独讲解 
@RestController 
@RequestMapping("/home") 
public class IndexController {    
	@RequestMapping("/")    
	String get() {        
	//mapped to hostname:port/home/        
	return "Hello from get";    
	}    
	@RequestMapping("/index")    
	String index() {        
	//mapped to hostname:port/home/index/        r
    eturn "Hello from index";    
	} 
}
```

#### 1.2.1@RequestMapping处理多个URI

可以将**多个请求映射到同一个方法上**，只需要添加一个带有请求路径值得列表在@RequestMapping注解上即可。

```java
@RestController @RequestMapping("/home") 
public class IndexController {
    @RequestMapping(value = {        
    	"",        
    	"/page",        
    	"page*",        
    	"view/*"   
    })    
    String indexMultipleMapping() {        
    return "Hello from index multiple mapping.";    
    } 
}
```

上面这段代码中可以看到得，@RequsetMapping支持通配符以及ANT风格的路径。如下的这些URL都会有indexMultipleMapping()来处理:

- localhost:8080/home 
- localhost:8080/home/ 
- localhost:8080/home/page 
- localhost:8080/home/pageabc 
- localhost:8080/home/view/ 
- localhost:8080/home/view/*

#### 1.2.2@RequestMapping结合@RequestParam

@RequsetParam注解配置@RequsetMapping一起使用，可以将**请求的参数同处理方法的参数绑定在一起**。==对请求进行跟严格的限定==

@RequestParam注解参数:

- required：定义了参数值是否必须 传，默认true(必须)

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

required被指定为false,所以getName()处理方法对于如下两个URL都会进行处理：

对于personName这个参数是可有可无的

`/home/name?person=zahngsan`，`/home/name` 

- name/value：定义了参数名称
- defaultValue：定义了用以给参数值为空的请求参数提供了一个默认值

```java
@RestController   
@RequestMapping("/home")   
public class IndexController {
    
 @RequestMapping(value = "/name")      
 String getName(@RequestParam(value = "person", defaultValue = "John") String personName) {          
	 return "Required element of request param";      
 }   
}
```

在这段代码中，如果person请求参数为空，那么getName()处理方法就会接收John这个默认值作为其参数值。

#### 1.2.3@RequestMapping处理HTTP的各种方法

SpringMVC的@RequestMapping注解能够处理HTTP请求方法有:`GET`,`HEAD`,`POST,PUT,PATCH,DELETE,OPTIONS,TRACE`。

为了能够**将一个请求映射到一个特定的HTTP方法**，你需要在@RequestMapping中使用**method**参数声明HTTP请求所使用的方法类型

```java
@RestController @RequestMapping("/home") 
public class IndexController {       
	@RequestMapping(method = RequestMethod.GET)    
	String get() {        
		return "Hello from get";    
	}        
	@RequestMapping(method = RequestMethod.DELETE)    
	String delete() {        
		return "Hello from delete";    
	}
	
    @RequestMapping(method = RequestMethod.POST)    
    String post() {        
    	return "Hello from post";    
    }
 
    @RequestMapping(method = RequestMethod.PUT)    
    String put() {       
    	return "Hello from put";    
    }
 
    @RequestMapping(method = RequestMethod.PATCH)    
    String patch() {        
    	return "Hello from patch";    
    }
}
```

上述代码中所有处理方法都会处理从同一个URL(/home)请求，但是根据指定HTTP方法是什么来决定具体使用那个方法来处理。

#### 1.2.4@RequestMapping的组合注解（方法级别的注解）

从Spring4.3开始引入了方法级的注解变体，也称作@RequestMapping的组合注解(快捷方式)。**组合注解可以更好的表达注解方法的语义**。它们扮演的劫色就是针对@RequestMapping的封装，成为定义端点的标准方法。

**方法级别的注解**变体有如下几个:

- @GetMapping  ==  @RequestMapping(method = {RequestMethod.GET})
- @PostMapping == @RequestMapping(method = {RequestMethod.POST})
- @PutMapping == @RequestMapping(method = {RequestMethod.PUT})
- @DeleteMapping == @RequestMapping(method = {RequestMethod.DELETE})
- @PatchMapping == @RequestMapping(method = {RequestMethod.PATCH})



#### 1.2.5一个完整的例子

- 定义一个用户默认登陆的页面控制器

  ```java
  /**
   * @program: springMVC-case
   * @Description: 默认控制器(欢迎页面)
   * @Author: SOYANGA
   * @Create: 2019-04-25 15:31
   * @Version 1.0
   */
  @Controller
  @RequestMapping
  public class IndexController {
  
      @RequestMapping(value = {"", "/index"}, method = {RequestMethod.GET})
      public ModelAndView hello() {
          ModelAndView modelAndView = new ModelAndView();
          modelAndView.setViewName("welcome");  //WEB_INF/Views/welcome.jsp
          return modelAndView;
      }
  }
  ```

- 定义用户登陆退出的控制器

  ```java
  /**
   * @program: springMVC-case-practice
   * @Description: 用户控制器
   * @Author: SOYANGA
   * @Create: 2019-04-25 20:19
   * @Version 1.0
   */
  @Controller
  @RequestMapping(value = "/user")
  public class UserController {
  
      private static final String CURRENT_USER = "current_user";
  
      @RequestMapping(value = "/login", method = {RequestMethod.POST})
      //组合注解等价上面的注解
      //@PostMapping(value = "/login", headers = {"Cache-Control"})
      //hraders：请求里面必须要有这个"Cache-Control"这个请求头
      //localhost:8080/user/login
      //引入@RequestParam注解，我们更为严格的控制请求，
      // 如果username或者password没有这两个参数则就不予以处理请求 required默认是true
      //没有添加defaultValue参数前：如果没有remind复选框这个参数，由于required设置的是false,所以没有这个参数请求照样执行
      //添加defaultValue后，如果没有remind复选框这个参数则，remind默认是0（不记住密码）
      public ModelAndView loginPost(
              @RequestParam(value = "username") String username,
              @RequestParam(value = "password") String password,
              @RequestParam(value = "remind", required = false, defaultValue = "0") String[] remind,
              HttpServletRequest request) {
          System.out.println("login username=" + username + ", password=" + password);
  
          //是否记住密码
          //String[] remind = request.getParameterValues("remind");
          System.out.println(Arrays.toString(remind));
  
          HttpSession session = request.getSession();
          session.setAttribute(CURRENT_USER, username);
  
          ModelAndView modelAndView = new ModelAndView();
          try {
              modelAndView.addObject("username", new String(username.getBytes("ISO-8859-1"), "UTF-8"));
              modelAndView.addObject("password", new String(password.getBytes("ISO-8859-1"), "UTF-8"));
          } catch (UnsupportedEncodingException e) {
              e.printStackTrace();
          }
          modelAndView.setViewName("home");  //WEB_INF/Views/home.jsp
          return modelAndView;
      }
  
      //@RequestMapping(value = "/login", method = {RequestMethod.GET})
      //组合注解等价上面的注解
      @GetMapping(value = "/login")
      // localhost:8080/user/login
      public ModelAndView loginGet() {
          ModelAndView modelAndView = new ModelAndView();
          modelAndView.setViewName("login");  //WEB_INF/Views/login.jsp
          return modelAndView;
      }
  
      //@RequestMapping(value = "/logout", method = {RequestMethod.GET})
      //组合注解等价上面的注解
      @GetMapping(value = "/logout")
      public ModelAndView logout(HttpServletRequest request) {
  
          HttpSession session = request.getSession();
          session.removeAttribute(CURRENT_USER);
  
          ModelAndView modelAndView = new ModelAndView();
          modelAndView.setViewName("welcome");  //WEB_INF/Views/welcome.jsp
          return modelAndView;
      }
  }
  ```

大致登陆流程

欢迎页面http://127.0.0.1:8080-->点击登陆跳转至登陆页面()Post方法–>http://127.0.0.1:8080/user/login—>填写用户名称密码+是否记住密码点击登陆–->利用@RequestParam后台获取到(是否记住密码的复选框值)且验证用户名，密码是否填写，默认不记住密码，确认无误--->将获取参数跳到home.jsp处理（URL不变)只是改变了页面的展示（view层）—>点击

#### 1.2.6@RequestMapping来处理生产和消费者

使用@RequestMapping注解得到`produces`和`consumes`这两个参数来**缩小请求映射的类型范围**。

根据请求的媒体类型来**产生对象**，要用到@RequestMapping的`produces`参数结合`@ResponseBody`注解。

根据`@RequestMapping`的`consumes`参数结合`@RequestBody`注解用请求媒体类型来**消费对象**。

添加JSON依赖

```xml
        <!--引入json的库-->
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.8.2</version>
        </dependency>
```

```java
@RestController
@RequestMapping(value = "/demo1")
public class Demo1Controller {


    /**
     * 生产者 希望服务器响应为 json而格式
     * 期望返回一个JSON格式，所以produces中就是限定返回JSON格式
     *
     * @return JSON字符串
     */
    @RequestMapping(value = "/prod", produces = {
            "application/json"
    })
    @ResponseBody
    public String producer() {
        Map<String, String> map = new HashMap();
        map.put("current_time", new Date().toString());
        map.put("username", "jack");
        return new Gson().toJson(map);
    }


    /**
     * 生产者 希望服务器响应为 json而格式
     * 期望返回一个JSON格式，所以produces中就是限定返回JSON格式 当前返回的是Map格式但是
     * 与我们期望不符合，假如项目中没有添加JSON依赖，则浏览器访问时406 not acception
     * 反之有JSON依赖，将map转换为JSON后进行响应
     *
     * @return JSON字符串
     */
    @RequestMapping(value = "/prod2", produces = {
            "application/json"
    })
    @ResponseBody
    public Map producer2() {
        Map<String, String> map = new HashMap();
        map.put("current_time", new Date().toString());
        map.put("username", "jack");
        return map;
    }


    /**
     * 生产者 希望服务器响应为 json而格式
     * 期望返回一个JSON格式，所以produces中就是限定返回JSON格式 当前返回的是user格式但是
     * 与我们期望不符合，假如项目中没有添加JSON依赖，则浏览器访问时406 not acception
     * 反之有JSON依赖，将user转换为JSON后进行响应
     *
     * @return JSON字符串
     */
    @RequestMapping(value = "/prod3", produces = {
            "application/json"
    })
    @ResponseBody
    public User producer3() {
        User user = new User();
        user.setAge(18);
        user.setName("tom");
        user.setEmail("tom@163.com");
        return user;
    }


    /**
     * consumes消费者：希望客户端返回的是一个json格式
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/cons1", consumes = {
            "application/json"
    }, method = RequestMethod.POST)
    public User consumer1(@RequestBody User user) {
        user.setName("welcom " + user.getName());
        return user;
    }


    /**
     * 静态内部User类
     */
    @Data
    public static class User {
        private String name;
        private Integer age;
        private String email;
    }
}

```

#### 1.2.7@RequestMapping来处理消息头

@RequestMapping注解提供了一个`header`参数来**根据请求头中的消息头内容缩小请求映射范围**。在header参数上使用`myheader=myValue`这种`key-value`格式指定值。

```java
@RestController
@RequestMapping(value = "/demo2", method = RequestMethod.GET)
public class Demo2Controller {

    /**
     * 用Http头信息进一步限制请求的范围，如果投中不含有Content-Type=text/html这个头信息则
     * 会报415  HTTP Status 415 – Unsupported Media Type 不支持参数错误。反之则请求正常处理
     *
     * @return 返回响应内容
     */
    @RequestMapping(value = "/header1", headers = {
            "Content-Type=text/html"
    })
    public String textHtml() {
        return "<h1>Hello world</h1>";
    }
}   
```

就只会处理到/demo2head1 并且 content-type header 被指定为 `text/html` 这个值的请求。

header参数的类型是字符串数组，所以可以指定多个消息头:

```java
/**
     * 用Http头信息进一步限制请求的范围，如果投中不含有Content-Type=text/html或者Content-Type=text/plain这个头信息则
     * 会报415  HTTP Status 415 – Unsupported Media Type 不支持参数错误。反之则请求正常处理
     *
     * @return 返回响应内容
     */
    @RequestMapping(value = "/header2", headers = {
            "Content-Type=text/html",
            "Content-Type=text/plain"

    })
    public String textPlainHtml() {
        return "<h1>See world again</h1>";
    }
```

上面示例代码的textPlainHtml()方法就能给够同时接受 `text/plain` 和 `text/html` 的请求了。 

#### 1.2.8@RequestMapping来处理请求参数

@RequestMapping指定的`params`参数可以进一步缩小请求映射的定位范围。使用param参数，可以让多个处理到同一个URL的请求，而这些请求的参数是不一样的。

在params参数上使用`myParam=myvalue`这种`key-value`格式指定值，也可以是使用操作符比如：

`myparam!=myValue`

```java
package com.github.soyanga.springmvc.control;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: springMVC-case-practice
 * @Description: @RequestMapping来处理请求参数params
 * @Author: SOYANGA
 * @Create: 2019-04-26 20:23
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/demo3")
public class Demo3Controller {

    /**
     * 请求param中必须要有"group=10"还有"color=XXX且id!=1否则400 Bad Request
     * eg：http://127.0.0.1:8080/demo3/param1?color=red&group=10&id=2
     * 或者没有id这个参数即可: http://127.0.0.1:8080/demo3/param1?color=red&group=10
     *
     * @param color
     * @return
     */
    @RequestMapping(value = "/param1", params = {
            "group=10", "id!=1"
    }, method = RequestMethod.GET)
    public String param1(@RequestParam("color") String color) {
        return color;
    }
}
```

#### 1.2.9@RequestMapping 处理动态(模板) URI

@RequestMapping注解可以同@PathVaraiable注解一起使用，**用来处理动态URI**,URI的值可以作为**控制器中处理方法的参数**，你也可以使用**正则表达式来只处理可以匹配到正则表达式的动态URI**。

```java
@RestController
@RequestMapping(value = "/demo4")
public class Demo4Controller {

    /**
     * 将请求地址中的某个值作为参数属性获取下来
     * eg:http://127.0.0.1:8080/demo4//fetch/1
     * 则获取下来的id = 1
     * @param id
     * @return
     */
    @RequestMapping(value = "/fetch/{id}", method = RequestMethod.GET)
    public String uriVariable(@PathVariable(value = "id") String id) {
        return "fetch id = " + id;
    }

    /**
     * 将请求地址中的某个值作为参数属性获取下来 -可用正则表达式再次进行限定
     * eg:http://127.0.0.1:8080/demo4//fetch/SOYANGA/blog/111
     * 获取下来 则name = SOYANGA  id = 111 
     * 其中id必须是数字否则不匹配正则表达式 则会报404 not found错误
     * @param name
     * @param id
     * @return
     */
    @RequestMapping(value = "/fetch/{name}/blog/{id:[0-9]+}", method = RequestMethod.GET)
    public String uriVariable2(
            @PathVariable(value = "name") String name,
            @PathVariable(value = "id") String id) {
        return "fetch name = " + name + "; blog id = " + id;
    }
}

```

*@PathVariable同@RequestParam的运行方式不同。你使用**@PathVariable是为了从URI模版中获取参数值**，而 **@RequestParam是从URI查询参数中获取参数值**。* 

#### 1.2.10@RequestMapping默认的处理方法

在控制器类中，你可以有一个默认的处理方法，它可以由一个向默认URI发起请求时被执行。

```java
@RestController
@RequestMapping(value = "/demo5")
public class Demo5Controller {

    @RequestMapping
    public String index() {
        return "default method";
    }
}
```

上述代码中，向 /demo5 发起的一个请求将会由index()来处理，因为@RequestMapping注解并没有指定任何值。 

#### 1.2.11@RequestMapping总结

在本章节中所看到的@RequestMapping注解是非常灵活的。可以使用该注解配置Spring MVC 来处理大量的场景 用例。它可以被用来在 **Spring MVC中配置传统的网页请求** ，也可以是**REST风格的Web服务** 。

在目前**前后端分离**的开发模式非常流行的大环境下，Web应用的界面交互部分后端服务不再参与，界面与后端主要以数据交互的方式进行，当下**Restful风格的服务请求地址或者接口成为主流**，后续学习，项目使用都将采用 Restful风格进行接口开发。 



