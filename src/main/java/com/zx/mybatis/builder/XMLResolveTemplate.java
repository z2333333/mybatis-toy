package com.zx.mybatis.builder;

import com.zx.mybatis.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author zx
 * @date 2020/6/1 11:20
 */
public abstract class XMLResolveTemplate {
    protected Configuration configuration =  new Configuration();
    protected XMLResolveAssistant xmlResolveAssistant = new XMLResolveAssistant();
    protected String xmlPath;

    protected abstract void parseXml();

    protected abstract void doDateSource();

    protected abstract void doMappers();

    protected abstract Configuration buildConfiguration();

    public XMLResolveTemplate(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public Configuration iniConfiguration() {
        parseXml();
        doDateSource();
        doMappers();
        return buildConfiguration();
    }

    public Document createDocument(String xmlPath) {
        return xmlResolveAssistant.createDocument(xmlPath);
    }

    public Element getTargetElement(String name) {
        return xmlResolveAssistant.getTargetElementTop(name);
    }
}
