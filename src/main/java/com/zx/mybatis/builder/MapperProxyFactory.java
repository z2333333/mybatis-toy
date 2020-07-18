package com.zx.mybatis.builder;

import java.lang.reflect.Proxy;


/**
 * @author zx
 * @date 2020/6/9 15:40
 */
public class MapperProxyFactory {

    protected static <T> T newInstance(MapperProxy<T> mapperProxy, Class<T> mapperInterface) {
        //用JDK自带的动态代理生成映射器
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }

    public static <T> T newInstance(Class<T> mapperInterface) {
        final MapperProxy<T> mapperProxy = new MapperProxy<T>(mapperInterface);
        return newInstance(mapperProxy, mapperInterface);
    }
}
