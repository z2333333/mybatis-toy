package com.zx.mybatis.builder.mapper;

import com.mysql.cj.util.StringUtils;
import com.zx.mybatis.builder.MapperProxyFactory;

import java.util.Set;

/**
 * @author zx
 * @date 2020/6/11 11:33
 */
public abstract class MapperBuilder {
    protected final String packageName;
    private MapperRegistry mapperRegistry = new MapperRegistry();
    private MapperManager mapperManager;

    protected abstract void scanMapper();

    protected abstract void resolve();

    public MapperBuilder(String packageName){
        if (StringUtils.isNullOrEmpty(packageName)) {
            throw new RuntimeException("package must not null");
        }
        this.packageName = packageName;

        if (mapperManager == null) {
            mapperManager = MapperManager.getInstance();
        }
    }

    protected MapperHolder addMapper(Class<?> mapperClass) {
        if (contain(mapperClass.getName())) {
            throw new RuntimeException("already exists the same mapper");
        }
        Object instance = MapperProxyFactory.newInstance(mapperClass);
        MapperHolder mapperHolder = new MapperHolder(mapperClass.getName());
        mapperHolder.setMapperProxy(instance);
        mapperManager.putMapper(mapperHolder.getMapperId(),mapperHolder);
        return mapperHolder;
    }

    protected MapperManager getResult() {
        return mapperManager;
    }

    protected boolean contain(String name) {
        return mapperManager.contain(name);
    }

    protected Set<Class<? extends Class<?>>> findClassByPackageName(String packageName) {
        return mapperRegistry.findClassByPackageName(packageName);
    }
}
