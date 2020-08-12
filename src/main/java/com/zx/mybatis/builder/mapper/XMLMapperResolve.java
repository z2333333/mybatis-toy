package com.zx.mybatis.builder.mapper;

import cn.hutool.core.util.ObjectUtil;
import com.mysql.cj.util.StringUtils;
import com.zx.mybatis.builder.XMLResolveAssistant;
import com.zx.mybatis.mapping.MetaClass;
import com.zx.mybatis.mapping.ParameterMapping;
import com.zx.mybatis.mapping.TypeAliasRegistry;
import com.zx.mybatis.mapping.TypeHandlerRegistry;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zx
 * @date 2020/6/15 16:58
 */
public class XMLMapperResolve {
    private final MapperHolder mapperHolder;
    private final MapperBuilderAssistant mapperBuilderAssistant = new MapperBuilderAssistant();
    private final TypeHandlerRegistry typeHandlerRegistry;
    private final TypeAliasRegistry typeAliasRegistry;
    private XMLResolveAssistant xmlResolveAssistant = new XMLResolveAssistant();

    public XMLMapperResolve(String name, MapperHolder mapperHolder, MapperManager mapperManager) {
        String xmlResource = "mapper/" + name + ".xml";
        xmlResolveAssistant.createDocument(xmlResource);
        this.mapperHolder = mapperHolder;
        this.typeHandlerRegistry = mapperManager.getTypeHandlerRegistry();
        this.typeAliasRegistry = mapperManager.getTypeAliasRegistry();
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

    protected void resultMapElements(NodeList resultMaps) {
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

            mapperHolder.addResultMap(resultMapId, resultMap);
        }
    }

    protected void sqlStatement() {
        for (SqlCommandType value : SqlCommandType.values()) {
            String type = value.name().toLowerCase();
            NodeList elements = xmlResolveAssistant.getTargetElements(type);
            if (elements != null && elements.getLength() > 0) {
                for (int i = 0; i < elements.getLength(); i++) {
                    sqlCommon((Element) elements.item(i));
                }
            }
        }
    }

    //解析当前xml的每个sql块,为块中待注入参数设置ParameterMapping
    private void sqlCommon(Element resultElement) {
        String id = resultElement.getAttribute("id");
        if (StringUtils.isNullOrEmpty(id)) {
            throw new RuntimeException("XML中sql标签ID不能为空");
        }

        String parameterType = resultElement.getAttribute("parameterType");
        String resultMap = resultElement.getAttribute("resultMap");
        Class<?> resolveAlias = typeAliasRegistry.resolveAlias(parameterType);

        //sql块的id在其mapper中有对应的方法才会设置
        Method method = getMapperMethod(id);
        if (method != null && !mapperHolder.getMethodCache().containsKey(method)) {
            MapperStatement mapperStatement = new MapperStatement(id, parameterType, resultMap);
            MapperMethod mapperMethod = new MapperMethod(mapperStatement);
            //返回值类型绑定
            //用建造模式,最终的组合和校验在build.build完成
            String parseSql = mapperBuilderAssistant.parseSql(resultElement.getTextContent());
            mapperStatement.setOrginSql(parseSql);
            for (String orgSqlParameter : mapperBuilderAssistant.getOrgSqlParameter()) {
                Class<?> propertyType = null;
                //将字符串表示的参数类型解析成对应的Java类
                mapperStatement.setParameterTypeClass(resolveAlias);
                if (!StringUtils.isNullOrEmpty(parameterType)) {
                    //分为基本类型和自定义类型两种情况
                    if (typeHandlerRegistry.hasTypeHandler(resolveAlias)) {
                        propertyType = resolveAlias;
                    } else if (parameterType != null) {
                        //实例化parameterType,判断是否有对应字段的getter方法
                        MetaClass metaClass = MetaClass.forClass(resolveAlias);
                        if (metaClass.hasGetter(orgSqlParameter)) {
                            //propertyType类型为该字段在自定义类中的字段类型
                            propertyType = metaClass.getGetterType(orgSqlParameter);
                        } else {
                            propertyType = Object.class;
                        }
                    }
                } else {
                    propertyType = Object.class;
                }

                //ParameterMapping用于执行sql时对参数进行映射
                ParameterMapping.Builder builder = new ParameterMapping.Builder(typeHandlerRegistry);
                builder.javaType(propertyType);
                mapperStatement.addParameterMapping(builder.build());
            }
            mapperHolder.getMethodCache().put(method, mapperMethod);
            mapperBuilderAssistant.resetOrgSqlParameter();
        }
    }

    protected Method getMapperMethod(String methodName) {
        Class<?> aClass = mapperHolder.getMapper(null).getClass();
        for (Method method : aClass.getMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
}
