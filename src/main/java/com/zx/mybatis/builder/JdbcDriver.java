package com.zx.mybatis.builder;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author zx
 * @date 2020/6/1 9:55
 */
public class JdbcDriver extends AbstractDateBaseDriver implements DataBaseDriver{

    public JdbcDriver(String url, String userName, String passWord) {
        super(url, userName, passWord);
    }

    @Override
    public void iniDriver() {
        try {
            this.connection = DriverManager.getConnection(url, userName, passWord);
            String name = "";
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        if (this.connection != null) {
            try {
                connection.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
