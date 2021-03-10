package com.zx.mybatis.builder.mapper;

import com.mysql.cj.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zx
 * @date 2020/6/11 14:02
 * Mapper持有器:对应xml文件级别
 */
public class MapperHolder {
    //测试git连接 2021 0310
    private final String mapperId;
    /** 被代理的mapper对象 **/
    private Object mapperProxy;
    /** 存放解析后的mapper方法 **/
    private final Map<Method, MapperMethod> methodCache = new ConcurrentHashMap<>();

    private final Map<String,ResultMap> resultMap = new ConcurrentHashMap<>();
//    private final Map<String,MapperStatement> statementMap = new ConcurrentHashMap<>();

    public <T> T getMapper(Class<T> type) {
        return (T) mapperProxy;
    }

    public void setMapperProxy(Object mapperProxy) {
        this.mapperProxy = mapperProxy;
    }

    public Map<Method, MapperMethod> getMethodCache() {
        return methodCache;
    }

    public MapperHolder(String mapperId) {
        if (StringUtils.isNullOrEmpty(mapperId)) {
            throw new RuntimeException("mapperId must not null");
        }
        this.mapperId = mapperId;
    }

    public String getMapperId() {
        return mapperId;
    }

    public void addResultMap(String key,ResultMap resultMap){
        this.resultMap.put(key, resultMap);
    }

//    public void addStatementMap(String key,MapperStatement mapperStatement){
//        this.statementMap.put(key, mapperStatement);
//    }
}
