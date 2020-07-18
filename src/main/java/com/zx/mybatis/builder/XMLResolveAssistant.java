package com.zx.mybatis.builder;

import cn.hutool.core.util.ObjectUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

/**
 * @author zx
 * @date 2020/6/12 16:12
 */
public class XMLResolveAssistant {
    protected Document root;

    public Document createDocument(String xmlPath) {
        // important: this must only be called AFTER common constructor
        try {
            InputStream inputStream = Resources.getResourceAsStream(xmlPath);

            //这个是DOM解析方式
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);

            //名称空间
            factory.setNamespaceAware(false);
            //忽略注释
            factory.setIgnoringComments(true);
            //忽略空白
            factory.setIgnoringElementContentWhitespace(false);
            //把 CDATA 节点转换为 Text 节点
            factory.setCoalescing(false);
            //扩展实体引用
            factory.setExpandEntityReferences(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            //需要注意的就是定义了EntityResolver(XMLMapperEntityResolver)，这样不用联网去获取DTD，
            //将DTD放在org\apache\ibatis\builder\xml\mybatis-3-config.dtd,来达到验证xml合法性的目的
            builder.setEntityResolver(new XMLMapperEntityResolver());
            builder.setErrorHandler(new ErrorHandler() {
                @Override
                public void error(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void warning(SAXParseException exception) throws SAXException {
                }
            });
            root = builder.parse(new InputSource(inputStream));
            return root;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public Element getTargetElementTop(String name) {
        NodeList environments = root.getElementsByTagName(name);
        if (ObjectUtil.isEmpty(environments)) {
            throw new RuntimeException(name + "tag is null");
        }
        return (Element) environments.item(0);
    }

    public NodeList getTargetElements(String name) {
        NodeList environments = root.getElementsByTagName(name);
        return environments;
    }
}
