package com.zx.mybatis.builder;

import java.sql.Connection;

/**
 * @author zx
 * @date 2020/6/2 17:05
 */
public abstract class AbstractDateBaseDriver {
    protected Connection connection = null;
    protected String url;
    protected String userName;
    protected String passWord;

    public AbstractDateBaseDriver(String url, String userName, String passWord) {
        this.url = url;
        this.userName = userName;
        this.passWord = passWord;
    }

}
