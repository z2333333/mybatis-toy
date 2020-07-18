package com.zx.mybatis.builder.mapper;

import cn.hutool.db.meta.JdbcType;

/**
 * @author zx
 * @date 2020/6/17 14:25
 */
public class ResultMapping {
    private String column;
    private String property;
    private Class<?> javaType;
    private JdbcType jdbcType;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public void setJavaType(Class<?> javaType) {
        this.javaType = javaType;
    }

    public JdbcType getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(JdbcType jdbcType) {
        this.jdbcType = jdbcType;
    }
}
