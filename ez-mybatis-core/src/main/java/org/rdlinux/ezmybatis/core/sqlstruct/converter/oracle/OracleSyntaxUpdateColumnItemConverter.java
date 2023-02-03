package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSyntaxUpdateColumnItemConverter;

public class OracleSyntaxUpdateColumnItemConverter extends MySqlSyntaxUpdateColumnItemConverter {
    private static volatile OracleSyntaxUpdateColumnItemConverter instance;

    protected OracleSyntaxUpdateColumnItemConverter() {
    }

    public static OracleSyntaxUpdateColumnItemConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSyntaxUpdateColumnItemConverter.class) {
                if (instance == null) {
                    instance = new OracleSyntaxUpdateColumnItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.ORACLE;
    }
}
