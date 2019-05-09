# Spring Boot基础

**重点**

> 1. 掌握Spring Boot核心注解
> 2. 掌握Spring Boot配置

## 1.核心注解

- **@SpringBootApplication**

在入口类上标注该注解，让SpringBoot自动给程序进行必要的配置，这个配置等同于:

**`@Configuration,@EnableAutoConfiguration和@ComponentScan`**三个配置。

- **@EableAutoConfiguration**

Spirng Boot自动配置(auto-configuration) :尝试根据你添加的jar依赖自动配置Spring应用。例如:如果classpath下存在H2库，并且没有手动配置任何数据库连接beans,那么将自动配置一个内存型(in-memory)连接库。

可以将@EnableAutoConfiguration或者@SpringBootApplication注解添加到一个@Configuration类上来选择自动配置。如果发现应用了你不想要的特定自动配置类，你可以使用@EnableAutoConfiguration注解的排除属性来禁用它们。(一般不会禁用掉自动配置)

```java
//添加自动扫描包，com.github.soyanga.springBootDemoa.control
//禁止DataSourceAutoConfiguration自动注入
@SpringBootApplication(
        scanBasePackages = {
                "com.github.soyanga.springBootDemo1.control"
        },
        exclude = {
                DataSourceAutoConfiguration.class
        }
)
```

- **@Configuration**

相当于传统的xml配置文件，如果有些第三方库需要用到xml文件，建议仍然通过@Configuration类作为项目的配置主类--可以使用@ImportResource注解加载xml配置文件。

```java
package com.github.soyanga.springBootBasic.config;

import com.github.soyanga.springBootBasic.component.ExampleBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Properties;

/**
 * @program: springBoot-Basics
 * @Description: 配置@Configuration的使用 相当于xml 配置类 配置Bean
 * @Author: SOYANGA
 * @Create: 2019-05-02 16:49
 * @Version 1.0
 */
@Configuration
public class AppConfiguration {

    @Bean(value = "hello")
    public String helloString() {
        return "hello";
    }


    @Bean(value = "welcome")
    public String welcomeString() {
        return "welcome";
    }


    @Bean
    public Properties properties() {
        return new Properties();
    }

    @Bean(initMethod = "init", destroyMethod = "destroy")
    @Scope(scopeName = "prototype") //非单例
    public ExampleBean exampleBean() {
        ExampleBean exampleBean = new ExampleBean();
        exampleBean.setName("jack");
        return exampleBean;
    }
}

```

- @**PropertySource**

  如果需要自定义的属性文件需要加载，可以使用该注解进行注入，并用@Value配合使用。

  ```java
  /**
   * @program: springBoot-Basics
   * @Description: @PropertySource属性源配置信息 属性源
   * @Author: SOYANGA
   * @Create: 2019-05-02 17:19
   * @Version 1.0
   */
  @PropertySource(value = {"classpath:appConfig.properties"})
  @Data
  @Component
  public class AppConfig {
  
      @Value("${app.config.host}")
      private String host;
  
      @Value("${app.config.port}")
      private Integer port;
  }
  
  ```

```properties
#src\main\resources\appConfig.properties
app.config.host=localhost
app.config.port=3306
```

- **@ImportResource** 

用来加载xml配置文件 将原先xml配置中的Bean导入到项目中来。

```java
@ImportResource(locations = {"classpath:application-bean.xml"})
public class SpringBootDemo1Application {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SpringBootDemo1Application.class, args);

        ExampleBean2 exampleBean2 = context.getBean(ExampleBean2.class);
        System.out.println(exampleBean2);
    }
}
```

- **@Bean**用@Bean标注方法等价于XML中配置的bean。

```java
    @Bean(initMethod = "init", destroyMethod = "destroy")
    @Scope(scopeName = "prototype") //非单例
    public ExampleBean exampleBean() {
        ExampleBean exampleBean = new ExampleBean();
        exampleBean.setName("jack");
        return exampleBean;
    }
```

- **Enviroment**

`org.springframework.core.env.Environment` 环境类，Spring3.1以后开始引入。比如JDK环境，Servlet环境，Spring环境等等；每个环境都有自己的配置数据，如``System.getProperties()`、`System.getenv()``等可以拿到JDK环境数据；`ServletContext.getInitParameter()`可以拿到Servlet环境配置数据等等；**也就是说Spring抽象了一 个Environment来表示环境配置。** 
在Spring Boot中使用直接用**@Resource或@Autowired**注入，即可获得系统配置文件application.properties/yml 的属性值，如果是自定义的配置文件，**则需要预先通过@PropertySource等其他注解注入后，才能获取。获取通过 getProperty()方法获取。**

```java
    //自动注入enviroment Spring自带的bean --->
    @Autowired
    private Environment environment;
    
    /**
     * 获取enviorment属性 从中获取系统的环境信息
     *
     * @return
     */
    @RequestMapping(value = "/environment", method = {RequestMethod.GET})
    public Map<String, Object> environment() {
        Map<String, Object> map = new HashMap<>();
        map.put("app.config.host", environment.getProperty("app.config.host"));
        map.put("java.home", environment.getProperty("java.home"));
        map.put("usr.dir", environment.getProperty("usr.dir"));
        return map;
    }
```

## 2.基本配置

### 2.1入口类

良好的习惯，通常Spring Boot项目有一个名为**Application**的入口类，入口类里有一个main方法，该main方法其实就一个标准的java应用入口方法。在main方法中使用**SpringApplication.run(…)方法来启动Spring Boot应用项目。**

```java
/**
 * @program: springboot-case
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-05-01 17:31
 * @Version 1.0
 */
@SpringBootApplication
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }
}
```

@SpringBootApplication是一组注解，其中@EnableAutoConfiguration注解Spring Boot根据路径上的jar包依赖为当前项目的进行配置。例如:添加Spring-boot-starter-web依赖，就会自动添加Tomcat和SpringMVC依赖，同时Spring Boot 会对Tomcat和Spring MVC进行自动配置。

Spring Boot会自动扫描@SpringBootApplication所在类的同级包(例如：com.github.boot)以及下级包里的Bean。

**建议入口类放置在项目根包下，即项目最外层包。**

### 2.2关闭特定的自动配置

有时候需要关闭特定的自动配置，这时需要使用`@SpringBootApplication`注解的**exclude**参数

```java
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
```

在如下图的选项中 寻找要关闭的类中的以**Configuration**结尾的类，那就是自动注入的注解

仅仅就是关闭自动装配，可以自己添加Bean，手动自动装配即可。

如果一个Bean自己实现了一个，第三方也提供了Bean。SpringBoot默认使用自己写的

这是因为`@ConditionalOnMissingBean`注解，但是不一定每个类都会实现这个注解，所以我们需要在自己写的类上添加`@Primary`注解就会让注解成为主要的Bean，就默认会使用添加`@Primary`的Bean。

![1556809775728](D:\婕\JavaEE学习之路\Spring\picture\取消自动配置注解.png)

### 2.3定制Banner

#### 2.3.1修改Banner

- 在Spring启动的时候会有一个默认的图案，由Spring以及版本信息组成

```js
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.1.4.RELEASE)
```

- 在`src/main/resource`下新建一个banner.txt
- 通过[Test to ASCII Art Generator](http://patorjk.com/software/taag/) 网站生成字符，比如： `bit Tech for Java`将生成的字符复制到banner.txt文件
- 再次启动程序，欢迎团变成如下所示

```java
|  _ \(_) |   |__   __|      | |      / _|                | |                  
| |_) |_| |_     | | ___  ___| |__   | |_ ___  _ __       | | __ ___   ____ _  
|  _ <| | __|    | |/ _ \/ __| '_ \  |  _/ _ \| '__|  _   | |/ _` \ \ / / _` | 
| |_) | | |_     | |  __/ (__| | | | | || (_) | |    | |__| | (_| |\ V / (_| | 
|____/|_|\__|    |_|\___|\___|_| |_| |_| \___/|_|     \____/ \__,_| \_/ \__,_|

${application.name}
```

#### 2.3.2关闭Banner

- 在main方法中构造Spring Application对象时修改

```
SpringApplication application = new SpringApplication(SpringBootDemo1Application.class);
application.setBannerMode(Banner.Mode.OFF); //取消Banner
application.run(args);
```

- 使用Fluent AP修改 --—->链式编程

```
        //Fluent API 构建者模式
        new SpringApplicationBuilder(SpringBootDemo1Application.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
```

> 扩展 : 构建者模式（Builder Pattern ），IDEA安装Builder Generator插件可以自动生成一个类的构建者模式类。 

### 2.4Spring Boot的配置

Spring Boot使用一个全局的配置文件`application.properties`或者`application.yml`**放置在`src/main/resource`目录或者类路径下`/config`下**

SpirngBoot不仅支持常规的`properties`配置文件，还支持yaml语言的配置文件。yaml是以数据为中心的语言，在配置数据的时候具有面向对象的特征。

如下：示例，将Tomcat默认端口的8080改为8090，并且将默认的访问路径`/`修改为`/springboot`。

- 在application.properties中修改

```properties
#修改端口号
server.port=8080
#下修改context-path路径
server.servlet.context-path=/springboot
```

- 在appliaction.yaml中修改

```yaml
server:      
	port: 8090      
	servlet:
		ycontext-path: /springboot
```

上面两种方式都能满足要求，但是@PropertySource注解是不支持加载yaml文件，另外yaml文件的格式要求比较严格，加载时还需要解析，所以在日常开发中，比较习惯用properties文件来配置，所以推荐使用properties进行配置。

### 2.5.starter pom

Spirng Boot提供了**简化企业级开发绝大多数场景的starter pom**，只要使用了应用场景需要的starter pom,相关的参数(属性)配置将会消除，就可以得到SpringBoot为我们提供了自动配置的Bean。

2.5.1依赖版本列表

- 查看[离线手册](https://docs.spring.io/spring-boot/docs/2.1.4.RELEASE/reference/pdf/spring-boot-reference.pdf)  的Appendix F.Dependency version章节
- 查看[在线手册中依赖版本章节](https://docs.spring.io/spring-boot/docs/2.1.4.RELEASE/reference/htmlsingle/#appendix-dependency-versions) 

#### 2.5.2官方 starter pom4

> 下面图中部分，spring-boot-starter-*的名称可能会因为版本差异由所不同，需要以实际版本的帮助手册为准

![1556862094638](D:\婕\JavaEE学习之路\Spring\picture\官方starter pom4_1.png)

![1556862129166](D:\婕\JavaEE学习之路\Spring\picture\官方starter pom4_2.png)

![1556862161707](D:\婕\JavaEE学习之路\Spring\picture\官方starter pom4_3.png)

#### 2.5.3第三方starter pom

![1556862255416](D:\婕\JavaEE学习之路\Spring\picture\第三方starter pom)

- [阿里巴巴Druid数据源 druid-spring-boot-starter](https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter)
- [Mybatis在Spring Boot中应用 mybatis-spring-boot-starter](www.mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/) 

### 2.6使用XML配置

Spring Boot提倡零配置，即无XML配置。但是在实际项目中，可能由有一些特殊要求必须使用XML配置，这时候可以通过Spring提供的@ImportResource来加载XML配置。

```java
@ImportResource({"classpath:context-bean.xml"})
```

## 3.外部配置

Spring Boot允许使用properties文件，yaml文件或者命令行参数作为外部配置。

### 3.1命令行参数配置

Spring Boot可以是基于jar包运行，打成jar包的程序可以直接通过下面命令行运行。

```java
java -jar xxx.jar
```

可以通过命令行参数修改Tomcat的端口号：

```java
java -jar xxx.jar --server.port=8081
```

通过Maven打包之后，SpringBoot的配置文件被打包的jar包中，要修改配置除了通过上面的命令行参数的方式进行修改之外，还可以通过在jar的包的同级目录下的conf目录中创建application.properties或者application.yml来进行参数的覆盖。

```properties
springboot-case-1.0.0.war 
#外置的配置文件 
application.properties
```

### 3.2属性配置

#### 3.2.1常规属性配置

在常规的Spring环境下，注入properties文件里的值的方式，**通过@PropertySource指明properties文件的位置，然后通过@Value注入值。**

在Spring Boot里，只需要在applicat.properties定义属性，直接使用@Value注入即可

- 在application.properties增加属性:

  ```properties
  book.author=secondriver 
  book.name=Hello Spring Boot 
  ```

- 修改入口类

  ```java
  @SpringBootApplication 
  @RestController 
  public class ExampleApplication {    
  	@Value("${book.author}")    
  	private String bookAuthor;    
  	@Value("${book.name}")    
  	private String bookName;    
  	@RequestMapping("/")    
  	public String index() {        
  		return "Book Author : " + bookAuthor + " Book Name :" + bookName;    
  	}    
  	public static void main(String[] args){
  		SpringApplication.run(ExampleApplication.class, args);    
  	} 
  }
  ```

- 运行服务，访问http://localhost:8080/接口

  



#### 3.2.2类型安全的配置

上面示例中使用@Value注入每个配置在实际项目中会显的格外麻烦，因为我们的配置通常会是许多个，若使用上 例的方式则要使用@Value注入很多次。
Spring Boot提供了基于类型安全的配置方式，通过`@ConﬁgurationProperties`将**properties属性和一个Bean及其属性关联，从而实现类型安全的配置。**

- 创建一个类型安全Bean

  ```java
  @Component
  @ConfigurationProperties(prefix = "app.book")
  @Data
  public class BookProperties {
      private String author;
      private String name;
  }
  ```

**通过@ConﬁgurationProperties加载properties文件内的配置，通过preﬁx属性指定properties的配置的前缀，通过locations属性指定properties文件的位置，如果不指定默认读取application.properties和application.yaml。** 

```properties
app.book.name=java in action
app.book.author=Jackson
```

> 为了能够在增加属性的时候，IDE能提示，可以在pom中添加如下依赖：

```xml
        <!--SpringBoot的配置处理器，在编译的时期运用的，在正式运行的不需要的这个-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
            <scope>provided</scope>
        </dependency>
```

- 修控制类

  ```java
      //自动注入bookProperties属性配置
      @Autowired
      private BookProperties bookProperties;
  
  	/**
       * 获取book属性 从中获取配置中获取
       *
       * @return
       */
      @RequestMapping(value = "/book", method = {RequestMethod.GET})
      public BookProperties bookProperties() {
          return bookProperties;
      }
  ```

  > 通过`@Autowired`注解直接将BookProperties的Bean注入到类的成员。



## 4.日志配置

Spring Boot支持 Java Util Logging， Log4j, Log4J2 和 Logback 作为日志框架，无论使用哪种日志框架， Spring Boot已为当前使用日志框架的控制台输出以及输出文件做好了配置。
默认情况下，Spring Boot使用Logback作为日志框架。

- 默认日志输出文件

  ```properties
  logging.file=D:\spring-boot.log
  ```

- 配置日志级别

配置日志级别的格式为：logging.level.包名=级别（ `TRACE,DEBUG,INFO,WARN,ERROR,FATAL,OFF `）

```properties
logging.level.root=ERROR 
logging.level.com.bittech.boot=DEBUG
```



## 5.Profile配置

Proﬁle是Spring用来针对不同环境的进行不同配置提供的支持。全局Proﬁle配置使用 `application{profile}.properties` （例如：`application-prod.properties`）。
通过在application.properties中设置 `spring.profiles.active=prod `来指定活动的Proﬁle。这里多个活动的 Proﬁle时使用逗号分隔即可。

下面示例演示**生产环境（prod）**和**开发环境(dev)**，**生产环境Tomcat端口号80，开发环境Tomcat端口号8080：**

- 创建`applicaton-prod.properties`和`application-dev.properties`

  配置文件目录结构：

  ![1556871420507](D:\婕\JavaEE学习之路\Spring\picture\生产环境配置目录结构.png)

- 生产和开发环境的配置如下:

application-prod.properties

```properties
server.port=80 
book.name=Hello Spring Boot (prod)
```

application-dev.properties

```properties
server.port=8080
book.name=Hello Spring Boot(dev)
```

application.properties文件配置以及修改proﬁle

```properties
spring.profiles.active=prod 
book.author=secondriver 
book.name=Hello Spring Boot
```

- 切换proﬁle运行程序，访问地址，效果如下：

  ![1556871605879](D:\婕\JavaEE学习之路\Spring\picture\1556871605879.png)

## 总结

| 知识块         | 知识点                                      |
| -------------- | ------------------------------------------- |
| SpringBoot注解 | 1.核心注解类                                |
| Spring应用     | 1.基本配置2.starter理解3.配置文件4.日志配置 |

