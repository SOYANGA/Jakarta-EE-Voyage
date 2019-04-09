# Mybatis的配置

**重点**

==**了解掌握Mybatis的配置**==

==**掌握Mybatis的environments配置**==

## 1.XML配置

### 1.1XML结构

Mybatis的配置文件包含了会影响MyBatis行为的设置(settings)和属性(properties)信息。其中配置文件的顶层结构如下：

- configuration 配置
  - properties 属性
  - settings 设置
  - typeAliass 类型别名
  - objectFactory 对象工厂
  - plugins 插件
  - **environments 环境集合**
    - environment 环境
      - transactionManager 事务管理
      - dataSource 数据源
  - databaseldProvider 数据库厂商标识
  - **mappers 映射器**

### 1.2属性(properties )

这些属性可外部配置且课动态替换的，既可以在典型的java属性文件中配置，亦可通过properties元素的子元素来传递。例如:

*k-v格式*

```xml
<properties resource="config/database.properties">
    <property name="username" value="dev_user"/>
     <property name="password" value="F2Fa3!33TYyg"/>
</properties>
```

其中属性就可以在整个配置文件中使用，来替换需要动态配置属性值。比如：

```xml
<dataSource type="POOLED">
    <property name="driver" value="${driver}"/>
    <property name="url" value="${url}"/>
    <property name="username" value="${username}"/>
    <property name="password" value="${password}"/>
</dataSource>
```

这个例子中的username和password将会由properties元素中设置的相应值来替换。driver和url属性将会由config.propertied文件中对应得值来替换。这样就为配置提供了

属性也可以被传递到SqlSessionFactoryBuilder.build()方法中。例如:

```java
SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader,props);
//...or...
SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader,environment,props);
```

如果属性在不只一个地方进行了配置，那么MyBatis将按照下面得顺序来加载。

- 在properties元素体内指定得属性首先被读取。

- 然后根据properties元素中得resource属性读取类路径下属性文件或根据url属性指定得路径读取属性文件，并覆盖以读取得同名属性。

- 最后读取作为方法参数传递的属性，并覆以读取的同名属性。

  **因此，通过方法参数传递的属性具有最高优先级，resource/url属性中指定的配置文件次之，最低优先级的是properties属性中指定的属性。** *（最先读取的属性的会被后来读取同名属性的方式覆盖)*

从Mybatis 3.4.2开始，你可以为占位符指定一个默认值。例如：

```xml
<dataSource type="POOLED">
<!--如果username的key不存在的话就使用ut_user作为username的值-->
    <property name = "username" value="${username:ut_user}"/>
</dataSource>
```

这个特性默认是关闭的。如果你想为占位符指定一个默认值，你应该添加一个指定的属性来开启这个特性

```xml
<properties resource="config/database.properties">
    <!--属性解析启动默认值-->
    <property name="org.apache.ibatis.parsing.PropertyParser.enable-default-value" value="true"/>
</properties>
```

### 1.3 settings(核心配置)

这时Mybatis中极为重要的配置，他们会改变Mybatis的运行时行为。下表描述了设置中各项的意图，默认值等。

| 设置参数                         | 描述                                                         | 有效值               | 默认值                   |
| -------------------------------- | ------------------------------------------------------------ | -------------------- | ------------------------ |
| cacheEnabled                     | 该设置影响所有映射器中配置的缓存全局开关                     | true\|false          | true                     |
| lazyLoadingEnabled               | 延迟加载的全局开关。当开启时，所有关联对象都会延迟加载。特定关联关系中可通过设置`fetchType`属性来覆盖该项开关状态 | true\|false          | false                    |
| aggresslveLazyLoading            | 当开启时，任何方法的调用都会加载该对象的所有属性。否则，每个属性会按需要加载（参考`lazyLoafTriggerMethodes`） | true\|false          | false(true in  <= 3.4.1) |
| mulitipleResultSetsEnabled       | 是否允许单一语句返回多结果集(需要兼容驱动)                   | true\|false          | true                     |
| useColumnLabel                   | 使用列标签代替列名。不同驱动载这方面会有不同的表现，具体可参考相关驱动文档或者通过测试这两种不同的模式来观察所有驱动的结果 | true\|false          | true                     |
| useGeneratedKeys                 | 允许JDBC支持自动生成主键，需要驱动兼容。如果设备为true则这个设置强制使用自动生成主键，尽管一些驱动不能兼容但仍可以正常工作（比如Derby） | true\|false          | false                    |
| autoMappingUnknownColumnBehavior | - 指定发现自动映射目标未知列(或者位置属性类型)的行为。`NONE`：不做任何反应   `WARNING`：输出提醒日志`FAILING`：映射失败（抛出`SqlSessionExection`） | NONE,WARNING,FAILING | NONE                     |
| autoMappingBehavior              | 指定Mybatis应如何自动映射列到字段或者属性。NONE表示取消自动映射；PARTIAL只会自动映射没有定义嵌套结果集映射的结果集。FULL会自动映射任意复杂的结果集(无论是否嵌套) | NONE，PARTIAL,FULL   | PARTIAL                  |
| defaultExecutorType              | 配置默认的执行器，SIMPLE就是普通的执行器；REUSE执行器会重用预处理语句(perpared statements) :BATCH执行器将重用语句并执行批量更新。 | SIMPLE REUSE BATCH   | SIMPLE                   |



| localCaheScope | Mybatis利用本地缓存机制(Local Cache)防止循环引用(circular references)和加速重复嵌套查询。默认值为SESION，这种情况下会缓存一个会话执行的所有查询。若设置为STATEMENT，本地会话仅用在语句执行上，对相同SqlSession的不同调用将不会共享数据 | SESSION  STATEMENT | SESSION |
| -------------- | ------------------------------------------------------------ | ------------------ | ------- |
|                |                                                              |                    |         |

完整配置如下：

```xml
<settings>
    <setting name="cacheEnabled" value="true"/>
    <setting name="lazyLoadingEnabled" value="true"/>
    <setting name="mutipleResultSetsEnbale" value="true"/>
    <setting name = "useColumnLabel" value="true"/>
    <setting name="useGeneratedkeys" value = "false"/>
    <setting name="atuoMapingBehavior" value="PARTIAl"/>
    <setting name="autoMappingUnknownColumnBehavior" value="WARNING"/>
    <setting name="defaultExecutorType" value="SIMPLE"/>
    <setting name="defaultFetchSize" value="100"/>
    <setting name="defauktStatementTimeout" value="25"/>
    <setting name="safeRowBoundsEnable" value="SESSION"/>
    <setting name="mapUnderscoreTocamelCase" value="false"/>
    <setting name="localCacheScope"  value="SESSION"/>
    <setting nme="jdbcTypeForNull" value="OTHER"/>
    <setting name="lazyLoadTriggerMenthods" value="equals,clone,hashCode,toString"/>
</settings>
```

#### 1.3.1配置日志

- 日志框架：[SLF4j ](https://www.slf4j.org/manual.html)的 Logback实现

```xml
<!--日志框架-->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.25</version>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.2</version>
        </dependency>
```

- 配置日志

  ```xml
      <!--mybatis配置，主要影响mybatis的运行行为-->
      <settings>
          <setting name="logImpl" value="SLF4j"/>
      </settings>
  ```

  

SLE4j的实现类 ->Logback

### 1.4 类型别名(typeAliases)

类型别名是为java类型设置一个短的名字。它只和XML配置有关，存在的意义仅在于用来减少类的完全限定名。

#### 1.4.1默认内建类型别名

Mybatis框架已经为许多常见的java类型内建了相应的类型别名。他们都是大小写不敏感的，需要注意的是由基本类型名称重复导致的特殊处理。

| 别名     | 映射的类型 |
| -------- | ---------- |
| _byte    | byte       |
| _long    | long       |
| _short   | short      |
| _int     | int        |
| _integer | int        |
| _double  | double     |
| _float   | float      |
| _boolean | boolean    |
| string   | String     |

| 别名    | 映射的类型 |
| ------- | ---------- |
| byte    | Byte       |
| long    | Long       |
| short   | Short      |
| int     | Integer    |
| integer | Integer    |
| double  | Double     |
| float   | Float      |
| date    | Date       |
| decimal | BigDecimal |

| 别名       | 映射的类型 |
| ---------- | ---------- |
| bigdecimal | BigDecimal |
| object     | Object     |
| map        | Map        |
| hashmap    | HashMap    |
| list       | List       |
| arraylist  | ArrayList  |
| collection | Collection |
| iterator   | Iterator   |

#### 1.4.2自定义类型别名

例如：

```xml
    <typeAliases>
        <typeAlias alias="ScottGroup" type="com.soyanga.mybatis.entity.ScottGroup"/>
        <typeAlias alias="ScottInfo" type="com.soyanga.mybatis.entity.ScottInfo"/>
        <typeAlias alias="ScottShare" type="com.soyanga.mybatis.entity.ScottShare"/>
    </typeAliases>

```

当这样的配置时，ScottGroup可以用在任何使用com.soyanga.mybatis.emtity.ScottGroup的地方。也可以指定一个包名，MyBatis会在包名下搜索需要的Java Bean,同时默认使用类名小写作为别名，比如：

```xml
    <!--配置类的别名-->
    <typeAliases>
        <!--指定一个包名，别名默认为类名的全小写-->
        <package name="com.soyanga.mybatis.entity"/>
    </typeAliases>
```

> 注意：由于IDE工具的智能化，定义类型的别名使用的频率较低

### 1.5 类型处理器(typeHandlers)

无论是MyBatis在预处理语句(PreparedStatement)中**设置一个参数时，还是从结果集中取出一个值时**，都会用类型处理器将获取的值以适合的方式转换成Java类型。

#### 1.5.1默认的类型处理器

Mybatis框架默认提供了多种类型的处理器，基本涵盖了java中使用到的各种数据类型。

#### 1.5.2 自定义类型处理器

Mybatis为用户提供了重写类型处理器或创建自己的类型处理器来处理不支持的或非标准的类型。

具体做法：实现 `org.apache.ibatis.type.TypeHandler`接口，或继承一个很便利的类`org.apache.ibatis.type.BaseTypeHandler`,然后可以选择性的将它映射到一个JDBC类型。

实现示例：

```java
//ExampleTypeHandler.java
@MappedJdbcTypes(jdbctype.VARCHAR)
@MApperTypes(value = String.class)
public class MyStringTypeHandler extends BaseTypeHandler<String> {


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter);
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getString(columnIndex);
    }
}
```

创建的MyStringHander会覆盖已经存在的处理java的String类型属性和VARCHAR参数及结果的类型处理器。

MyBatis不会窥探数据库元信息来决定使用那种类型，所以你必须在参数和结果映射中指明那是VARCHAR类型的字段，以使其能够绑定到正确的类型处理器上。这是因为：Mybatis直到语句被执行才清除数据类型

通过类型处理器的泛型，MyBatis可以得知该类型处理器的处理java类型，同时可以通过修改配置或者添加注解的方式来改变处理器的处理类型

- 第一种：在类型处理器的配置元素（typeHandler element）上增加一个javaType属性（比如：javaType=“Stirng”）和jdbcType属性（比如： jdbcType = “VARCHAR”）;

- 第二种：在类型处理器的类上(TypeHandler class)增加@MappedTyped注解来指定与其关联的java类型列表；@MappedJdbcTypes注解来只当与其关联的JDBC类型。

  > 如果在配置中指定了javaTyped或者jdbcType属性，则注解方式将被忽略
  >
  > 建议：选择一种方式来配置类型处理器（配置或者注解）

- 配置类型处理器

  - 第一种：配置处理器类

    ```xml
    <typeHandlers>
        <typeHandler handler="com.soyanga.mybatis.plugin.MyStringTypeHandler" jdbcType="VARCHAR" javaType="string"/>
    </typeHandlers>
    ```

  - 第二种：自动检索（autodiscovery）

    ```xml
    <!--类型处理器-->
    <typeHandlers>
    <!--多个类型处理器的加载-->
            <package name="com.soyanga.mybatis.plugin"/>
    </typeHandlers>
    ```

    > 使用自动检索配置功能的时候，只能通过注解的方式来指定JDBC和Java类型

#### 1.5.3泛型类型处理器（了解）

用户可以创建一个泛型类型处理器，它可以处理多于一个类。为了达到此目的，需要增加一个接收类作为参数的构造器，这样在构造一个类型处理器的时候Mybatis就会传入一个具体的类。

```java
//GenericTypeHandler.java
public class GenericTypeHandler<E extends MyObject> extends
    BaseTypeHandler<E>{
    private Class<E> type;
    
    public GenericTypeHandler(Class<E> type){
        if(tyep = null){
            throw new IllegalArgumentException("Type argument count be null");
        }
        this.type = type;
        }
    }
}
```

`EnumTypeHandler`和`EnumOrdinalTypeHandler`都是泛型类型处理器(genericTypeHandlers)

### 1.6对象工厂(ObjectFactory)

MyBatis每次**创建结果对象**的新实例时，他都会使用一个对象工厂（ObjectFactory）实例来完成。默认的对象工厂需要做的仅仅时实例化目标类，要么通过默认构造方法，要么在参数映射存在的时候通过参数构造方法来实现实例化。如果想覆盖对象工厂的默认行为，则可以通过创建自己的对象工厂来实现。比如：

```java
//ExampleObjectFactory.java
public class ExampleObjectFactory extends DefaultObjectFactory{
    public Object create(Class type){
        return super.create(type);
    }
    /**
    *type：对象类型
    *constructorArgTypes：参数类型集合
    *construstorArgs：参数集合
    */
    public Object create(Class type,List<Class> constructorArgTypes,List<Object> construstorArgs){
        return super.create(type,constructorArgTypes,constructorArgs);
    }
    /**
    *配置元素的属性包装成Properties对象，再转换为对象的属性
    *
    */
    public void setProperties(Properties properties){
        super.setProperties(properties);
    }
    public<T> bollean isCollection(Class<T> type){
        return Collection.class.isAssignableFrom(type);
    }
}

<!--mybatis-config.xml-->
    <!--对象工厂-对象的构建-->
    <!--<objectFactory type="org.apache.ibatis.reflection.factory.DefaultObjectFactory">-->
    <objectFactory type="org.mybatis.example.ExampleObjectFactory">
        <!--name属性名 value属性个数-->
        <property name="abc" value="1"/>
        <property name="def" value="2"/>
    </objectFactory>
```

ObjectFactory接口很简单，它包含两个创建用的方法，一个是处理默认构造方法的，另外一个是处理带参数的构造方法的。最后setProperties方法可以被用来配置ObjectFactory 在初始化你的ObjectFactory实例后，ObjectFactory元素中定义属性会被传递给setProperties方法。

### 1.7插件(plugins)

MyBatis允许你在已映射语句执行过程中得某一点进行拦截调用。默认情况下，MyBatis允许使用插件来拦截得方法调用包括：

- `statementHandler`的`perpare,parameterize,batch,update,query`方法 创建sql命令
- `ParamentHandler`的`getParameterObject，setParameters`方法 传入参数处理
- `Executor`的`update,query,flushStatements,commit,rollback,getTransaction,close,isClosed`方法  执行器
- `ResuitSetHandler`的`handleResultSets,handleOutputParameters`方法 结果处理

这些类中方法的细节可以通过查看每个方法的签名来发现，或者直接查看MyBatis的发行包中的源码。假设你想做的不仅仅是监控方法的调用，那么你应该很好的了解正在重写的方法的行为。因为如果在试图修改或者重写已有方法的行为的时候，你很可能在破坏MyBatis的核心模块。这些都是很底层的类和方法，所以**使用插件的时候要特别的当心。**

通过MyBatis提供的强大机制，使用插件是非常简单的，只需实现Interceptor接口，并指定了想要拦截的方法签名即可。

> 拦截器从一些列的MyBatis的执行流程中修改某些流程。

```java
//ExamplePlugin.java
@Intercepts({@Signature(
	type = Executor.class,
	method = "update",
	args = {MappedStatement.class, Object.class})})
public class ExamplePlugin implement Interceptor{
    public Object intercept(Invocation invocation)throws Throwable{
        return invocation.proceed();
    }
    public object plugin(Object target){
        return Plugin.wrap(target,this);
    }
    public void setProperties(Properties properties){
        
    }
}

<!-- mybatis-config.xml -->
    <plugins>
    	<plugin interceptor="org.mybatis.example.ExamplePlugin">
    		<property name = "somProperty" value="100"/>
    	</plugin>
    </plugins>
```

上面的插件将会拦截在Executor实例中所有的update方法调用，这理的Executor是负责执行底层映射语句的内部对象。

### 1.7配置环境(environments)

MyBatis可以配置成适应多种环境，这种机制有助于将SQL映射应用于多种数据库中，现实情况下有多种理由需要这么做。例如：开发，测试和生产环境需要有不同的配置；或者共享相同Schema的多个生产数据库使用相同的SQL映射。

> 尽管可以配置多个环境，每个SqlSessionFactory实例只能选择其一，**每个数据库对应一个SqlSessionFactory实例**

为了指定创建哪种环境，只要将它作为可选参数传递给SqlSessionFactoryBuilder即可。可以接受环境配置的两个方法签名是：

方法参数优先级是最高的

```java
sqlSexxionFactory factory  = new SqlSessionFactoryBuilder().build(reader,environment);
SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader,environment,properties);
```

如果忽略了环境参数，那么默认环境将会被加载，如下所示：

```java
sqlSexxionFactory factory  = new SqlSessionFactoryBuilder().build(reader);
SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader,properties);
```

环境元素定义了如何配置环境：

```xml
<environments default="development">  //默认的环境配置
        <environment id="development">
            <transactionManager type="JDBC">
            </transactionManager>
            <dataSource type="POOLED">
                <property name="driver" value="${driver:com.mysql.jdbc.Driver}"/>
                <property name="url" value="jdbc:mysql://localhost:3306/scott"/>
                <property name="username" value="${username:root}"/>
                <property name="password" value="${password:123456789}"/>
            </dataSource>
        </environment>
        <environment id="prod">
            <transactionManager type="JDBC"> //事务管理
            </transactionManager>
            <dataSource type="POOLED">
                <property name="driver" value="${driver:com.mysql.jdbc.Driver}"/>
                <property name="url" value="jdbc:mysql://localhost:3306/scott"/>
                <property name="username" value="${username:root}"/>
                <property name="password" value="${password:123456789}"/>
            </dataSource>
        </environment>
    </environments>
```

注意这里的关键点：

- 默认的环境ID（比如：default=“development”）
- 每个environment元素定义的环境ID(比如：id = “development”)
- 事务管理器配置（比如：type=“JDBC”）
- 数据源的配置（比如：type=“POOLED”）

默认的环境和环境ID是可以随意命名的，只要保证默认环境要匹配其中一个环境ID。

#### 1.7.1事务管理

在MyBatis中两种类型的事务管理器（也就是 type=“[JDBC | MANAGED]”）:

- JDBC-这个配置就是直接使用JDBC的提交回滚设置，它**依赖从数据源得到的连接**来管理事务作用域。

  ```java
  SqlSession sqlSession = sqlSessionFactory.openSession(true); //true是自动提交  反之需要自己提交
  ```

- MANAGED-这个配置几乎没做什么。它从来不提交或回滚一个连接，而是让容器来管理事务的整个生命周期（比如JEE应用服务器的上下文tomcat,jetty,jboss），默认情况下会关闭连接，然后一些容器并不希望这样，因此需要将closeConnection属性设置为false来阻止它默认的关闭行为，例如：

```xml
<transactionManager type="JDBC">
    <property name="closeConnection" value="false"/>
</transactionManager>
```

> 使用Spring+MyBatis，则没有必要配置事务管理器，因为Spring模块会使用自带的管理器来覆盖前面的配置

#### 1.7.2数据源（dataSource）

dataSource元素使用标准的JDBC数据源接口来配置JDBC连接对象的资源，许多MyBatis的应用程序将会按示例子中来配置数据源。然而它并不是必须的。要知道为了方便使用延迟加载，数据源才是必须的。

MyBatis有三种呢见数据源类型 (也就是 type = “[**UNPOOLED|POOLED|JNDI**]”):

- **UNPOOLED**:这个数据源的是实现*只在每次被请求时打开 和 关闭连接*。虽然有一点慢，它对在及时可用连接方面没有性能要求的简单应用程序是一个很好的选择，不同的数据库在这方面表现也是不一样的，所以对某些数据库来说使用连接池并不重要，这个配置也是理想的。UNPOOLED类型的数据源仅仅需要配置以下5种属性：

  | 属性                             | 描述                                                         |
  | -------------------------------- | ------------------------------------------------------------ |
  | driver                           | 这是JDBC驱动的java类的完全限定名（并不是JDBC驱动中可能包含的数据源类型） |
  | url                              | 这是数据库的JDBC URL地址                                     |
  | username                         | 登陆数据库的用户名                                           |
  | password                         | 登陆数据库的密码                                             |
  | defaultTransactionIsolationLevel | 默认的连接事务隔离级别                                       |
  | driver.*                         | 作为可选项，可以传递属性给数据库驱动，例如：driver.encoding=UTF8 |

  这将通过DriverManager.getConnection(url,driverProperties)方法传递值为UTF8的encoding属性给数据库驱动。

**POOLED**-这种数据源的实现利用“池”的概念将JDBC连接对象组织起来，避免了创建新的连接实例所必须的初始化和认证时间。这是一种使得并发**Web应用快速响应请求的流行处理方式**。

除了上述提到UNPOOLED下的属性外，会有更多属性来配置POOLED的数据源：

| 属性                                   | 描述                                                         |
| -------------------------------------- | ------------------------------------------------------------ |
| poolMaximumActiveConnections           | 在任意时间可以存在的活动（也就是正在用）连接数量，默认值：10 |
| poolMaximumldleConnections             | 任意时间可能存在的空闲连接数                                 |
| poolMaximumCheckoutTime                | 在被强制返回之前，池中连接被检出（out）时间，默认值:20000毫秒 |
| poolTimeToWait                         | 这是一个底层设置，如果获取连接花费很长时间，他会给连接池打印状态日志，并再次尝试获取一个连接（避免误配置的情况下一致安静的失败），默认值：20000毫秒 |
| poolMaximumLocalBadConnectionTolerance | 这是一个关于坏链接容忍度的底层设置，对于每一个尝试从缓存池获取连接的线程，一个线程获取到的是一个坏链接，那么数据源允许这个线程尝试重新获取一个新的连接，但是这个重新尝试的次数不应该超过poolMaximumldleConnections与poolMaximumLocalBadConnectionTolerance之和。默认值：3（since:3.4.5） |
| pollPingQuery                          | 发送到数据库的侦测查询，用来检验连接处在正常工作秩序中并接受请求是“NO PING QUERY SET”,这回导致多个数据驱动失败时带有一个恰当的错误信息 |
| pollPingEnabled                        | 是否启用侦测查询。若开启，也必须使用可执行的SQL语句设置pollPingQuery（最好是一个非常快的SQL）默认值true |
| poolPingConnectionsNotUsedFor          | 配置pollPingQuery的使用频度，这可以设置成具体的数据库连接超时时间，避免不必要的侦测，默认值为0（即所有连接时刻被侦测-当然仅当pollPingEnabled为true时使用） |

**JNDI(Java Naming and Directory Interface)** - 这个数据源的实现是为了能在EJB或应用服务器这类容器中使用，容器可以集中或在**外部配置数据源**，然后放置一个JNDI上下文的引用。这种数据源配置只需要两个属性

| 属性            | 描述                                                         |
| --------------- | ------------------------------------------------------------ |
| initial_context | 这个属性用来在 initialContext中寻找上下文（即，initialContext.lookup(initial_context))。这是个可选属性，如果忽略，那么data_source属性将会直接从initialContext中寻找 |
| data_source     | z这是引用数据源实例位置的上下文的路径。提供了initial_context配置时会在其返回的上下文中进行查找，没有提供时则直接在initialContext中查找 |
| env.*           | h和其他数据源配置类似，可以通过添加前缀“env.”直接把属性传递给初始上下文，例如：env.encoding=UTF8 |

### 1.8映射器(mappers)

MyBatis的行为已经由上述元素配置完了，我们现在就要定义SQL映射语句了。但是首先我们需要告诉MyBatis到哪里区找这些语句。Java在自动查找这个方面没有提供一个很好的方法，最佳方式是告诉MyBatis到哪里区找映射文件。你可以使用想到一类路径的资源引用，或完全限定资源定位符（包括file:///的URL），或类名和包名等。例如：

- 加载类路径下的映射器配置文件

  ```xml
  <!--使用 classpath相对资源 -->    
  <mappers>
          <mapper resource="mapper/ScottGroupMapper.xml"/>
  </mappers>
  ```

- 加载全路径下的映射器配置文件

  ```xml
  <!--使用url路径-->
  <mappers>
          <mapper url="file"///var/mappers/ScottGroupMapper.xml/>
  </mappers>
  ```

- 加载指定包下的Mapper接口类

  ```xml
  <!--注册一个包下的所有mapper接口-->
  <mappers>
       <package name=com.soyanga.mybatis.mapper"/>
  </mappers>
  ```



```java
public interface ScottGroupMapper {

    @Select(value = {"select * from dept;"})
    List<ScottGroup> query(@Param("id") String id); //参数注解

    int insertScottGroup(ScottGroup scottGroup);
}
```

注解由局限性：虽然便利但是，不能完全支持MyBatis配置。且破坏了SQL的 隔离/集中处理。

