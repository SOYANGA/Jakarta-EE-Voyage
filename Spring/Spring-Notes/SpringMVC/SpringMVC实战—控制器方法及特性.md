# SpringMVC实战—控制器方法及特性

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
     * ControllerMethodController.User(id=1, name=SOYANGA, address=ControllerMethodController.Address(name=XIAN, code=7100))
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

  