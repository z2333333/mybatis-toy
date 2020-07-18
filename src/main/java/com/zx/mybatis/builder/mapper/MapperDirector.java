package com.zx.mybatis.builder.mapper;

/**
 * @author zx
 * @date 2020/6/11 14:43
 */
public class MapperDirector {
    private MapperBuilder[] mapperBuilders;

    //todo 中介者模式
    public MapperDirector(MapperBuilder... mapperBuilders){
        this.mapperBuilders = mapperBuilders;
    }

    public void doBuilder(){
        for (MapperBuilder mapperBuilder : mapperBuilders) {
            mapperBuilder.scanMapper();
            mapperBuilder.resolve();
        }
    }
}
