package com.zx.mybatis.builder.mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zx
 * @date 2020/6/17 14:19
 */
public class ResultMap {
    private String id;
    private Class<?> type;
    private List<ResultMapping> resultMappingList = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public void setResultMappingList(List<ResultMapping> resultMappingList) {
        this.resultMappingList = resultMappingList;
    }
}
