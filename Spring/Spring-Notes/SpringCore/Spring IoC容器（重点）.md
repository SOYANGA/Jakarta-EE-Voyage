# Spring IoC容器（重点）

**重点**  

1. 了解IoC容器构建
2. 掌握Bean的初始化
3. 掌握Bean的依赖装配
4. 了解Bean的作用域
5. 了解Bean的生命周期

## 1.Ioc容器构建

Spring框架实际上是IoC容器，同时也是实现DI的功能，那如何创建一个SpringIoC容器



![1554884474737](C:\Users\32183\AppData\Roaming\Typora\typora-user-images\1554884474737.png)

​			关于Spring容器，Bean的配置信息，Bean的实现类以及应用程序之间的关系

SpringIoC的构建方式有：

- **A基于XML配置的方式**（可以开启注解配置，通过注解配置Bean和装配工作）
- B基于Goovy脚本配置的方式（也称：Spring Gooovy Bean Definition DSL）
- **C基于Java Config的配置注解方式**（主要通过Configuration和Bean注解，以及其他注解SpringBoot会用到的）

目前广泛的使用的是A和C两种方式，并且随着Spring生态的发展，方式C越来越流行了。

![1554885205110](D:\婕\JavaEE学习之路\Spring\picture\1554885205110.png)

如上图所示：无论是那种方式配置都属于**Conﬁguration Metadata**配置，通过配置来完成容器的组装。

下面是使用这是三种方式的示例：

- 方式A:**基于XML配置的方式**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">

    <!--此处配置类 创建对象的描述信息 有参构造 对象被动创建-->
    <bean id="circular" class="com.github.soyanga.common.impl.Circular">
        <constructor-arg name="radius" value="10"/>
    </bean>

    <bean id="rectangle" class="com.github.soyanga.common.impl.Rectangle">
        <constructor-arg name="width" value="10"/>
        <constructor-arg name="height" value="20"/>
    </bean>

    <bean id="triangle" class="com.github.soyanga.common.impl.Triangle">
        <constructor-arg name="a" value="10"/>
        <constructor-arg name="b" value="24"/>
        <constructor-arg name="c" value="30"/>
    </bean>

    <!--类装配属性 外键  依赖装配-->
    <bean id="xmlShapeCompute" class="com.github.soyanga.xml.XmlShapeCompute">
        <property name="circular" ref="circular"/>
        <property name="rectangle" ref="rectangle"/>
        <property name="triangle" ref="triangle"/>
    </bean>

</beans>
```

SpringIoC容器创建

```java
ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
```



- 方式B:Goovy脚本配置的方式 (了解即可)



- 方式C:基于Java Config的配置注解方式

  ```java
  package com.github.soyanga;
  
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.context.annotation.AnnotationConfigApplicationContext;
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  
  /**
   * @program: springcore-case-IoC
   * @Description: Ioc容器 配置类 跟业务逻辑无关联，主要是配置Bean的实例化
   * @Author: SOYANGA
   * @Create: 2019-04-10 16:46
   * @Version 1.0
   */
  @Configuration
  public class IocJavaConfigApplication {
  
      public static class Student {
          private int id;
          private String name;
  
          public int getId() {
              return id;
          }
  
          public void setId(int id) {
              this.id = id;
          }
  
          public String getName() {
              return name;
          }
  
          public void setName(String name) {
              this.name = name;
          }
  
          @Override
          public String toString() {
              return "Student{" +
                      "id=" + id +
                      ", name='" + name + '\'' +
                      '}';
          }
      }
  
      @Bean
      public String hello() {
          return "Hello";
      }
  
      @Bean(value = "zhangsanStudent")
      public Student zhangsan() {
          Student student = new Student();
          student.setId(1);
          student.setName("zhangsan");
          return student;
      }
      @Autowired
      private String hello;
  
      @Bean(value = "lisiStudent")
      public Student lisi() {
          Student student = new Student();
          student.setId(1);
          student.setName("lisi");
          return student;
      }
  
  
      /**
       * 基于Java Conﬁg的配置注解方式 （主要通过Conﬁguration和Bean注解）
       * @param args
       */
      public static void main(String[] args) {
          AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(IocJavaConfigApplication.class);
  
          String hello = (String) context.getBean("hello");
          System.out.println(hello);
  
          Student zhangsanStudent = (Student) context.getBean("zhangsanStudent");
          System.out.println(zhangsanStudent);
  
          Student lisiStudent = (Student) context.getBean("lisiStudent");
          System.out.println(lisiStudent);
      }
  }
  ```

- 使用XML配置优点

  > - xml是Spring最早支持的方式
  > - xml天生比较严谨，可以避免使用上的错误
  > - xml可视化较好，可以通过xml较为直观的看到整个容器中的Bean的关联关系
  > - xml的使用也是为了更好的理解Spring框架中的一些概念

## 2.Bean的实例化

JavaSE中创建一个Bean实例时，通常都是通过关键字`new`来完成的，那么我们要在xml配置中怎么完成Bean的实例化呢？Spring框架为我们提供了三种方式。

### 2.1构造方法实例化

```xml
<bean id="exampleBean" class="examples.ExampleBean"/>
<bean name="anotherExample" class="examples.ExampleBeanTwo">
```

通过构造方法方式实例化Bean,默认这个类是需要一个无参构造的。其中id和name都是Bean配置的属性，用来标识一个Bean。

### 2.2静态工厂方式实例化（类方法）

```java
public class ClientService {

    private static final ClientService cientService = new ClientService();

    private ClientService() {

    }

    public static ClientService getInstance() {
        return cientService;
    }
}
```

xml配置中添加`factory-method`

```xml
<!--2.静态工厂实例化Bean对象-->
<bean id="clientService" class="com.github.soyanga.ClientService" factory-method="getInstance"/>
```

### 2.3实例工厂方法实例化（对象方法）

实例工厂方法和静态工厂方法比较相似，区别在于实例工厂方法是通过**已经在容器中的Bean**通过其他方法实例化一个新的Bean。 简称：Bean中Bean

```java
package com.github.soyanga;

/**
 * @program: springcore-case-IoC
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-10 19:12
 * @Version 1.0
 */
public class DefaultClientServiceLocator {
//    private static ClientService clientService = new ClientService();

    public ClientService getClientService() {
        return ClientService.getInstance();
    }
}

```

xml配置中如下：

```xml
<!--3.实例工厂方法实例化（对象方法）-->
<bean id="defaultClientServiceLocator" class="com.github.soyanga.DefaultClientServiceLocator"/>
<bean id="clientService2" factory-bean="defaultClientServiceLocator" factory-method="getClientService"/>

```



## 3.Bean的依赖装配

SpringCore的核心内容

### 3.1依赖装配方式

#### 3.1.1构造方法参数装配

在Spring中，可以使用“通过构造方法自动装配”，实际上**是按照构造函数的参数类型以及构造方法的参数顺序自动装配**。这意味着，如果一个Bean的数据类型与其他Bean构造参数的数据类型是相同的，那么就将自动装配。当然如果装配过程中构造方法的参数具有歧义的时候，就需要我们**指定类型**，**位置**或者**参数名**的方式来告知Spring如何装配。

如下通过构造方法装配的4种方式

**第一种：Bean的引用**

```xml
    <!--第一种：Bean的引用-->
    <bean id="bar" class="com.github.soyanga.Bar"/>
    <bean id="baz" class="com.github.soyanga.Bazz"/>

    <bean id="foo" class="com.github.soyanga.Foo">
        <constructor-arg ref="bar"/>
        <constructor-arg ref="baz"/>
    </bean>
```

**第二种：根据参数类型**

```java
package com.github.soyanga;

public class ExampleBean3 {
    private int age;
    private String name;

    public ExampleBean3(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }


    public String getName() {

        return name;
    }

    @Override
    public String toString() {
        return "ExampleBean3{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}

```

```xml
<bean id="exampleBean3" class="com.github.soyanga.ExampleBean3">
     <constructor-arg type="int" value="20"/>
     <constructor-arg type="java.lang.String" value="张三"/>
</bean>
```

> 如果出现构造参数有两个类型一样的参数时，不能使用类型装配，会产生歧义

**第三种：根据参数位置**

```xml
<bean id="exampleBean4" class="com.github.soyanga.ExampleBean3">
   <constructor-arg index="0" value="25"/>
   <constructor-arg index="1" value="李四"/>
</bean>
```

第四种:根据参数名称

```xml
<!--第四种：根据参数名称-->
<bean id="exampleBean5" class="com.github.soyanga.ExampleBean3">
     <constructor-arg name="age" value="26"/>
     <constructor-arg name="name" value="王五"/>
</bean>
```

根据参数 具有特殊限制，需要开启编译时参数名称保留。在JDK1.8之前采用 @ConstructorProperties 注解， JDK1.8之后我们可以在编译的时候添加 -parameters 来确保编译之后保留参数名。

```java
@ConstructorProperties(value = {"age", "name"})
public ExampleBean3(int age, String name) {
     this.age = age;
     this.name = name;
}
```

####  3.1.2 Setter方法装配

容器在调用无参构造方法或者无参工厂方法实例化Bena之后，调用Setter方法完成属性的装配

```
public class Rectangle implements Shape {

}

public class ComplexShape {
    private Shape shape;

    public Shape getShape() {
        return shape;
    }
    public void setShape(Shape shape) {
        this.shape = shape;
    }
}

```

```xml
 <bean id="rectangle" class="com.github.soyanga.Rectangle"/>
<bean id="complexShape" class="com.github.soyanga.ComplexShape">
    <property name="shape" ref="rectangle"/>
</bean>
```



### 3.2依赖和装配详解

#### 3.2.1直接赋值

直接赋值主要针对`基本类型和String类型`，我们可以设置`properties`元素的`value`属性用来指定属性或者构造方法参数赋值

引用类型我们需要用`ref`来引用引用对象的id

开始之前，我们在maven中引用`commons-dbcp2`开源数据库连接池库，在配置文件中描述创建数据源`BasicDataSource`对象的方式

```xml
        <!--commons-dbcp2数据库连接池-->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-dbcp2</artifactId>
            <version>2.1.1</version>
        </dependency>

        <!--mysql依赖-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.43</version>
        </dependency>
```



```xml
<!--直接复制装配以及实例化 commons-dbcp2   destroy-method = 执行完进行销毁-->
<bean id="dataSource" class="org.apache.commons.dbcp2.BasicDataSource" destroy-method="close">
    <!--驱动-->
    <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
    <!--url-->
    <property name="url" value="jdbc:mysql://localhost:3306/scott"/>

    <property name="username" value="root"/>
    <property name="password" value="123456789"/>
    <!--最大连接数 int类型-->
    <property name="maxTotal" value="10"/>
</bean>
```

```java

DataSource dataSource = (DataSource) context.getBean("dataSource");
System.out.println(dataSource);
```

#### 3.2.2idref和ref元素

在Spring，idref和ref属性都可以用在construct-arg元素和property元素中完成装配

```xml
<!--idref(单纯引用Bean的id) 和 ref 区别-->
<bean id="idref" class="com.github.soyanga.Idref">
   <constructor-arg>
         <idref bean="bar"/>
    </constructor-arg>
</bean>
```

`idref`和`ref`区别:

- idref装配的目标是id而不是目标bean的实例，同时使用idref容器在部署的时候还有验证这个名称曾bean是否真实存在。其实idref就是跟value一样，只是将某个字符串装配到属性或者构造函数当中去了，只不过装配的是某个Bean的定义和id属性
- ref则是讲目标Bean定义的实例装配到属性或者构造函数当中去了。

#### 3.2.3内部Bean

一个`bean`元素定义在`<property>`和`<constructor-arg/>`元素中我们称为`inner Bean`

```xml

    <!--内部Bean -->
    <bean id="foo2" class="com.github.soyanga.Foo">
        <constructor-arg>
            <bean class="com.github.soyanga.Bar"/>
        </constructor-arg>
        <constructor-arg>
            <bean class="com.github.soyanga.Bazz"/>
        </constructor-arg>
    </bean>
```

### 3.3其他元素

#### 3.3.1Collection

使用`<list/>,<set/>,<map/>,<props/>`元素可以设置java集合类型(比如：List,Set.map.Properties)的属性和参数

```xml
    <!--Collection集合元素的使用-->
    <bean id="complexObject" class="com.github.soyanga.ComplexObject">
        <property name="properties">
            <props>
                <prop key="admin">admin@126.com</prop>
                <prop key="soyanga">soyanga@126.com</prop>
                <prop key="bing">bing@126.com</prop>
            </props>
        </property>
        <property name="list">
            <list>
                <value>java</value>
                <null/>
                <idref bean="idref"/>
                <value>20</value>
            </list>
        </property>
        <property name="set">
            <set>
                <value>50ddd</value>
                <value>100sdasd</value>
            </set>
        </property>

        <property name="map">
            <map>
                <entry key="sss" value="wwwww"/>
                <entry key="s85" value-ref="foo2"/>
                <entry key="ada" value-ref="dataSource"/>
                <entry>
                    <key>
                        <null/>
                    </key>
                    <value>
                        aaaaaaaaaaaaaa
                    </value>
                </entry>
            </map>
        </property>
    </bean>
```

```java
package com.github.soyanga;

import java.util.*;

public class ComplexObject {

    private Properties properties;
    private List<String> list;
    private Set<String> set;
    private Map<String, Object> map;

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Set<String> getSet() {
        return set;
    }

    public void setSet(Set<String> set) {
        this.set = set;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "ComplexObject{" +
                "list=" + list +
                ", set=" + set +
                ", map=" + map +
                '}';
    }
}

```

在使用集合元素时如Map的key和Value，集合的value可以使用如下元素：

<list/>,<set/>,<map/>,<props/>,<bean>,<ref>,<idref>,<value>,<null>

#### 3.3.2Null和空字符串值

- 空字符串赋值和Null赋值

  ```xml
      <!--null和空字符串-->
      <bean id="exampleEmail" class="com.github.soyanga.ExampleEmail">
          <property name="email" value=""/>
          <property name="name">
              <null/>
          </property>
      </bean>
  ```

- 等价于Java中设置

  ```java
  exampleEmail.setName("");
  exampleEmail.setValue(null);
  ```

#### 3.3.3延迟初始化

Spring默认容器启动时将所有Bean都初始化完成，设置延时初始化告诉容器在第一次使用Bean的时候，完成初始化操作，而不是启动的。  （相当于懒加载）

```xml
    <!--Lazy-initialized-->
    <bean id="lazy" class="com.github.soyanga.Bazz" lazy-init="true">
        
    </bean>
```

#### 3.3.4自动装配（了解）

在课程前面我们都讲的时手工完成Bean之间的一依赖关系的建立（装配），Spring还提供了自动装配的能力，Spring的自动咋装配的功能定义：无需再Spring配置文件中描述Bean之间的依赖关系(如配置`<property>,<constructor-arg>`),IoC容器会自动建立Bean之间的依赖关系。

```java
public class Customer {
    private final Bar bar;

    public Customer(Bar bar) {
        this.bar = bar;
    }

    public Bar getBar() {
        return bar;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "bar=" + bar +
                '}';
    }
}

public class Customer2 {
    private Bar bar;

    public Bar getBar() {
        return bar;
    }

    public void setBar(Bar bar) {
        this.bar = bar;
    }

    @Override
    public String toString() {
        return "Customer2{" +
                "bar=" + bar +
                '}';
    }
}

```

```xml
    <!--自动装配 构造方法 调用的是byType-->
    <bean id="customer" class="com.github.soyanga.Customer" autowire="constructor"/>

    <!--自动装配 属性byName-->
    <bean id="customer2" class="com.github.soyanga.Customer2" autowire="byName"/>

    <!--自动装配 属性byType-->
    <bean id="customer3" class="com.github.soyanga.Customer2" autowire="byType"/>
```

## 4.Bean的作用域

Bean的作用域是描述一个Bean实例在Spring IoC容器的存在状态，比如根据不同请求，线程而存在多个。

**Singleton:**

- 描述：该作用域下的Bean在IoC容器中只存在一个实例，所有对象对其的引用都返回同一个
- 场景：无状态的Bean使用该作用域
- Spring默认选择该作用域

![1555259862478](C:\Users\32183\AppData\Roaming\Typora\typora-user-images\1555259862478.png)

**Prototype:**

- 描述：每次对该作用域下的Bean的请求都会创新的实例
- 场景：有状态的Bean使用该作用域

![1555259936004](D:\婕\JavaEE学习之路\Spring\picture\1555259936004.png)

**request** httpService 

描述：每次http请求会创建新的Bean实例，类似于prototype 

场景：一次http的请求和响应的共享Bean 

备注：限定SpringMVC中使用



**session** httpSession

- 描述：在一个http session中，定义一个Bean实例 

- 场景：用户回话的共享Bean, 比如：记录一个用户的登陆信息 

- 备注：限定SpringMVC中使用



**global session** 门户网站

- 描述：类似与http session，但限于protlet web应用可用 

- 场景：所有构成某个portlet web应用的各种不同的portlet所共享 

- 备注：限定protlet web应用，你在编写一个标准的基于Servlet的web应用，并且定义了一个或多个具有 global session作用域的bean，http session作用域，并且不会引起任何错误 



**application session**   域对象

- 描述：在一个http servlet Context中，定义一个Bean实例 

- 场景：Web应用的上下文信息，比如：记录一个应用的共享信息 

- 备注：限定SpringMVC中使用 

## 5.基于注解配置

无论是通过XML还是注解配置，它们都是表达Bean定义的载体，其本质都是为Spring容器提供Bean定义的信息。 在表现形式上都是将XML定义的内容通过注解进行描述。 

Spring容器启动成功的三大要素： 

- Bean定义信息
- Bean实现类
- Spring本身

### 5.1使用注解定义Bean

采用基于XML的配置，则Bean定义信息和Bean实现类本身分离的

采用基于注解的配置文件，则Bean的定义信息是通过Bean实现类上标注注解实现的。



#### Bean的标识

标识一个类，可以被Spring容器识别，自动转为被容器管理的Bean。

@Component注解标识的类：表明是一个普通的Bean

@Repository注解标识的类：用户DAO实现了进行标识

@Servicr:用于Service实现类进行标识

@Contronller:用于Contorller实现类进行标识

这几个标识的本质是相同的均表明类被标识，分别设置主要是为了标识类的用途，使我们可以一眼看出Bean的用途。



### 5.2使用注解装配

#### 5.2.1@Autowired自动装配

@Autowired实现Bean的注入，基础类型默认是按照(byType)匹配的方式在容器中查找匹配的Bean,当有且仅有一个匹配的Bean时，Spring将其注入@Autowired标注的变量中（引用类型）

@Autorwired的required属性值默认为true,如果容器中没有找到标注变量类型的匹配的Bean,那么Spring容器启动时将报`NoSuchBeanDefintionException`异常。如果希望Spring及时找不到匹配的Bean完成注入时也不要抛出异常，那么可以使用@Autowired(required=false)进行标志

#### 5.2.2@Qualifier注入指定Bean

加入容器中的三个类型Shapr的Bean,分别为：cuirclar,rectangle,triangle,则上面代码中注入一个circular的Bean。

#### 5.2.3@Scope,@PostConstruct,@PreDestory

@Scope:标识Bean的生命作用域

@PostConstruct:标识Bean的初始化之后调用的方法 java的类

@PreDesyory:标识Bean的销毁之前调用的方法  java的类

```java
@Component @Scope(scopeName = "prototype") 
public class CardComponent {        
	@PostConstruct    
	public void init() {        
		System.out.println("Bean初始化之后调用");    
	}        
	@PreDestroy    
	public void destroy() {        
		System.out.println("Bean销毁之前调用");    
	} 
}
```

上面的注解方式等价与XML中的配置

```xml
<bean class="com.bittech.common.component.CardComponent" scope="prototype" initmethod="init" destroy-method="destroy"/>
```

## 6.Bean的配置比较



配置应用场景比较

| 配置方式 | 基于XML配置                                                  | 基于注解配置                                                 | 基于Java类配置（IoC容器）                                    |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 适用场景 | Bean实现类来源第三方类库，如DataSource等，因此无法在类中标注注解，所以通过XML配置方式较好。 | Bean的实现类是当前项目开发的，且可以直接在Java类中使用基于注解的配置 | 基于Java的类配置优势在于可以通过代码方式控制Bean初始化的整体逻辑，如果实例化Bean的逻辑比较复杂，则比较适应基于Java类配置的方式 |

