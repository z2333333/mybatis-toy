package com.zx.mybatis.builder.mapper;

/**
 * @author zx
 * @date 2020/6/18 14:52
 */
public class MapperStatement {
    private final String id;
    private String type;
    private String parameterType;
    private String resultMap;
    private String sql;

    public MapperStatement(String id) {
        this.id = id;
    }
}
