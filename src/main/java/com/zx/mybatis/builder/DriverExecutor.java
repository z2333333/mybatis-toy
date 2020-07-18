package com.zx.mybatis.builder;

/**
 * @author zx
 * @date 2020/6/2 14:47
 */
public class DriverExecutor {
    private final DataBaseDriver dataBaseDriver;

    public DriverExecutor(DataBaseDriver dataBaseDriver){
        this.dataBaseDriver = dataBaseDriver;
    }

    public void iniDriver() {
        this.dataBaseDriver.iniDriver();
    }
}
