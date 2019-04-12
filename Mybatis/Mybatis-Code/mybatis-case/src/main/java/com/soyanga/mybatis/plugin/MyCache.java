package com.soyanga.mybatis.plugin;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * @program: mybatis-case
 * @Description: MyCache 自定义缓存利用MyBatis的接口
 * @Author: SOYANGA
 * @Create: 2019-04-05 11:22
 * @Version 1.0
 */
/*
 MyBatis会为，每一个nameSpace配置一个Cache实例 将其中的id属性(nameSpace字符串)作为为cache的一个唯一标识
 */
public class MyCache implements Cache {

    /**
     * 日志信息
     */
    private final Logger logger = LoggerFactory.getLogger(MyCache.class);

    /**
     * nameSpace中的id 作为cache的标识符
     */
    private final String id;

    /**
     * 开辟容量
     */
    private final Integer capacity = 1024;

    /**
     * 设置最大缓存数目用户缓存回收策略--（即满即清空）
     */
    private Integer maxSize = 1024;

    /**
     * 将key和value一一对应起来 内部存储数据结构（线程安全的Map）
     */
    private final ConcurrentHashMap<Object, Object> cacaheData = new ConcurrentHashMap<Object, Object>(capacity);  //默认开批1024


    public MyCache(String id) {
        this.id = id;
    }


    /**
     * 获取Cache的标识id
     *
     * @return The identifier of this cache
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * 将key和value一一对应起来  (HashMap/数据库)
     *
     * @param key   Can be any object but usually it is a {@link CacheKey}
     * @param value The result of a select.
     */
    @Override
    public void putObject(Object key, Object value) {
        //即满即清空 回收策略
        if (this.cacaheData.size() >= this.getMaxSize()) {
            this.clear();
        }
        logger.debug("putObject key={} value={}", key, value);
        this.cacaheData.put(key, value);
    }

    /**
     * 获取缓存中的对象
     *
     * @param key The key
     * @return The object stored in the cache.
     */
    @Override
    public Object getObject(Object key) {
        Object value = cacaheData.get(key);
        logger.debug("getObject key={} value={}", key, value);
        return value;
    }


//    As of 3.3.0 this method is only called during a rollback for any previous value that was missing in the cache.
//    This lets any blocking cache to release the lock that may have previously put on the key.
//    A blocking cache puts a lock when a value is null and releases it when the value is back again.
//    This way other threads will wait for the value to be available instead of hitting the database.
//

    /**
     * 删除缓存中的某个对象
     *
     * @param key The key
     * @return Not used
     */
    @Override
    public Object removeObject(Object key) {
        Object value = cacaheData.remove(key);
        logger.debug("removeObject key={} value={}", key, value);
        return value;
    }

    /**
     * Clears this cache instance
     */
    @Override
    public void clear() {
        logger.debug("clear cache");
        cacaheData.clear();
    }

    /**
     * 并不是核心方法，可以不要去实现
     *
     * @return
     */
    @Override
    public int getSize() {
        return 0;
    }

    /**
     * 并不是核心类，可以不去实现
     *
     * @return
     */
    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }
}
