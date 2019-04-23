package com.github.soyanga.spel;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * @program: springcore-case-spel
 * @Description: 解析文本字符串
 * @Author: SOYANGA
 * @Create: 2019-04-16 00:26
 * @Version 1.0
 */
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
