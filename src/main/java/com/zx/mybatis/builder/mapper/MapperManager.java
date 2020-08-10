package com.zx.mybatis.builder.mapper;

import com.zx.mybatis.mapping.TypeAliasRegistry;
import com.zx.mybatis.mapping.TypeHandlerRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zx
 * @date 2020/6/12 10:10
 */
public class MapperManager {
    protected Map<String,MapperHolder> mapperHolderMap = new HashMap<>();
    /** 类型处理器注册机 **/
    protected final TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();
    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    public void putMapper(String key, MapperHolder mapperHolder) {
        mapperHolderMap.put(key, mapperHolder);
    }

    public boolean contain(String name) {
        return mapperHolderMap.containsKey(name);
    }

    private MapperManager(){}
    private static class MapperManagerHolder{
        private static MapperManager instance = new MapperManager();
    }

    public static final MapperManager getInstance(){
        return MapperManagerHolder.instance;
    }

    public TypeHandlerRegistry getTypeHandlerRegistry() {
        return typeHandlerRegistry;
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }
}
