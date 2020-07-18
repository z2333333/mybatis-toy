package com.zx.mybatis;

import com.zx.mybatis.builder.JdbcDriver;

/**
 * @author zx
 * @date 2020/5/29 14:21
 */
public class Configuration {
    private JdbcDriver jdbcDriver;

    public JdbcDriver getJdbcDriver() {
        return jdbcDriver;
    }

    public void setJdbcDriver(JdbcDriver jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }
}
