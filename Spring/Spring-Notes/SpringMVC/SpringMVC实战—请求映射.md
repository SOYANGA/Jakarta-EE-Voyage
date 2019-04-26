# SpringMVC实战

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

- required：定义了参数值是否必须传，默认true(必须)

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

- @GetMapping 
- @PostMapping 
- @PutMapping 
- @DeleteMapping
-  @PatchMapping

```

```

