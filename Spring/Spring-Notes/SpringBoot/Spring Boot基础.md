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

- @**propertySource**

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

```
    @Bean(initMethod = "init", destroyMethod = "destroy")
    @Scope(scopeName = "prototype") //非单例
    public ExampleBean exampleBean() {
        ExampleBean exampleBean = new ExampleBean();
        exampleBean.setName("jack");
        return exampleBean;
    }
```

- **Enviroment**

`org.springframework.core.env.Environment` 环境类，Spring3.1以后开始引入。比如JDK环境，Servlet环境，Spring环境等等；每个环境都有自己的配置数据，如``System.getProperties()`、`System.getenv()``等可以拿到JDK环境数据；``ServletContext.getInitParameter()`可以拿到Servlet环境配置数据等等；**也就是说Spring抽象了一 个Environment来表示环境配置。**
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

良好的习惯，通常Spring Boot项目有一个名为***Application**的入口类，入口类里有一个main方法，该main方法其实就一个标准的java应用入口方法。在main方法中使用**SpringApplication.run(…)方法来启动Spring Boot应用项目。**

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

有时候需要关闭特定的自动配置，这时需要使用``@SpringBootApplication`注解的**exclude**参数

```java
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
```

在如下图的选项中 寻找要关闭的类中的以 *Configuration结尾的类，那就是自动注入的注解

仅仅就是关闭自动装配，可以自己添加Bean，手动自动装配即可。

如果一个Bean自己实现了一个，第三方也提供了Bean。SpringBoot默认使用自己写的

这是因为`@ConditionalOnMissingBean`注解，但是不一定每个类都会实现这个注解，所以我们需要在自己写的类上添加`@Primary`注解就会让注解成为主要的Bean，就默认会使用添加``@Primary`的Bean。

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

```
|  _ \(_) |   |__   __|      | |      / _|                | |                  
| |_) |_| |_     | | ___  ___| |__   | |_ ___  _ __       | | __ ___   ____ _  
|  _ <| | __|    | |/ _ \/ __| '_ \  |  _/ _ \| '__|  _   | |/ _` \ \ / / _` | 
| |_) | | |_     | |  __/ (__| | | | | || (_) | |    | |__| | (_| |\ V / (_| | |____/|_|\__|    |_|\___|\___|_| |_| |_| \___/|_|     \____/ \__,_| \_/ \__,_|

${application.name}
```

#### 2.3.2关闭Banner

- 在main方法中构造Spring Application对象时修改

```

```

- 使用Fluent AP修改

```

```

> 扩展：

### 2.4Spring Boot的配置

