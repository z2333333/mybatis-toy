package com.zx.mybatis;

import com.zx.mybatis.builder.XMLConfigBuilder;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder("mybatis-config.xml");
        xmlConfigBuilder.doBuild();
    }
}
