package com.zx.mybatis.builder.mapper;

import cn.hutool.core.util.ObjectUtil;
import com.zx.mybatis.builder.XMLResolveAssistant;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zx
 * @date 2020/6/15 16:58
 */
public class XMLMapperResolve {
    private final MapperHolder mapperHolder;
    private XMLResolveAssistant xmlResolveAssistant = new XMLResolveAssistant();

    public XMLMapperResolve(String name,MapperHolder mapperHolder) {
        String xmlResource = "mapper/"+ name + ".xml";
        xmlResolveAssistant.createDocument(xmlResource);
        this.mapperHolder = mapperHolder;
    }

    public void doParse(MapperHolder mapperHolder) {
        //校验namespace和class路径一致性
        String namespace = xmlResolveAssistant.getTargetElementTop("mapper").getAttribute("namespace");
        if (namespace == null || namespace.equals("")) {
            throw new RuntimeException("Mapper's namespace cannot be empty");
        }

        //解析resultMap
        resultMapElements(xmlResolveAssistant.getTargetElements("resultMap"));
        sqlStatement();
        //配置sql
    }

    protected void resultMapElements(NodeList resultMaps){
        if (ObjectUtil.isEmpty(resultMaps)) {
            return;
        }
        //XMLMapperBuilder.resultMapElement
        //typeAliasRegistry的几个来源：配置文件中<typeAliases>标签
        for (int i = 0; i < resultMaps.getLength(); i++) {
            Element element = (Element) resultMaps.item(i);
            //resultMap标签的type属性转成class-存在的情况：全限定类名，别名（typeAliases标签过来的），Java中的基本类型
            String type = element.getAttribute("type");
            String resultMapId = element.getAttribute("id");
            Class<?> typeClass;
            try {
                typeClass = Class.forName(type);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Can not find class:" + type);
            }
            ResultMap resultMap = new ResultMap();
            resultMap.setId(resultMapId);
            resultMap.setType(typeClass);

            NodeList results = element.getElementsByTagName("result");
            List<ResultMapping> resultMappingList = new ArrayList<>();
            for (int j = 0; j < results.getLength(); j++) {
                Element resultElement = (Element) results.item(j);
                String column = resultElement.getAttribute("column");
                String property = resultElement.getAttribute("property");
                String javaType = resultElement.getAttribute("javaType");
                String jdbcType = resultElement.getAttribute("jdbcType");
                ResultMapping resultMapping = new ResultMapping();
                resultMapping.setColumn(column);
                resultMapping.setProperty(property);
                resultMapping.setJavaType(null);
                resultMapping.setJdbcType(null);
                resultMappingList.add(resultMapping);
            }
            if (!resultMappingList.isEmpty()) {
                resultMap.setResultMappingList(resultMappingList);
            }

            mapperHolder.addResultMap(resultMapId,resultMap);
        }
    }

    protected void sqlStatement(){
        for (SqlCommandType value : SqlCommandType.values()) {
            String type = value.name().toLowerCase();
            Element targetElement = xmlResolveAssistant.getTargetElementTop(type);
            sqlCommon(targetElement);
        }
    }

    private void sqlCommon(Element resultElement){
        String id = resultElement.getAttribute("id");
        String parameterType = resultElement.getAttribute("parameterType");
        String resultMap = resultElement.getAttribute("resultMap");
    }
}
