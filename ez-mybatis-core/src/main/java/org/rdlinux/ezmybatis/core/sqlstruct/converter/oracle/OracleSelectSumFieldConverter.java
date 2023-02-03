package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSelectSumFieldConverter;

public class OracleSelectSumFieldConverter extends MySqlSelectSumFieldConverter {
    private static volatile OracleSelectSumFieldConverter instance;

    protected OracleSelectSumFieldConverter() {
    }

    public static OracleSelectSumFieldConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSelectSumFieldConverter.class) {
                if (instance == null) {
                    instance = new OracleSelectSumFieldConverter();
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
