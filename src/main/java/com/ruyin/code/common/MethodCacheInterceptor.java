package com.ruyin.code.common;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

import java.util.List;

public class MethodCacheInterceptor implements MethodInterceptor{
    private Logger logger = Logger.getLogger(MethodCacheInterceptor.class);
    private  RedisUtils redisUtils;
    private List<String> targetNameList;
    private List<String> methodNameList;

    public Object invoke(MethodInvocation invocation) throws Throwable {
        return null;
    }
}
