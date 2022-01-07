package org.rdlinux.ezmybatis.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;

/**
 * 将bool类型存储为int处理器
 */
public class BooleanIntTypeHandler extends BaseTypeHandler<Boolean> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType)
            throws SQLException {
        if (parameter == null) {
            ps.setNull(i, Types.NULL);
        } else {
            ps.setInt(i, parameter ? 1 : 0);
        }
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int result = rs.getInt(columnName);
        if (rs.wasNull()) {
            return null;
        } else {
            return result != 0;
        }
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int result = rs.getInt(columnIndex);
        if (rs.wasNull()) {
            return null;
        } else {
            return result != 0;
        }
    }

    @Override
    public Boolean getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int result = cs.getInt(columnIndex);
        if (cs.wasNull()) {
            return null;
        } else {
            return result != 0;
        }
    }
}
