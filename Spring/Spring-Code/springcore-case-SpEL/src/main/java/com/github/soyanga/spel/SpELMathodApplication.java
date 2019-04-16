package com.github.soyanga.spel;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;


/**
 * @program: springcore-case-spel
 * @Description: 解析方法
 * @Author: SOYANGA
 * @Create: 2019-04-16 00:26
 * @Version 1.0
 */
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
