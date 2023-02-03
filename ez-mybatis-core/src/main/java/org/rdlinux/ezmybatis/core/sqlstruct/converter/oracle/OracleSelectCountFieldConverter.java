package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSelectCountFieldConverter;

public class OracleSelectCountFieldConverter extends MySqlSelectCountFieldConverter {
    private static volatile OracleSelectCountFieldConverter instance;

    protected OracleSelectCountFieldConverter() {
    }

    public static OracleSelectCountFieldConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSelectCountFieldConverter.class) {
                if (instance == null) {
                    instance = new OracleSelectCountFieldConverter();
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
