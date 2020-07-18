package com.zx.mybatis.builder.mapper;

import java.util.Set;

/**
 * @author zx
 * @date 2020/6/11 13:51
 */
public class XMLMapperBuilder extends MapperBuilder{

    public XMLMapperBuilder(String packageName) {
        super(packageName);
    }

    @Override
    protected void scanMapper() {
        Set<Class<? extends Class<?>>> classes = findClassByPackageName(packageName);
        for (Class<?> mapperClass : classes) {
            if (!mapperClass.isInterface()) {
                continue;
            }
            MapperHolder mapperHolder = addMapper(mapperClass);
            XMLMapperResolve xmlMapperResolve = new XMLMapperResolve(mapperClass.getSimpleName(),mapperHolder);
            xmlMapperResolve.doParse(mapperHolder);
        }
    }

    @Override
    protected void resolve() {

    }
}
