package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSelectCountColumnConverter;

public class OracleSelectCountColumnConverter extends MySqlSelectCountColumnConverter {
    private static volatile OracleSelectCountColumnConverter instance;

    protected OracleSelectCountColumnConverter() {
    }

    public static OracleSelectCountColumnConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSelectCountColumnConverter.class) {
                if (instance == null) {
                    instance = new OracleSelectCountColumnConverter();
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
