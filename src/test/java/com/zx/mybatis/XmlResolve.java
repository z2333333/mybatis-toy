package com.zx.mybatis;

import com.zx.mybatis.builder.JdbcDriver;
import com.zx.mybatis.builder.Resources;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zx
 * @date 2020/5/28 19:51
 */
public class XmlResolve {
    public static void main(String[] args) {
        String resource="mybatis-config.xml";
        InputStream inputStream=null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Document document = createDocument(new InputSource(inputStream));
        parseElements(document.getDocumentElement());
    }

    public static Document createDocument(InputSource inputSource) {
        // important: this must only be called AFTER common constructor
        try {
            //这个是DOM解析方式
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);

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
            //builder.setEntityResolver(entityResolver);
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
            return builder.parse(inputSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void parseElements(Element root){
        //todo 模板
        NodeList nl = root.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                //4-从元素解析得到属性值

                if (ele.getNodeName().equals("environments")) {
                    doDateSource(ele);
                }

                String name = ele.getAttribute("name");//根据属性名称读取属性值
                System.out.println("name：" + name);
                String className = ele.getAttribute("class");
                System.out.println("className：" + className);
                //5-从元素解析特定子元素并解析(以property为例)
                getCertainElementFromParentElement(ele);
            }
        }
    }

    private static void doDateSource(Element element){
        NodeList dataSourceElements = element.getElementsByTagName("dataSource");
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
        }
    }

    private static void parseElementFromRoot(Element root) {
        NodeList nl = root.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                //4-从元素解析得到属性值
                String name = ele.getAttribute("name");//根据属性名称读取属性值
                System.out.println("name：" + name);
                String className = ele.getAttribute("class");
                System.out.println("className：" + className);
                //5-从元素解析特定子元素并解析(以property为例)
                getCertainElementFromParentElement(ele);
            }
        }
    }

    private static void getCertainElementFromParentElement(Element ele) {
        NodeList propertyEleList = ele.getElementsByTagName("property");//根据标签名称获取标签元素列表
        for (int i = 0; i < propertyEleList.getLength(); i++) {
            Node node = propertyEleList.item(i);
            if (node instanceof Element) {
                Element propertyEle = (Element) node;
                String name = propertyEle.getAttribute("name");
                System.out.println("propertyEle: name == " + name);
                String value = propertyEle.getAttribute("value");
                System.out.println("propertyEle: value == " + value);
            }
        }
    }
}
