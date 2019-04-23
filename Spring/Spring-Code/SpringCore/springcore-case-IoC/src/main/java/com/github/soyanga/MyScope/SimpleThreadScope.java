package com.github.soyanga.MyScope;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.core.NamedInheritableThreadLocal;

import java.util.HashMap;
import java.util.Map;


/**
 * @program: springcore-case-IoC
 * @Description: 自定义作用域 用于线程
 * @Author: SOYANGA
 * @Create: 2019-04-12 00:52
 * @Version 1.0
 */
public class SimpleThreadScope implements Scope {
    ThreadLocal<Map<String, Object>> beans = new NamedInheritableThreadLocal<Map<String, Object>>("SimpleThreadLocal") {
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<String, Object>();
        }
    };
    public static final Logger logger = LoggerFactory.getLogger(SimpleThreadScope.class);


    @Override
    public Object get(String s, ObjectFactory<?> objectFactory) {
        Map<String, Object> scope = this.beans.get();
        Object object = scope.get(s);
        if (object == null) {
            object = objectFactory.getObject();
            scope.put(s, object);
        }
        return object;
    }

    @Override
    public Object remove(String s) {
        return this.beans.get().remove(s);
    }

    @Override
    public void registerDestructionCallback(String s, Runnable runnable) {
        logger.warn("SimpleThreadScope does not support destruction callbacks. +" +
                "Consider using RequestScope in a web enviornment");
    }

    @Override
    public Object resolveContextualObject(String s) {
        return null;
    }

    @Override
    public String getConversationId() {
        return Thread.currentThread().getName(); //返回当前线程名称
    }
}
