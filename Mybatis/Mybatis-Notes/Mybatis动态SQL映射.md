# Mybatis动态SQL映射

> 重点：
>
> - 理解Mybatis的动态SQL映射原理
> - 掌握Mybatis的动态SQL映射编写

## 1.动态SQL简介

MyBatis的强大特性之一就是他的动态SQL。JDBC中根据不同条件拼接SQL语句繁琐。利用动态SQL就可以摆脱这种痛苦。

通常动态SQL不可能是独立的一部分，MyBatis当然使用一种强大的动态SQL语言来改进这种情形，这种语言可以被用在任意SQL映射语句中。

动态SQL元素和使用JSTL(JSP标准标签库)或其他类似基于XML的文本处理器相似。在MyBatis之前版本中，需要了解很多元素，现在MyBatis3大大提升了元素，用不到原先一半元素就可以了。MyBatis采用功能强大的基于**OGNL**（Object Graphic Navigation Language 对象图导航语言） 的表达式来消除其他元素。

动态SQL映射使用标记：

- if
- choose(when,otherwise)
- trim(where,set)
- foreach

## 2.动态SQl标记

### 2.1if

动态SQL通常要做的事情是有条件地包含where子句地一部分。比如：

```xml
<select id="queryScottEmpWhithdeptnoAndJob" resultMap="scottEmpMap">
    select
    <include refid="emp_column"/>
    from emp
    where deptno=20
    <if test="job!=null and jon != '' " >
         and job like #{job}
    </if>
</select>    
```

多重if判断  and

```xml
<select id="queryScottEmpWhithdeptnoAndLikeJobAndLikeEname" resultMap="scottEmpMap">
     select
     <include refid="emp_column"/>
     from emp
     where deptno=20
     <if test="job!=null and job!= '' ">
         and job like #{job}
     </if>
     <if test="ename!=null">
         and ename like #{ename}
     </if>

</select>
```

### 2.2where

```xml
<select id="queryScottEmpWhithdeptnoAndLikeJobAndLikeEname2"
      resultMap="scottEmpMap">
      select
      <include refid="emp_column"/>
      from emp

   <where>
       <if test="deptno!=null">
                deptno = #{deptno}
       </if>

        <if test="job!=null and job!= '' ">
            and job like #{job}
        </if>

         <if test="ename!=null">
             and ename like #{ename}
         </if>
    </where>

</select>
```

### 2.3choose,when,otherwise

有些时候，我们不想用条件语句，而只是想从中择出一点。针对这种情况，MyBatis提供了choose元素，它有点像Java中的switch语句

还是如上的例子，但是这次变为提供了“job”就按“job”查找，提供了”ename”就按“ename”查找，若两者都没有，就按业务情况返回所有deptno = 20的且ename是

`JACK`的职务表信息。

```xml
    <!--choose when otherwise 相当于SwitchCase case default-->
    <select id="queryScottEmpWhithDeptnoAndLikeJobOrEname"
            resultMap="scottEmpMap">
        select
        <include refid="emp_column"/>
        from emp
        <where>
            deptno=20
            <choose>
                <when test="job!=null">
                    and job = #{job}
                </when>
                <when test="ename!=null">
                    and ename = #{ename}
                </when>
                <otherwise>

                </otherwise>
            </choose>
        </where>

    </select>
```



### 2.3trim,where,set

前面几个例子已经合宜的解决了经典的动态SQL问题，现在考虑回到if示例，这次我们将` deptno= 20`也设置为动态的条件或者去掉看看会发生什么。

会在解析SQL语句时出现SQL语句异常，所以我们此时需要`where`来解决此问题，它会将where中包括的语句适当的增加删除某些多余的SQL命令，使得达到开发者预期的情况，比如去掉多余的 and，当没有符合条件时，where语句不执行。

```xml
    <select id="queryScottEmpWhithdeptnoAndLikeJobAndLikeEname2"
            resultMap="scottEmpMap">
        select
        <include refid="emp_column"/>
        from emp

        <where>
            <if test="deptno!=null">
                deptno = #{deptno}
            </if>

            <if test="job!=null and job!= '' ">
                and job like #{job}
            </if>

            <if test="ename!=null">
                and ename like #{ename}
            </if>
        </where>

    </select>
```

where元素直到只有在一个以上if条件有值时才会添加where子句，而且，若最后内容是“AND”或者“OR”开头的，where元素也知道如何将他们去除。

如果where元素不正常处理，我们还可以通过trim元素来定制我们想要的功能。比如，和where元素等价的自定义trim元素为：

```xml
<trim prefix="where" prefixOverrides="AND |OR">
...
</trim>
```

prefixOverrides 属性会忽略通过管道分隔的文本序列（注意此例中的空格也是必要的）。其功能就是在所有prefixOverrides 属性指定的内容将被移除，并且插入prefix属性中指定的内容。

```xml
    <select id="queryScottEmpWhithdeptnoAndLikeJobAndLikeEname2"
            resultMap="scottEmpMap">
        select
        <include refid="emp_column"/>
        from emp

        <!--<where>-->
        <!--<if test="deptno!=null">-->
        <!--deptno = #{deptno}-->
        <!--</if>-->

        <!--<if test="job!=null and job!= '' ">-->
        <!--and job like #{job}-->
        <!--</if>-->

        <!--<if test="ename!=null">-->
        <!--and ename like #{ename}-->
        <!--</if>-->
        <!--</where>-->

        <!--将where后的语句中所偶有以and 或者 or结尾或开头的标签都去掉-->
        <trim prefix="where" prefixOverrides="and | or">
            <if test="deptno!=null">
                deptno = #{deptno}
            </if>

            <if test="job!=null and job!= '' ">
                and job like #{job}
            </if>

            <if test="ename!=null">
                and ename like #{ename}
            </if>
        </trim>
    </select>
```

类似用于动态更新的解决方案叫做set，set元素可以被用于动态包含需要**更新**的列，而且舍去其他的。比如：舍去以，结尾的，并且当set元素不为空的元素中个数大于1时，在开头添加set 

```xml
<update id="updateScottEmpByObjectWithSet" parameterType="com.soyanga.mybatis.entity.ScottEmp">
        update emp
        <!--<trim prefix="set" suffixOverrides=",">-->
            <!--<if test="hiredate!=null">-->
                <!--hiredate = #{hiredate},-->
            <!--</if>-->
            <!--<if test="ename!=null">-->
                <!--ename = #{ename},-->
            <!--</if>-->
        <!--</trim>-->
        <set>
            <if test="hiredate!=null">
                hiredate = #{hiredate},
            </if>
            <if test="ename!=null">
                ename = #{ename},
            </if>
        </set>
        where empno = #{empno}
    </update>
```



```xml
<trim prefix="set" suffixOverrides=",">
    ...
</trim>
```

### 2.4foreach

动态SQL的另外一个常用的必要操作是需要对一个集合进行遍历，通常子构建 IN 条件语句的时候。比如：

参数类型为：list

```xml
<!--映射接口-->
List<ScottEmp> queryScottempByList(List deptnos);
<!--命令配置-->
    <select id="queryScottempByList" parameterType="list" resultMap="scottEmpMap">
        select
        <include refid="emp_column"/>
        from emp
        <where>
            deptno in
            <foreach collection="list"
                     open="(" separator="," close=")"
                     index="index" item="item">
                #{item}
            </foreach>
        </where>
    </select>
<!--测试代码-->
    @Test
    public void test_queryScottempByList() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
        List<Integer> deptnolist = new ArrayList<Integer>();
        deptnolist.add(10);
//        deptnolist.add(20);
//        deptnolist.add(30);
        List<ScottEmp> scottEmpList = scottEmpMapper.queryScottempByList(deptnolist);
        logger.info("queryScottEmpWhithDeptnoAndLikeJobOrEname  Result {}", scottEmpList);
        sqlSession.close();
    }    
```

参数类型为 ：array

```xml
<!--映射接口-->
List<ScottEmp> queryScottempByArray(Integer[] deptnos);
<!--命令配置-->
    <select id="queryScottempByArray" parameterType="integer[]" resultMap="scottEmpMap">

        select
        <include refid="emp_column"/>
        from emp
        <where>
            deptno in
            <foreach collection="array"
                     open="(" separator="," close=")"
                     index="index" item="item">
                #{item}
            </foreach>
        </where>
    </select>
<!--测试代码-->
    @Test
    public void test_queryScottempByArray() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
        List<ScottEmp> scottEmpList = scottEmpMapper.queryScottempByArray(new Integer[]{10});
        logger.info("queryScottEmpWhithDeptnoAndLikeJobOrEname  Result {}", scottEmpList);
        sqlSession.close();
    }
```

参数类型为Map

```xml
<!--映射接口-->
    List<ScottEmp> queryScottempByMap(Map deptnoMap);

<!--命令配置-->
    <select id="queryScottempByMap" resultMap="scottEmpMap">
        select
        <include refid="emp_column"/>
        from emp
        <where>
            <if test="ename!=null">
                ename like #{ename}
            </if>

            <if test="deptnos!=null">
                and deptno in
                <foreach collection="deptnos"
                         open="(" separator="," close=")"
                         index="index" item="item">
                    #{item}
                </foreach>
            </if>

        </where>
    </select>

<!--测试代码-->
    //TDD 测试驱动开发
    @Test
    public void test_queryScottempByMap() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
        Map map = new HashMap();
        map.put("ename", "%J%");
        map.put("deptnos", new Integer[]{10,20,30});
        List<ScottEmp> scottEmpList = scottEmpMapper.queryScottempByMap(map);
        logger.info("queryScottEmpWhithDeptnoAndLikeJobOrEname  Result {}", scottEmpList);
        sqlSession.close();
    }

```

foreach元素的功能非常强大，它允许你指定一个集合，声明可以用在元素体内的集合项和索引变量。它也允许你指定开辟匹配的字符串以及在迭代中间放置分隔符。这个元素是很智能的，因此他不会偶然地附加多余地分隔符。

> 注意：你可以将任何可迭代对象（如列表，集合等)和任何的字典或者数组对象传递给foreach作为集合参数。当使用可迭代对象或者数组时，index是当前迭代的次数。item的值便是本次迭代获取的元素。当使用字典（或者Map.Entry对象的集合时），index是键，item是值。
>
> collection 迭代输出 List时为list ,迭代输出数组时为array,迭代输出当使用字典（或者Map.Entry),为其中的存储的集合名称
>
> open 拼接SQL左边   item    close 拼接SQL右边  separator=“,” ：分隔符为”，“



```xml
<foreach collection="deptnos"
                         open="(" separator="," close=")"
                         index="index" item="item">
                    #{item}
</foreach>
```



## 3.扩展

### 3.1分页插件

在进行数据库应用程序开发的时候，经常会使用到各种各样的分页，这里又开源社区提供了一个分页插件([PageHelper][https://github.com/pagehelper/Mybatis-PageHelper/blob/master/README_zh.md]),能够制成任何复杂的表单，多表查询。

[使用方法][https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/HowToUse.md] ：中文的插件

- 添加分页插件依赖

  ```
  <!--Mybatis分页插件-->
          <dependency>
              <groupId>com.github.pagehelper</groupId>
              <artifactId>pagehelper</artifactId>
              <version>5.1.4</version>
          </dependency>
  ```


- 配置拦截器插件

  在MyBatis的配置文件中添加拦截器插件

  ```xml
      <!--配置PageHelper分页插件-->
      <plugins>
          <plugin interceptor="com.github.pagehelper.PageInterceptor">
              <property name="helperDialect" value="mysql"/>
          </plugin>
      </plugins>
  ```

- 使用方式

  ```java
  //第一种，RowBounds方式的调用
  List<Country> list = sqlSession.selectList("x.y.selectIf", null, new RowBounds(0, 10));
  
  //第二种，Mapper接口方式的调用，推荐这种使用方式。
  PageHelper.startPage(1, 10);
  List<Country> list = countryMapper.selectIf(1);
  
  //第三种，Mapper接口方式的调用，推荐这种使用方式。
  PageHelper.offsetPage(1, 10);
  List<Country> list = countryMapper.selectIf(1);
  
  //第四种，参数方法调用
  //存在以下 Mapper 接口方法，你不需要在 xml 处理后两个参数
  public interface CountryMapper {
      List<Country> selectByPageNumSize(
              @Param("user") User user,
              @Param("pageNum") int pageNum, 
              @Param("pageSize") int pageSize);
  }
  //配置supportMethodsArguments=true
  //在代码中直接调用：
  List<Country> list = countryMapper.selectByPageNumSize(user, 1, 10);
  
  //第五种，参数对象
  //如果 pageNum 和 pageSize 存在于 User 对象中，只要参数有值，也会被分页
  //有如下 User 对象
  public class User {
      //其他fields
      //下面两个参数名和 params 配置的名字一致
      private Integer pageNum;
      private Integer pageSize;
  }
  //存在以下 Mapper 接口方法，你不需要在 xml 处理后两个参数
  public interface CountryMapper {
      List<Country> selectByPageNumSize(User user);
  }
  //当 user 中的 pageNum!= null && pageSize!= null 时，会自动分页
  List<Country> list = countryMapper.selectByPageNumSize(user);
  
  //第六种，ISelect 接口方式
  //jdk6,7用法，创建接口
  Page<Country> page = PageHelper.startPage(1, 10).doSelectPage(new ISelect() {
      @Override
      public void doSelect() {
          countryMapper.selectGroupBy();
      }
  });
  //jdk8 lambda用法
  Page<Country> page = PageHelper.startPage(1, 10).doSelectPage(()-> countryMapper.selectGroupBy());
  
  //也可以直接返回PageInfo，注意doSelectPageInfo方法和doSelectPage
  pageInfo = PageHelper.startPage(1, 10).doSelectPageInfo(new ISelect() {
      @Override
      public void doSelect() {
          countryMapper.selectGroupBy();
      }
  });
  //对应的lambda用法
  pageInfo = PageHelper.startPage(1, 10).doSelectPageInfo(() -> countryMapper.selectGroupBy());
  
  //count查询，返回一个查询语句的count数
  long total = PageHelper.count(new ISelect() {
      @Override
      public void doSelect() {
          countryMapper.selectLike(country);
      }
  });
  //lambda
  total = PageHelper.count(()->countryMapper.selectLike(country));
  ```

  



XML中的一些特殊字符的转译符

![1554724202872](C:\Users\32183\AppData\Roaming\Typora\typora-user-images\1554724202872.png)

练习：

> ![1554725010278](C:\Users\32183\AppData\Roaming\Typora\typora-user-images\1554725010278.png)