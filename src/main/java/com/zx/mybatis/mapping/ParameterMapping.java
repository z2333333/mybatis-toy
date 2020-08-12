package com.zx.mybatis.mapping;

/**
 * @author zx
 * @date 2020/8/5 17:55
 * 用于执行sql时对参数进行映射
 */
public class ParameterMapping {
    //建造者模式
    private Class<?> javaType = Object.class;
    private JdbcType jdbcType;
    private TypeHandler<?> typeHandler;
    private TypeHandlerRegistry typeHandlerRegistry;

    private ParameterMapping() {}

    public static class Builder {
        private ParameterMapping parameterMapping = new ParameterMapping();
        public Builder(TypeHandlerRegistry typeHandlerRegistry) {
            parameterMapping.typeHandlerRegistry = typeHandlerRegistry;
        }

        public ParameterMapping build() {
            resolveTypeHandler();
            return parameterMapping;
        }

        public Builder javaType(Class<?> javaType) {
            parameterMapping.javaType = javaType;
            return this;
        }

        private void resolveTypeHandler() {
            if (parameterMapping.typeHandler == null && parameterMapping.javaType != null) {
                parameterMapping.typeHandler = parameterMapping.typeHandlerRegistry.getTypeHandler(parameterMapping.javaType, parameterMapping.jdbcType);
            }
        }
    }
}
