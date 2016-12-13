package com.ruyin.code.redis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service("memberService")
public class MemberService extends RedisGenerator<String,Member>{

    /**添加对象*/
    public boolean add(final Member member){
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                RedisSerializer<String> serializer =redisTemplate.getStringSerializer();
                byte[] key = serializer.serialize(member.getId());
                byte[] name = serializer.serialize(member.getNickname());
                return redisConnection.setNX(key,name);
            }
        });
        return  result;
    }

    /**添加集合*/
    public boolean add(final List<Member> list){
        Assert.notEmpty(list);
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                for (Member member: list) {
                    byte[] key = serializer.serialize(member.getId());
                    byte[] name = serializer.serialize(member.getNickname());
                    redisConnection.setNX(key,name);
                }
                return true;
            }
        },false,true);
        return  result;
    }

    /**删除对象,依赖key*/
    public void delete(String key){
        redisTemplate.delete(key);
    }

    /**删除集合,依赖keys集合*/
    public void delete(List<String> keys){
        redisTemplate.delete(keys);
    }

    /**修改对象*/
    public boolean update(final Member member){
        String key = member.getId();
        if(get(key) == null)
            throw  new NullPointerException("数据行不存在,key="+key);
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] key = serializer.serialize(member.getId());
                byte[] value = serializer.serialize(member.getNickname());
                redisConnection.set(key,value);
                return true;
            }
        });
        return result;
    }

    /**根据key获取对象*/
    public  Member get(final String id){
        Member member = redisTemplate.execute(new RedisCallback<Member>() {
            public Member doInRedis(RedisConnection redisConnection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] key = serializer.serialize(id);
                byte[] value = redisConnection.get(key);
                if(value == null)
                    return  null;
                String nickname = serializer.deserialize(value);
                return new Member(id,nickname);
            }
        });
        return  member;
    }
}
