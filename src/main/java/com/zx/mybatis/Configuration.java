package com.zx.mybatis;

import cn.hutool.core.util.ObjectUtil;
import com.zx.mybatis.builder.DataBaseDriver;
import com.zx.mybatis.builder.mapper.MapperManager;

/**
 * @author zx
 * @date 2020/5/29 14:21
 */
public class Configuration {
    protected DataBaseDriver jdbcDriver;
    protected MapperManager mapperManager;

    private Configuration(){}
    public static class Builder {
        private Configuration configuration = new Configuration();

        public Builder() {
        }

        public Configuration build() {
            if (ObjectUtil.isEmpty(configuration.jdbcDriver)) {
                throw new RuntimeException("jdbc driver cannot be null");
            }
            configuration.jdbcDriver.iniDriver();
            return configuration;
        }

        public Builder jdbcDriver(DataBaseDriver dataBaseDriver) {
            configuration.jdbcDriver = dataBaseDriver;
            return this;
        }

        public Builder mapperManager(MapperManager mapperManager) {
            configuration.mapperManager = mapperManager;
            return this;
        }
    }
}

