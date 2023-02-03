package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSyntaxUpdateFieldItemConverter;

public class OracleSyntaxUpdateFieldItemConverter extends MySqlSyntaxUpdateFieldItemConverter {
    private static volatile OracleSyntaxUpdateFieldItemConverter instance;

    protected OracleSyntaxUpdateFieldItemConverter() {
    }

    public static OracleSyntaxUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSyntaxUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new OracleSyntaxUpdateFieldItemConverter();
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
