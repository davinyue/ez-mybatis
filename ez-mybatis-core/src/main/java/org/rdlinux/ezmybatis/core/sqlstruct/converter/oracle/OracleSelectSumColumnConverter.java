package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSelectSumColumnConverter;

public class OracleSelectSumColumnConverter extends MySqlSelectSumColumnConverter {
    private static volatile OracleSelectSumColumnConverter instance;

    protected OracleSelectSumColumnConverter() {
    }

    public static OracleSelectSumColumnConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSelectSumColumnConverter.class) {
                if (instance == null) {
                    instance = new OracleSelectSumColumnConverter();
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
