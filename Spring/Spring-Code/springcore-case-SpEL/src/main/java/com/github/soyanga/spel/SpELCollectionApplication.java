package com.github.soyanga.spel;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @program: springcore-case-spel
 * @Description: 解析数组和集合
 * @Author: SOYANGA
 * @Create: 2019-04-16 00:26
 * @Version 1.0
 */
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
