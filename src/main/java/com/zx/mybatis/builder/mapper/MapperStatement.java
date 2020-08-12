package com.zx.mybatis.builder.mapper;

import com.zx.mybatis.mapping.ParameterMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zx
 * @date 2020/6/18 14:52
 * sql级的对象
 */
public class MapperStatement {
    private final String id;
    private String type;
    //当xml中不指定时意味着是通过@parm方式或map传参,按照顺序替换sql中的?
    private String parameterType;
    private Class<?> parameterTypeClass;
    /**
     * 当参数类型为自定义对象时list保存对象的所有属性
     */
    private final List<ParameterMapping> parameterMappings = new ArrayList<>();
    private String resultMap;
    private String orginSql;

    public MapperStatement(String id,String parameterType,String resultMap) {
        this.id = id;
        this.parameterType = parameterType;
        this.resultMap = resultMap;
    }

    public String getOrginSql() {
        return orginSql;
    }

    public void setOrginSql(String orginSql) {
        this.orginSql = orginSql;
    }

    public void setParameterTypeClass(Class<?> parameterTypeClass) {
        this.parameterTypeClass = parameterTypeClass;
    }

    public void addParameterMapping(ParameterMapping parameterMapping) {
        parameterMappings.add(parameterMapping);
    }
}
