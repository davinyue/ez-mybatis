package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSelectAvgFieldConverter;

public class OracleSelectAvgFieldConverter extends MySqlSelectAvgFieldConverter {
    private static volatile OracleSelectAvgFieldConverter instance;

    protected OracleSelectAvgFieldConverter() {
    }

    public static OracleSelectAvgFieldConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSelectAvgFieldConverter.class) {
                if (instance == null) {
                    instance = new OracleSelectAvgFieldConverter();
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
