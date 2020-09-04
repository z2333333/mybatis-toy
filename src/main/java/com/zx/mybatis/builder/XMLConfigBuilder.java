package com.zx.mybatis.builder;

import com.zx.mybatis.builder.mapper.MapperDirector;
import com.zx.mybatis.builder.mapper.XMLMapperBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author zx
 * @date 2020/6/1 13:54
 */
public class XMLConfigBuilder extends XMLResolveTemplate {

    public XMLConfigBuilder(String xmlPath) {
        super(xmlPath);
    }

    @Override
    protected void parseXml() {
        createDocument(this.xmlPath);
    }

    @Override
    protected void doDateSource() {
        Element element = getTargetElement("environments");
        NodeList dataSourceElements = element.getElementsByTagName("dataSource");
        //实际上mybatis把数据源DataSource分为三种(抽象工厂):
        //UNPOOLED不使用连接池的数据源
        //POOLED使用连接池的数据源
        //JNDI使用JNDI实现的数据源
        if (dataSourceElements != null) {
            String url = "";
            String userName = "";
            String passWord = "";
            NodeList childNodes = dataSourceElements.item(0).getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (node instanceof Element) {
                    Element propertyEle = (Element) node;
                    String name = propertyEle.getAttribute("name");
                    System.out.println("propertyEle: name == " + name);
                    String value = propertyEle.getAttribute("value");
                    if (name.equals("url")) {
                        url = value;
                    }else if (name.equals("username")) {
                        userName = value;
                    }else if (name.equals("password")) {
                        passWord = value;
                    }
                    System.out.println("propertyEle: value == " + value);
                }
            }

            JdbcDriver jdbcDriver = new JdbcDriver(url,userName,passWord);
            this.configurationBuilder.jdbcDriver(jdbcDriver);
        }
    }

    @Override
    protected void doMappers() {
        Element element = getTargetElement("mappers");
        NodeList packageElements = element.getElementsByTagName("package");
        if (packageElements != null) {
            Element item = (Element)packageElements.item(0);
            String packageName = item.getAttribute("name");

            //扫描包下的接口
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(packageName);
            MapperDirector mapperDirector = new MapperDirector(xmlMapperBuilder);
            mapperDirector.doBuilder();

        }
    }
}
