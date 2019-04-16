# Spring资源处理和SpEL语言

> **重点：**

> 1. 掌握Spring资源加载
> 2. 掌握SpEL语言的基本使用

## 1.SpEL语言

Spring动态表达式语言(简称：SpEL)是一**个支持运行时查询和操作对象图的强大动态语言**。其语法类似于EL(`Expression Language`)表达式，具有诸如**显示方法调用**和**基本字符串模板函数**等特性。

思考：为什么引入SpEL

- Java是一门强类型的静态语言，所有代码在运行之前都必须进行严格的类型检查并且编译称JVM字节码。因此虽然安全，性能方面得到了保障，但是牺牲了灵活性，这个特征就决定了**Java在语言层面上无法直接进行表达式语句的动态解析。**
- 动态语言恰恰相反,其显著特点是在程序运行时可以改变程序的结构和变量类型。典型的动态语言有`Python,Ruby,JavaScript,Perl`等。这些动态语能够语言能够广泛应用在各种领域，得益于其动态，简单，灵活的特性。
- Java在实现复杂业务系统，大型商业系统。分布式系统以及中间件等方面有着非常强的优势，在开发这些系统的过程中（如：积分规则，促销活动，游戏的技能设置），有时候需要引用动态语言的一些特性，以弥补其在动态性能方面的不足，JDK1.6后嵌入JavaScript解析引擎(`NashornScriptEngine`)，方便再Java中调用JavaScript编写的动态脚本。
- Spring社区为了弥补Java语言动态性能方面不足，提供了Spring动态语言，其实是**一个支持运行时查询和操作对象图的强大**的动态语言（语法类似与EL表达式，具备**显示方法调用，基本字符串模板函数等特性**）。

### 1.1SpEL核心接口

SpEL的所有类与接口都定义在`oeg.springframework.expression`包及其子包，以及`spel.support`中。

该部分位于Spring框架的expression模块

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-expression</artifactId>
</dependency>
```

#### 1.1.1ExpressionParse接口 -字符串解析

ExpressionParse接口用来**解析表达式字符串**，表达式字符串是一个用**单引号标注**或者用**转义的双引号标注的字符串**。

ExpressionParse接口的实现类：

![类图](D:\婕\JavaEE学习之路\Spring\picture\1555345066576.png)



#### 1.1.2EvaluationContext接口 - 属性方法字段解析，类型转换器

EvaluationContext接口提供**属性，方法，字段解析器以及类型转换器**。 默认实现类StandardEvaluationContext 的**内部使用反射机制来操作对象**，为了提高性能，在其会对已经获取的Method, Field和Constructor实例进行**缓存**。

![1555345240085](D:\婕\JavaEE学习之路\Spring\picture\1555345240085.png)

### 1.2SPEL基础表达式



#### 1.2.1文本字符解析

文本表达式支持字符串，日期，字符，布尔类型以及null。其中字符串需要使用单引号或者反斜杠+双引号包含起来，比如：`‘Hello World'"," \"Hello World\" " ` 

```java
import lombok.Data;
import java.util.Date;

@Data
public class User {
    /**
     *    
     * 用户名    
     */
    private String userName;
    /**
     *     * 近访问时间    
     */
    private Date lastVisit;
    /**
     *     * 用户积分    
     */
    private Integer credits;
    /**
     *     * 用户出生地    
     */
    private PlaceOfBirth placeOfBirth;
}


package com.github.soyanga.SpEL;

import lombok.Data;

@Data
public class PlaceOfBirth {
    //国家    
    private final String nation;
    //地区    
    private final String district;

    public PlaceOfBirth(String nation, String district) {
        this.nation = nation;
        this.district = district;
    }
}

```

- 字符串解析

```java
public class SpELTextApplication {
    public static void main(String[] args) {
        //构造一个标识式实例
        ExpressionParser parser = new SpelExpressionParser();

        //解析字符串
        String helloWorld = (String) parser.parseExpression("'Hello World'").getValue();
        String helloWorld2 = parser.parseExpression("\"Hello World \"").getValue(String.class);

        System.out.println(helloWorld);
        System.out.println(helloWorld2);
        //解析布尔类型 true /false
        Boolean booleanValue = parser.parseExpression("true").getValue(Boolean.class);
        System.out.println(booleanValue);
        //解析整数类型
        int value = parser.parseExpression("120").getValue(Integer.class);
        System.out.println(value);

    }
}
```



#### 1.2.2对象属性解析

```java
public class SpELObjectTextApplication {
    public static void main(String[] args) {
        //1.创建User对象
        User user = new User();
        user.setUserName("soyanga");
        user.setLastVisit(new Date());
        user.setCredits(20);
        PlaceOfBirth placeOfBirth = new PlaceOfBirth("中国", "西安");
        user.setPlaceOfBirth(placeOfBirth);
        System.out.println(user);
        System.out.println(user.getPlaceOfBirth());

        //SpEl访问对象属性 2.构造SpEl解析解析上下文
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext(user);

        //访问对象姓名
        String userName = (String) parser.parseExpression("userName").getValue(context);
        Integer credits = (Integer) parser.parseExpression("credits").getValue(context);
        System.out.println(userName);
        System.out.println(credits);

        //嵌套对象属性获取 - placeofBirth
        PlaceOfBirth placeOfBirth1 = (PlaceOfBirth) parser.parseExpression("placeOfBirth").getValue(context, PlaceOfBirth.class);
        System.out.println(placeOfBirth1);
        String district = (String) parser.parseExpression("placeOfBirth.district").getValue(context);
        System.out.println(district);


        //不使用上下文 效率低--上下文使用了缓存，且不需要反复加载反射类。
        String district2 = (String) parser.parseExpression("placeOfBirth.district").getValue(user, String.class);
        System.out.println(district2);

        //
        String nation = (String) parser.parseExpression("placeOfBirth.nation").getValue(user, String.class);
        System.out.println(nation);
    }
}

```



对象属性解析和文本字符解析不同，不需要加单引号或者转译\。对于属性解析需要在取值的时候传递一个计算上下文参数`EvaluationContext`。这里将`user`实例作为上下文的根对象传递给`EvaluationContext `，这样SpEL表达式解析器就可以根据**属性路径表达式** 获取**上下文中根对象的属性值**。 

但是这样反复使用根对象时，会有效率上的差异，上下文`EvaluationContext context` 中使用为了提高性能，在其会对已经获取的Method, Field和Constructor实例进行**缓存**。所以连续解析属性或其他字段时，上下文会更有效率。



#### 1.2.3数组和集合解析

·在SpEL中，支持数组，集合类型（Map,List）的解析。数组支持标准Java语言创建数组的方法，如：`new int[]{1,2,3}`。List支持大括号起来的内容，数据项之间用逗号隔开，如`"{1,2,3}","{{'a','b'},{'x','y'}}"。`目前SpEL还不支持多维数组初始化，如： "new int\[2][3]{{1,2,3},{4,5,6}}" 。Map采用如下表达式`{userName:'Jack',credits:100}" `。

```java
public class SpELCollectionApplication {
    public static void main(String[] args) {
        //构造一个标识式实例
        ExpressionParser parser = new SpelExpressionParser();

        //array 解析成数组对象
        int[] arrray = parser.parseExpression("new int[] {1,2,3}").getValue(int[].class);
        System.out.println(Arrays.toString(arrray));

        String[] strArrayValue = parser.parseExpression("new String[] {\"Hello\",\"World\"}").getValue(String[].class);
        System.out.println(Arrays.toString(strArrayValue));

        //list
        List listIntValue = parser.parseExpression("{1,2,3,4}").getValue(List.class);
        System.out.println(listIntValue);

        List listObjectValue = parser.parseExpression("{\"java\",\"Spring\"}").getValue(List.class);
        System.out.println(listObjectValue);

        //map {key:value, key:value}
        Map mapValue2 = parser.parseExpression
                ("{userName:'zhangsan',age:28,placeOfBirth:{nation:'china',district:'Xian'}}").getValue(Map.class);
        System.out.println(mapValue2);
        System.out.println(mapValue2.get("userName"));
        System.out.println(mapValue2.get("placeOfBirth"));

    }
}
```

#### 1.2.4方法解析

在SpEL中，**方法调用Java可以访问的方法，包括对象方法，静态方法，并支持可变方法参数，还可以调用String类型的所有可以访问的方法。**

```java
public class SpELMathodApplication {
    public static void main(String[] args) {
        //构造一个标识式实例
        ExpressionParser parser = new SpelExpressionParser();

        //String对象的方法 substring ,length ,indexOf

        String strSubstring = parser.parseExpression("'HelloWorld'.substring(5)").getValue(String.class);
        System.out.println(strSubstring);
        
        Integer strLength = parser.parseExpression("'HelloWorld'.length()").getValue(Integer.class);
        System.out.println(strLength);
        
        Integer strIndexOf = parser.parseExpression("'HelloWorld'.indexOf('World')").getValue(Integer.class);
        System.out.println(strIndexOf);

        //Java SE中反射的方式调用length方法
        try {
            String obj = "HelloWolrd";
            Method method = obj.getClass().getMethod("length");
            Object value = method.invoke(obj);
            System.out.println(value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        //静态方法 通过类来访问
        //java.lang.System.currentTimeMillis();
        long currentTimes = parser.parseExpression("T(java.lang.System).currentTimeMillis()").getValue(long.class);
        System.out.println(currentTimes);

        //java.lang.Math.min()
        double minValue = parser.parseExpression("T(java.lang.Math).min(10,20)").getValue(Double.class);
        System.out.println(minValue);

        //实例方法
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        EvaluationContext context = new StandardEvaluationContext(simpleDateFormat);
        //注意:表达式字符串中要创建对象需要使用类的全限定名
        //String value = simpleDateFormat.format(new java.util.Date());
        String dateFormat = parser.parseExpression("format(new java.util.Date())").getValue(context, String.class);
        System.out.println(dateFormat);
    }
}
```



### 1.3Spring中使用SpEL

#### 1.3.1基于XML的配置

- XML配置表达式

  ```xml
  <bean id="guessNumber" class="com.github.soyanga.spel.pojo.GuessNumber" scope="prototype">
       <property name="number" 
                 value="#{T(java.lang.Math).random()}"/>
  </bean>
  ```

  ```java
  package com.github.soyanga.spel.pojo;
  
  import lombok.Data;
  
  @Data
  public class GuessNumber {
      private double number;
  }
  
  ```

通过SpEL提供的T类型操作符，直接调用java.lang.Math的静态方法生成一个随机数赋值给GuessNumber 的number属性。

- 内置Bean`SystemProperties`和`SystemEnvironment`

Spring中内置了`SystemProperties`和`SystemEnvironment`两个Bean,可以通过它们分别获取系统属性变量值和系统环境变量值。

> System.getProperties()；方法获取系统属性 
>
> System.getenv()；方法获取系统环境变量

```xml
    <!--内置Bean ->系统属性Bean systemProperties  -->
    <bean id="systemPropertiesBean" class="com.github.soyanga.spel.pojo.SystemPropertiesBean">
        <property name="classPath" value="#{systemProperties['java.class.path']}"/>
        <property name="javaHome" value="#{systemProperties['java.home']}"/>
        <property name="javaVersion" value="#{systemProperties['java.version']}"/>
        <property name="osName" value="#{systemProperties['os.name']}"/>
    </bean>

    <!--内置Bean -> 系统环境变量Bean SystemEnvironment-->
    <bean id="systemEnvironmentBean" class="com.github.soyanga.spel.pojo.SystemEnvironmentBean">
        <property name="appData" value="#{systemEnvironment['APPDATA']}"/>
        <property name="path" value="#{systemEnvironment['Path']}"/>
        <property name="systemDriver" value="#{systemEnvironment['SystemDirver']}"/>
    </bean>
```

```java
//内置系统属性Bean
SystemPropertiesBean systemPropertiesBean = (SystemPropertiesBean) context.getBean("systemPropertiesBean");
System.out.println(systemPropertiesBean);

//内置系统环境Bean
SystemEnvironmentBean systemEnvironmentBean = (SystemEnvironmentBean) context.getBean("systemEnvironmentBean");
System.out.println("\n\n" + systemEnvironmentBean);

```

#### 1.3.2基于注解的配置

**`@Value`**注解可以标注在**类中的属性，方法，构造函数上**。下面是一个从配置文件中加载一个参数值的示例。

- 配置文件

  ```properties
  #配置文件：database.properties 
  url=jdbc:mysql://localhost:3306/memo 
  username=root 
  password=root 
  classname=com.mysql.jdbc.Driver
  ```

- 在XML中配置一个Id为`properties`的Bean用来加载配置文件

  ```xml
  <!-- 开启自动扫描 --> <context:component-scan base-package="com.bittech.example"/>
   
  <util:properties id="properties" location="database.properties"/
  ```

- 在Bean中通过@Value注解注入

  ```java
  @Data
  @Component
  public class MyDataSource {        
  	@Value(value = "#{properties['url']}")    
  	private String url;
      
  	@Value(value = "#{properties['username']}")    
  	private String username;
      
  	@Value("#{properties['password']}")    
  	private String password;
      
  	@Value("#{properties['classname']}")   
  	private String classname;        
  }
  ```

Spring提供了另一种更为简便的方式占位符的方式。

## 2.资源配置文件

我们在配置Bean的属性时，大多数情况下值基本是固定的。这样就存在一个问题，在配置例如数据库的用户名，密码；邮箱用户名 密码；以及一些编码阶段不可预知的信息（支付宝AppId私钥，公钥），这个时候我们就需要外置资源文件来解决这个问题。

例如将上述properties文件的内容修改一下，让其变的直观

```properties
#配置文件：database.properties
jdbc.url=jdbc:mysql://localhost:3306/Scott
jdbc.username=root
jdbc.password=123456789
jdbc.driverclass=com.mysql.jdbc.Driver

pay.alipay.id=
pay.weixin.id=

email.host=
email.port=
email.username=
email.password=
```

### 2.1 使用PropertyPlaceholderConﬁgure配置属性文件 

- 单个资源文件

```xml
    <!--资源配置文件 Spring提供的类PropertyPlaceholderConfigurer   单文处理-->
    <bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="fileEncoding" value="UTF-8"/>
        <property name="location" value="config.properties"/>
    </bean>
```

- 多个资源文件

  ```xml
      <!--多文件处理 合并两个文件属性 -->
      <bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
          <property name="fileEncoding" value="UTF-8"/>
          <property name="locations">
              <list>
                  <value>classpath:database.properties</value>
                  <value>classpath:config.properties</value>
              </list>
          </property>
      </bean>
  ```



### 2.2使用 `context:property-placeholder` 配置属性文件 

```xml
<!--用元素标签使用占位符-->
<context:property-placeholder file-encoding="UTF-8" location="config.properties,database.properties"/>
```

以上配置相当于在Spring容器中配置了一个 PropertyPlaceholderConfigurer 的Bean，显然这种方式更为优雅。

### 2.3使用@Value注解给Bean的属性注入值 

```java
@Component
@Data
public class MyDataSource2 {

    @Value(value = "${jdbc.url}")
    private String url;

    @Value(value = "${jdbc.username}")
    private String userName;

    @Value(value = "${password}")
    private String passWord;

    @Value(value = "${jdbc.driverclass}")
    private String driverClass;
}
```

### 2.4 Spring资源接口 

我们在使用Spring框架过程中，离不开和各类资源文件打交道，这些资源文件有来自本地文件，有来自一个jar，也有来自一个URL。因为JDK操作底层资源基本上是从`java.net.URL, java.io.File, java.util.Properties`这些类开始，获取资源要么是从绝对路径，要么就是当前类的相对路径。从类路径和web容器上下文中获取资源是很不方便的。Spring的Resource接口提供了统一的访问底层资源的能力。

Resource接口的实现类结构图：

![1555421823885](D:\婕\JavaEE学习之路\Spring\picture\1555421823885.png)

> 面向接口编程
>
> 策略模式

- ByteArrayResource ： 代表 byte[] 数组资源， getInputStream 将返回一个ByteArrayInputStream 。 ByteArrayResource 可**多次读取**数组资源，即 isOpen () 永远返回 false 。
- InputStreamResource ： 代表 java.io.InputStream 字节流，对于 getInputStream 操作将直接返回该字节流，因此只能**读取一次**该字节流，即 isOpen 永远返回 true 。 
- FileSystemResource ： 代表 java.io.File资源，对于getInputStream操作将返回底层文件的字节流，isOpen 将永远返回false ，从而表示**可多次读取底层文件**的字节流。 
- ClassPathResource ：代表 classpath 路径的资源，将使用 ClassLoader 进行加载资源。classpath 资源存在于类路径中的文件系统中或jar包里，且 isOpen 永远返回 false ，表示**可多次读取资源**。 ClassPathResource 加载资源替代了Class类和ClassLoader类的 getResource(String name) 和 **getResourceAsStream(String name) 两个加载类路径资源方法，提供一致的访问方式。**

了解Spring资源接口实现类之后，可以通过文件名的前缀来动态决定加载资源时使用的实现类（实际Spring也是通 过这种方式来实现资源的加载时策略的选择）。

```java
public class SpringResourceApplication {

    //file:D:/db.properties   file:linux--> /home/lisan/db.propertoes
    // http://www.zhangsan.com/files/db.propertoes
    //classpath:com/soyanga/db.propertoes
    //classpath:db.propertoes

    /**
     * file:D:/db.properties   file:linux--> /home/lisan/db.propertoes
     * http://www.zhangsan.com/files/db.propertoes
     * classpath:com/soyanga/db.propertoes
     * classpath:db.propertoes
     * 通过文件名去判定资源所属的具体的Resource类
     *
     * @param filename 如上
     * @return 文件加载输入流
     */
    public static Resource loadResource(String filename) {
        if (filename == null || filename.length() == 0) {
            return null;
        }
        if (filename.startsWith("file")) {
            return new FileSystemResource(filename.substring("file:".length()));
        }
        if (filename.startsWith("classpath")) {
            return new ClassPathResource(filename.substring("classpath".length()));

        }
        if (filename.startsWith("http") || filename.startsWith("ftp")) {
            try {
                return new UrlResource(filename);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Resource resource = loadResource("http://www.zhangsan.com/files/db.propertoes");
        System.out.println(resource);

    }
}
```



| 知识块   | 知识点                                |
| -------- | ------------------------------------- |
| SpEL     | 1.SpEL核心接口 <br>2. SpEL基本使用    |
| 资源配置 | 1. 资源接口实现类 <br>2. 属性文件配置 |

