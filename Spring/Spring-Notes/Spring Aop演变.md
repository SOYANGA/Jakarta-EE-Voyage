# Spring Aop演变

**重点**

> 1. 理解AOP概念
> 2. 了解AOP的基础知识’
> 3. 掌握AOP的基础编程
> 4. 了解基于@AspectJ和Schema的AOP

## 1.AOP是什么？

### 1.1AOP的概念

AOP`Aspect oriented Programing`简称，面向切面编程。可以说是OOP（`Object-Oriented Programing`面向对象编程）的补充和完善。OOP三大特征：**封装、继承、多态**等概念来建立一种**对象层结构**，**用以模拟公共行为的一个集合**。当我们需要**分散的对象引入公共行为的时候**，OOP则显得无能为力。也就是说，**OOP允许你定义从上到下的关系，但并不适合定义从左到右的关系**。

例如日志功能，日志代码往往水平的散步在所有对象层次中，而与它所散步到的对象的核心功能毫无关系。对于其他类型的代码，如安全性，异常处理也是如此。**这种散步在各处的代码被称为横切（cross-cutting）代码，在OOP设计中，它导致了大量代码的重复，而不利于各个模块的重用。** 

而AOP技术则恰恰相反，它利用一种称为“横切”的技术，解剖开封装的对象内部，并将那些影响了多个类的**公共行为封装到一个可重用模块**，并将其命名为“Aspect”切面

所谓”切面“，**就是将那些与业务无关，却为业务模块所共同调用或责任封装起来，==便于减少系统的重复代码，降低模块之间的耦合度，并有利于未来的可操作性和维护性。==**AOP代表的是一个横向的关系。

> 如果说“对象”是一个空心圆柱体，其中封装的是对象得到属性和行为；那么面向切面编程就是一把利刃，将这些空心圆柱剖开，以获得其内部的消息。而剖开的切面，也就是所谓的“切面”了，然后它又以巧夺天功的妙手将这些切面复原，不留痕迹。

![1555488483358](D:\婕\JavaEE学习之路\Spring\picture\AOP与OOP关注点的区别)

AOP把软件系统分为两部分：**核心关注点**和**横切关注点**。 

两点之间并没有紧密的耦合  解耦**功能逻辑**和**对象核心业务**

**核心关注点：**业务处理的主要流程就是核心关注点

**横切关注点**：与之关系不大的部分且每层业务都会涉及到的公共部分被称为横切关注点。(并不是所属对象所作的事)

**横切关注点的特点：**横切关注点有一个特点，他们经常发生在核心关注点的多处，而各处都基本相似。比如权限认证，日志，事务处理。

**AOP的作用**：AOP的作用在于**分离系统中的各个关注点，将核心关注点和横切关注点分离开来。**

**AOP的核心思想**就是“**将应用程序中的商业逻辑同对其提供支持的通用服务进行分离**。”-----拆分核心关注点 与 横切关注点（通用辅助共同点）

![1555489002470](D:\婕\JavaEE学习之路\Spring\picture\OOP作用)

实现AOP技术，主要分为两大类

- 采用**动态代理技术**，利用**截取消息**的方式，对该**消息进行装饰**，以**取代原有对象行为的执行**。
- 采用**静态植入的方式**，引入特定的语法创建“切面“”，从而使得编译器可以在**编译期间植入有关“切面”的代码。** 

## 2.AOP思想演变

### 2.1演变过程

一个接口由多个类实现，且类中均有核心的业务方法和一些公共处理（安全检查，权限检查，打印日志等公共模块）

则在每个类的核心业务实现的时候就会大量重复编写这块的代码，所以我们将公共部分提取出模板或者公共的父类方法。然后让调用的类来使用公共模板或者继承公共父类。

- 弊端

但是这种方式有弊端就是，当业务扩展时，或需求更改时我们必须要修改大量的代码。需要修改的地方分散在很多个文件中，如果需要修改的文件很多那么修改的量会很大，这无疑会增加出错的几率，并且加大系统维护的难度。 而且，如果添加功能的需求是在软件开发的后期才提出的话，这样大量修改已有的文件，也不符合基本的“**开-闭原则”。**

改进解决方案

采用装饰器模式或者代理模式来实现业务公共业务和核心业务直间的关系

- 装饰器模式定义：动态地给一个对象添加一些额外地指责。就是增加功能来说，装饰器模式比生成子类更为灵活。
- 代理模式定义：为其它对象提供一种代理以控制这个对象的访问。（将对象访问限制为经过代理对象后才能访问到）

代理分为：

- 静态代理（实现同一个接口）/JDK动态代理
- CGLIB动态代理

相比于JDK动态代理（实现InvocationHandler 接口 覆写invoke方法）来说CGIB更灵活，JDK动态代理只能解决存在接口的子类的代理。而CGLIB则无需有接口，只需要有对象即可实现动态代理（继承MethodInterceptor 类实现intercept方法）生成一个拦截器，即包装原有方法。进而达到实现动态代理的作用。

但是这样做确实解决了公共模块代码冗余的情况，但是这样一来所有使用到公共模块的对象，都不能直接生成，需要经过动态代理后才能使用，并不符合面向对象编程的习惯。

![1555519359986](D:\婕\JavaEE学习之路\Spring\picture\AOP演变过程-动态代理)

为了更好的解决--AOP提供

人们认识到，传统的程序经常表现出一些不能自然地适合跨越多个程序模块的行为，例如日志记录、对上下文敏感 的错误处理等等，人们将这种行为称为 **“横切关注点（Cross Cutting Concern）**”，因为它跨越了给定编程模型中 的典型职责界限。 如果使用过于密切关注点的代码，就会知道**缺乏模块性**所带来的问题。因为横切行为的实现是分散的，开发人员发现**这种行为难以作逻辑思维实现和更改**。因此，面向方面的编程AOP应运而生。 

## 2.AOP作用?

切面关注通用的逻辑，下面是在项目中常用到切面的场景：

- Authentication 权限
- Caching 缓存
- Context passing 内容传递
- Error handing 错误处理
- Lazy loading 懒加载
- Debugging 调试
- logging,tacing,profiling and monitoring 记录跟踪，优化，校准
- Performance optimization 性能优化
- Presistence 持久化
- Resource pooling 资源池
- Synchronization 同步
- Transactions 事务

AOP的带来的好处

- 降低模块耦合度
- 使系统容易扩展
- 延迟设计决定:使用AOP,设计师可以推迟为将来的需求作决定，因为需求作为独立的方面很容易实现
- 更好的代码复用性

## 3.AOP术语

描述切面的常用术语有通知(advice),切点(pointcut)和连接点(joinpoint)。

下面展示了AOP常用的几个概念是如何关联在一起的。

![1555578081899](D:\婕\JavaEE学习之路\Spring\picture\AOP切点和连接点)

大多数描述AOP功能的述语很不直观，尽管如此，这些述语已经成为AOP行为的组成部分。

### 3.1通知(Advice)

切面也是有目标的---—-—--它必须完成的工作。在AOP术语中，**切面的工作被称之为通知。**

通知:定义了切面是什么，何时使用，其描述了切面要完成的工作，还解决了合适执行这个工作的问题。

Spring切面可以应用5种类型的通知

- 前置通知(Before): 在目标方法被调用之前调用通知功能； 
- 后置通知(After)： 在目标方法完成之后调用通知，此时不会关心方法的输出是什么； 
- 返回通知(After-returning)：在目标方法成功执行之后调用通知； 
- 异常通知(After-throwing): 在目标方法抛出异常后通知调用； 
- 环绕通知(Around)：通知包裹了被通知的方法，在被通知的方法通知之前和调用之后执行自定义的行为。 

### 3.2连接点(Join Point)

连接点：：是在应用执行过程中能够插入切面的一个点，这个点可以是方法调用时，抛出异常时，甚至修改字段时。 切面代码可以利用这些点插入到应用的正常流程之中，并添加新的行为。 

### 3.3切点(Pointcut)

切点：是缩小切面所通知的连接点的范围。如果说通知定义了切面是什么和何时的话，那么**切点就定义了何处**。

**切点的定义会匹配通知所要织入的一个或者多个连接点。**

通常会**使用明确的类和方法名称，或者利用正则表达式定义所匹配的类和方法名来指定这些切点**。 

### 3.4切面(Aspect)

切面:是通知和切点的结合。通知和切点共同定义了切面的全部内容——-——--—-它是什么，在何时，在何处完成其功能。

### 3.5引入(Introduction)

引入：允许我们向现有类的类添加新的方法和属性

扩展--代码

例如:我们创建一个`Auditable`通知类，该类记录了对象最后一次修改时的状态，这很简单，只需一个方法`setLastModified(Date)`,和一个实例变量来保存这个状态。然后，这个新的方法和实例变量就可以被引入到现有的类中，从而可以在无需修改这些现有类的情况下，让它们具有新方法的行为和状态。

### 3.5织入(Weaving)

织入是把切面应用到目标对象并创建新的代理对象的过程，切面在指定的连接点被织入到目标对象中。在目标对象的生命周期里有多个点可以进行织入:

- **编译器：**

  切面在目标类编译时被织入。这种方式需要特殊的编译器。AspectJ的织入编译器就是以这种方式织入切面的。

- **类加载器：**

  切面在目标类加载到JVM时被织入。这种方式需要特殊的类加载器(ClassLoader),它可以在目标类被引入应用之前增强该目标类的字节码。AspectJ5的加载织入(load-time weaving.LTW)就支持以这种方式织入切面。

- **运行期:**

  切面在应用运行的某一时刻被织入。一般情况下，在织入切面时，IoC容器会为目标对象动态创建一个代理对象。SpringAOP就是以这种方式织入切面的。 

### 3.7目标对象(Targer Object)

被一个或者多个切面所通知(Advice)的对象，也把它叫做被通知的对象(Advice)。既然SpringAOP是通过运行代理实现的，那么这个对象永远是一个被代理(Proxied)对象

## 4.Spring对AOP的支持

### 4.1SpringAOP支持方式

- 基于代理的经典AOP支持
- 纯POJO切面
- @AspectJ注解驱动切面
- 注入式Aspect切面(适用于Spring各个版本)

前三种都是SpringAOP的实现变体，SpringAOP构建在动态代理基础上，因此Spring对AOP的支持局限于**方法拦截**。Spring经典的AOP直接使用**ProxyFactoryBean**，这是比较笨重和繁琐的方式。现在Spring提供了更简洁和干净的**面向切面编程方式**，引入简单的声明式和基于注解的AOP。 

### 4.2SpringAOP的特点

- Spring通知是使用Java编写
- Spring在运行时通知对象
- Spring只支持方法级别的连接点  -Spring对AOP的支持局限于**方法拦截**

![1555580944650](D:\婕\JavaEE学习之路\Spring\picture\1555580944650.png)

通过在代理类中包裹切面，Spring在运行期把切面织入到Spring管理的Bean中。如上图，代理类封装了目标类， 并且拦截被通知方法的调用，再把调用转发给真正的目标Bean。当代理拦截到方法调用时，在调用目标Bean方法 过程中，会执行切面逻辑。

### 4.3使用注解创建切面

#### 4.3.1演示示例

- 添加依赖

  ```xml
  <!--Spring添加切面的依赖-->
  <dependency>
     <groupId>org.springframework</groupId>
     <artifactId>spring-aspects</artifactId>
     <version>4.3.9.RELEASE</version>
  </dependency>
  ```

- 定义切面

  ```java
  @Aspect
  @Component
  public class PayServiceAspect {
  
      //切点在PayService.pay()上
      @Pointcut(value = "execution(* com.github.soyanga.aopexample.*.pay())")
      public void payPointcut() {
  
      }
  
  //    @Pointcut(value = "args(java.lang.String)")
  //    public void StringArgs() {
  //
  //    }
  
      @Pointcut(value = "execution(* com.github.soyanga.aopexample.impl.*.*.pay(java.lang.String))")
      public void StringArgs() {
  
      }
  
      @Before(value = "StringArgs()")
      public void beaforeString() {
          System.out.println("Method has a String parameter");
  
      }
  
  
      //时间 切点
      @Before(value = "payPointcut()")
      public void beforeLog() {
          //干了什么事
          System.out.println("> before log");
  
      }
  
      @Before(value = "payPointcut()")
      public void beforeSecurity() {
          System.out.println(">> before sercurity");
      }
  
      @Before(value = "payPointcut()")
      public void beforeStartTime() {
          System.out.println(">>> before start time ");
      }
  
      @After(value = "payPointcut()")
      public void afterEndTime() {
          System.out.println(">>> afer end time");
      }
  
  
      /**
       * @param joinPoint 临界点
       */
      @Around(value = "payPointcut()")
      public Object aroundTime(ProceedingJoinPoint joinPoint) {
          //切面前面代码
          System.out.println(">>>> around befor");
  
          //(临界点)连接点方法的执行
          Object retValue = null;
          try {
              retValue = joinPoint.proceed();
          } catch (Throwable throwable) {
              throw new RuntimeException(throwable.getMessage());
          }
  
          //切面后代码
          System.out.println(">>>> around after");
          return retValue;
      }
  
      //pay方法返回时后执行
      @AfterReturning(value = "payPointcut()")
      public void afterRetuning() {
          System.out.println(">>>>> afer returning");
      }
  
      @AfterThrowing(value = "payPointcut()", throwing = "e")
      public void afterThrowing(RuntimeException e) {
          System.out.println(">>>>>> after throwing " + e.getMessage());
      }
  
  }
  
  ```

- XML配置使用切面

  在Spring的XML中装配Bean的话，需要使用Spring aop名命空间中的 `<aop:aspectj-autoproxy/>` 元素。下面是 XML配置展现如何完成该功能：

  ```xml
     <context:component-scan base-package="com.github.soyanga.aopexample.impl.case5"/>
  
      <!--扫描aspectj的注解为自动代理-->
      <aop:aspectj-autoproxy/>
  ```

  **IoC容器调用核心方法**

  ```java
  public class AopApplication {
      public static void main(String[] args) {
          ApplicationContext context = new ClassPathXmlApplicationContext("application-aop.xml");
  
  //        PayService payService = (PayService) context.getBean("alipayService");
  //        payService.pay();
  
          BankPayService bankService = (BankPayService) context.getBean("bankPayService");
          bankService.pay();
          System.out.println("----------------------------------");
          bankService.pay("hello");
      }
  }
  
  ```

  

- JavaConfig配置使用切面

  ```java
  @Configuration
  @ComponentScan(basePackages = "com.github.soyanga.aopexample.impl.case5") //扫描包
  @EnableAspectJAutoProxy  //相当于appliction-aop.xml中添加扫描的aop的注解
  public class AopJavaConfigApplication {
      public static void main(String[] args) {
          ApplicationContext context = new AnnotationConfigApplicationContext(AopJavaConfigApplication.class);
  
          PayService payService = (PayService) context.getBean("weixinPayService");
          payService.pay();
      }
  
  }
  
  ```

- 程序运行结果

  ```java
  >>>> around befor
  > before log
  >> before sercurity
  >>> before start time 
  WeixinPayService
  >>>> around after
  >>> afer end time
  >>>>> afer returning
  ```

#### 4.3.2切点表达式

Spring借助AspectJ的**切点表达式**语言定义Spring切面

- arg():限制连接点匹配参数为指定类型的执行方法
- @args():限制连接点匹配参数由指定注解标注的执行方法
- execution():用于匹配时连接点的执行方法
- this():限制连接点匹配AOP代理的bean引用为指定类型的类
- target：限制连接点匹配目标对象为指定类型的类
- @target：限制连接点匹配特定的执行对象，这些对象对应得类要具有指定类型的注解
- within()：限制连接点匹配指定的类型
- @within()：限制连接点匹配指定注解所标注的类型(当使用Spring AOP时，方法定义在由指定的注解所标注的类里)
- @annotation()：限定匹配带有指定注解的连接点

#### 4.3.3切面注解(通知+切点)

Spring使用AspectJ注解来声明通知方法 value=“pointcut”

- @After：通知方法会在目标方法返回或者抛出异常后调用
- @AfterReturning：通知方法会在目标方法返回后调用
- @AfterThrowing：通知方法会在目标方法抛出异常后调用
- @Around：通知方法会在目标方法封装起来
- @Before：通知方法会在目标方法调用之前执行

### 4.4在XML中声明切面

#### 4.4.1演示示例

我们把PayAspect中的切面相关的注解都去掉，在XML中配置：

```xml
<context:component-scan base-package="com.github.soyanga.aopexample.impl.case5"/>
    <aop:config>
        <!--切点表达式-->
        <aop:pointcut id="payPointcut" expression="execution(* com.github.soyanga.aopexample.PayService.pay())"/>
        <aop:aspect ref="payServiceAspect">
            <aop:before method="beforeLog" pointcut-ref="payPointcut"/>
            <aop:before method="beforeSecurity" pointcut-ref="payPointcut"/>
            <aop:around method="aroundTime" pointcut-ref="payPointcut"/>
        </aop:aspect>
    </aop:config>
</beans>
```

​	

```java
public class AopXMLApplication {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-aop2.xml");
        PayService service = (PayService) context.getBean("weixinPayService");
        service.pay();
    }
}
```





#### 4.4.2配置元素

Spring的AOP配置元素能够以**非侵入性**的方式声明切面 

- aop:aspectj-autoproxy : 启用@AspectJ注解驱动的切面 

-  aop:conﬁg : 顶层的AOP配置元素。大多数的aop:*元素必须包含在aop:conﬁg元素内 
- aop:pointcut : 定义一个切点 
- aop:aspect : 定义一个切面 
- aop:advisor : 定义AOP通知器 
- aop:after : 定义AOP后置通知 
- aop:after-returning : 定义AOP返回通知 
- aop:after-throwing : 定义AOP异常通知 
- aop:around : 定义AOP环绕通知 
- aop:before : 定义一个AOP前置通知

### 4.5AOP开发步骤

- 抽取横切:分析需求，提取出横切关注点
- 实现横切：独立实现横切关注点
- 切面组装：配置横切关注点与核心关注点的关系

### 4.6AOP设计考虑

- **是否拦截字段**

SpringAOP**仅支持方法拦截**。通过AOP来修改属性不是一个好的做法，因为属性通常应该在类的内部进行访问，它体现了类的封装性，如果拦截字段并修改就会破坏这种封装。

- **创建更多的切面**

或许我们会认为AOP特别强大而到处使用，以至于一个方法被调用时，都难以说清楚究竟是谁执行了那些代码，这是很可怕的，也是典型的**过度设计**。

通常如下情况才会考虑设计成切面：

1. **大部分模块都需要使用的通用功能，包括系统或者模块级别**
2. **预计目前的是实现会在今后进行功能扩展的可能性很大的地方。**

- **正交性**

如果多个切面互相影响，造成一些无法预测的结果，该怎么办?

如果多个切入点的功能实现叠加，甚至造成错误，又该怎么办？

对于这种情况，在设计AOP的时候要特别注意，要最**遵循连接点的正交模型**：不同种类的连接点和不同种类的实现应该能够以任何顺序组合使用。

换句话说，请**保持你设计的方面的独立性，功能实现的独立性，不依赖于多个方面的执行先后顺序。切面不能依赖其他切面。**

*假如并未遵循连接点正交模型的化，当修改一个切面或者添加一个切面时，就会导致程序发生难以预料的错误。*

- **对粗粒度对象使用AOP**

AOP通常用来对粗粒度对象进行功能增强，比如对业务逻辑对象，拦截某个业务方法，进行功能增强，从而提高系统的可扩展性。

但是需要注意不要在细粒度的对象上使用AOP，比如对某个实体描述对象。在运行时，这种细粒度对象通常实例很多，比如可能有多条数据，这种情况下使用AOP会有大量的方法拦截带来的反射处理，严重影响性能。

*eg:Spring类（细粒度），假如给Spring类某个方法中织入切面进行拦截的化，则在整个项目中但凡用到Spring这个方法时，则就会导致AOP的调用,整个项目中用了很多的Spirng类的方法则会导致性能严重下降。*

## 总结

| 知识块     | 知识点                              |
| ---------- | ----------------------------------- |
| AOP思想    | 1.AOP概念和思想                     |
| AOP实现    | 1.AOP各种不同的实现(静态，动态代理) |
| Spring AOP | 1.SpringAOP的使用  2.AOP设计        |

