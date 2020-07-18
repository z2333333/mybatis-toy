package com.zx.mybatis.builder.mapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zx
 * @date 2020/6/12 10:10
 */
public class MapperManager {
    private Map<String,MapperHolder> mapperHolderMap = new HashMap<>();

    public void putMapper(String key,MapperHolder mapperHolder) {
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
}
