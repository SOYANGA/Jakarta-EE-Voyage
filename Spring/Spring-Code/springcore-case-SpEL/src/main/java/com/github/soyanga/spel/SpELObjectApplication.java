package com.github.soyanga.spel;

import com.github.soyanga.spel.pojo.PlaceOfBirth;
import com.github.soyanga.spel.pojo.User;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Date;

/**
 * @program: springcore-case-spel
 * @Description: 解析对象属性，类型
 * @Author: SOYANGA
 * @Create: 2019-04-16 00:26
 * @Version 1.0
 */
public class SpELObjectApplication {
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
        String district2 = parser.parseExpression("placeOfBirth.district").getValue(user, String.class);
        System.out.println(district2);

        //
        String nation = (String) parser.parseExpression("placeOfBirth.nation").getValue(user, String.class);
        System.out.println(nation);
    }
}
