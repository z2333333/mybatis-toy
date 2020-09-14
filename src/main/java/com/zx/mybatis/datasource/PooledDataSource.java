package com.zx.mybatis.datasource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author zx
 * @date 2020/9/14 17:05
 */
public class PooledDataSource extends DataSourceDecorate{
    public PooledDataSource(DataSource dataSource) {
        super(dataSource);
    }

    private void strengthen() {
        System.out.println("进行功能增强");
    }

    @Override
    public Connection getConnection() throws SQLException {
        strengthen();//从连接池获取连接,如果没有则调用原生的获取
        return super.getConnection();
    }
}
