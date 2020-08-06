package com.zx.mybatis.mapping;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author zx
 * @date 2020/8/6 11:26
 */
public abstract class BaseTypeHandler<T> implements TypeHandler<T> {

    @Override
    public void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        //特殊情况，设置NULL
        if (parameter == null) {
            if (jdbcType == null) {
                //如果没设置jdbcType，报错啦
                throw new RuntimeException("JDBC requires that the JdbcType must be specified for all nullable parameters.");
            }
            try {
                //设成NULL
                ps.setNull(i, jdbcType.TYPE_CODE);
            } catch (SQLException e) {
                throw new RuntimeException("Error setting null for parameter #" + i + " with JdbcType " + jdbcType + " . " +
                        "Try setting a different JdbcType for this parameter or a different jdbcTypeForNull configuration property. " +
                        "Cause: " + e, e);
            }
        } else {
            //非NULL情况，怎么设还得交给不同的子类完成, setNonNullParameter是一个抽象方法
            setNonNullParameter(ps, i, parameter, jdbcType);
        }
    }

    @Override
    public T getResult(ResultSet rs, String columnName) throws SQLException {
        T result = getNullableResult(rs, columnName);
        //通过ResultSet.wasNull判断是否为NULL
        if (rs.wasNull()) {
            return null;
        } else {
            return result;
        }
    }

    @Override
    public T getResult(ResultSet rs, int columnIndex) throws SQLException {
        T result = getNullableResult(rs, columnIndex);
        if (rs.wasNull()) {
            return null;
        } else {
            return result;
        }
    }

    @Override
    public T getResult(CallableStatement cs, int columnIndex) throws SQLException {
        T result = getNullableResult(cs, columnIndex);
        //通过CallableStatement.wasNull判断是否为NULL
        if (cs.wasNull()) {
            return null;
        } else {
            return result;
        }
    }

    //非NULL情况，怎么设参数还得交给不同的子类完成
    public abstract void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException;

    //以下3个方法是取得可能为null的结果，具体交给子类完成
    public abstract T getNullableResult(ResultSet rs, String columnName) throws SQLException;

    public abstract T getNullableResult(ResultSet rs, int columnIndex) throws SQLException;

    public abstract T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException;
}
