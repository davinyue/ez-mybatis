package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSelectConverter;

public class OracleSelectConverter extends MySqlSelectConverter {
    private static volatile OracleSelectConverter instance;

    protected OracleSelectConverter() {
    }

    public static OracleSelectConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSelectConverter.class) {
                if (instance == null) {
                    instance = new OracleSelectConverter();
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
