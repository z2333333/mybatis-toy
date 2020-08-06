package com.zx.mybatis.builder.mapper;

/**
 * @author zx
 * @date 2020/6/15 10:27
 * Mapper中方法对象:select|insert..级别
 */
public class MapperMethod {
    MapperStatement mapperStatement;

    public MapperMethod(MapperStatement mapperStatement) {
        this.mapperStatement = mapperStatement;
    }
}
