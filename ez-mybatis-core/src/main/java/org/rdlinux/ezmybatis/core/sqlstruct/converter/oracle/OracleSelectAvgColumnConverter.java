package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSelectAvgColumnConverter;

public class OracleSelectAvgColumnConverter extends MySqlSelectAvgColumnConverter {
    private static volatile OracleSelectAvgColumnConverter instance;

    protected OracleSelectAvgColumnConverter() {
    }

    public static OracleSelectAvgColumnConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSelectAvgColumnConverter.class) {
                if (instance == null) {
                    instance = new OracleSelectAvgColumnConverter();
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
