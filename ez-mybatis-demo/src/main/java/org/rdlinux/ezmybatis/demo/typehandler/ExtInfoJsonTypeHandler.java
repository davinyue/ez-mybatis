package org.rdlinux.ezmybatis.demo.typehandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.rdlinux.ezmybatis.demo.entity.ExtInfo;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ExtInfo 对应的 JSON TypeHandler
 */
public class ExtInfoJsonTypeHandler extends BaseTypeHandler<ExtInfo> {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ExtInfo parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setString(i, mapper.writeValueAsString(parameter));
        } catch (JsonProcessingException e) {
            throw new SQLException("Error converting ExtInfo to JSON string", e);
        }
    }

    @Override
    public ExtInfo getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parse(rs.getString(columnName));
    }

    @Override
    public ExtInfo getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parse(rs.getString(columnIndex));
    }

    @Override
    public ExtInfo getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parse(cs.getString(columnIndex));
    }

    private ExtInfo parse(String json) throws SQLException {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return mapper.readValue(json, ExtInfo.class);
        } catch (Exception e) {
            throw new SQLException("Error parsing JSON string to ExtInfo", e);
        }
    }
}
