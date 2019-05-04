package com.github.soyanga.springBootBasic.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * @program: springBoot-action
 * @Description: SprinBoot使用 Redis
 * @Author: SOYANGA
 * @Create: 2019-05-04 00:33
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/redis")
public class RedisController {

    /**
     * 自动导入StringRedisTemplate的模板类 --Spirng帮我们已经做好了
     */
    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping(value = "/add")
    public Map<String, String> add(@RequestParam("key") String key, @RequestParam("value") String value) {
        Map<String, String> data = new HashMap<>();
        //添加
        redisTemplate.opsForValue().set(key, value);
        data.put(key, value);
        return data;
    }

    @RequestMapping(value = "/query")
    public Map<String, String> query(@RequestParam("key") String key) {
        Map<String, String> data = new HashMap<>();
        //查询
        String value = redisTemplate.opsForValue().get(key);
        data.put(key, value);
        return data;
    }

    @RequestMapping(value = "delete")
    public Map<String, String> delete(@RequestParam("key") String key) {
        Map<String, String> data = new HashMap<>();
        String value = redisTemplate.opsForValue().get(key);
        //删除
        redisTemplate.delete(key);
        data.put(key, value);
        return data;
    }

    @RequestMapping(value = "/list")
    public Map<String, String> list(@RequestParam("key") String key) {
        Map<String, String> data = new HashMap<>();
        //要去key的类型必须是string类型的
        Set<String> keys = redisTemplate.keys(key);
        if (keys != null) {
            for (String k : keys) {
                if (redisTemplate.type(k) == DataType.STRING) {
                    data.put(k, redisTemplate.opsForValue().get(k));
                }
            }
        }
        return data;
    }
}
