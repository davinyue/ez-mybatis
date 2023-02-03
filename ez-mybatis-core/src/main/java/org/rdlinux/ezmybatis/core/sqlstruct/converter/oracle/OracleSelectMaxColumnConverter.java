package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSelectMaxColumnConverter;

public class OracleSelectMaxColumnConverter extends MySqlSelectMaxColumnConverter {
    private static volatile OracleSelectMaxColumnConverter instance;

    protected OracleSelectMaxColumnConverter() {
    }

    public static OracleSelectMaxColumnConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSelectMaxColumnConverter.class) {
                if (instance == null) {
                    instance = new OracleSelectMaxColumnConverter();
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
