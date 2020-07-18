package com.zx.mybatis.builder;

/**
 * @author zx
 * @date 2020/6/2 13:43
 */
public interface DataBaseDriver {
    void iniDriver();

    void destroy();
}
