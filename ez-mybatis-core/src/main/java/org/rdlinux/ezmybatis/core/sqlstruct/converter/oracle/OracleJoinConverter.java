package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Join;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlJoinConverter;

public class OracleJoinConverter extends MySqlJoinConverter implements Converter<Join> {
    @Override
    protected StringBuilder dobuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration, Join join,
                                       MybatisParamHolder mybatisParamHolder) {
        if (type != Type.SELECT) {
            throw new UnsupportedOperationException(String.format("%s model unsupported", type.name()));
        }
        return super.dobuildSql(type, sqlBuilder, configuration, join, mybatisParamHolder);
    }
}
