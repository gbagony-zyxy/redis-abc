package com.ruyin.code.common;

import org.apache.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.Serializable;

public final class RedisUtils {

    private Logger logger = Logger.getLogger(RedisUtils.class);
    private RedisTemplate<Serializable,Object> redisTemplate;

    public void setRedisTemplate(RedisTemplate<Serializable, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //删除keys
    public void remove(String... keys){
        for(String key : keys)
            remove(key);
    }

    public void remove(String key){
        if(exist(key))
            redisTemplate.delete(key);
    }

    //判断缓存是否存在对应的value
    private boolean exist(String key) {
        return  redisTemplate.hasKey(key);
    }

    //读取缓存
    public Object get(String key){
        Object result = null;
        ValueOperations<Serializable,Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    //写入缓存
    public boolean set(String key,Object value){
        boolean result = false;
        try {
            ValueOperations<Serializable,Object> operations = redisTemplate.opsForValue();
            operations.set(key,value);
            result = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    //写入缓存,设置过期时间
    public boolean set(String key,Object value,long expiretime){
        boolean result = false;
        try {
            ValueOperations<Serializable,Object> operations = redisTemplate.opsForValue();
            operations.set(key,value,expiretime);
            result = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
