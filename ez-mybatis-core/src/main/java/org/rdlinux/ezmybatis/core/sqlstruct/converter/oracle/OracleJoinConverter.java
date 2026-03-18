package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.Join;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlJoinConverter;

public class OracleJoinConverter extends MySqlJoinConverter implements Converter<Join> {
    @Override
    protected void doBuildSql(Type type, Join join, SqlGenerateContext sqlGenerateContext) {
        if (type != Type.SELECT) {
            throw new UnsupportedOperationException(String.format("%s model unsupported", type.name()));
        }
        super.doBuildSql(type, join, sqlGenerateContext);
    }
}
