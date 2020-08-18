package com.zx.mybatis.builder;

import com.zx.mybatis.Configuration;
import com.zx.mybatis.builder.mapper.IConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author zx
 * @date 2020/6/1 11:20
 */
public abstract class XMLResolveTemplate implements IConfiguration {
    protected Configuration.Builder configurationBuilder =  new Configuration.Builder();
    protected XMLResolveAssistant xmlResolveAssistant = new XMLResolveAssistant();
    protected String xmlPath;

    protected abstract void parseXml();

    protected abstract void doDateSource();

    protected abstract void doMappers();

    public XMLResolveTemplate(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public void doBuild() {
        parseXml();
        doDateSource();
        doMappers();
    }

    public Document createDocument(String xmlPath) {
        return xmlResolveAssistant.createDocument(xmlPath);
    }

    public Element getTargetElement(String name) {
        return xmlResolveAssistant.getTargetElementTop(name);
    }

    @Override
    public Configuration getConfiguration() {
        return configurationBuilder.build();
    }
}
