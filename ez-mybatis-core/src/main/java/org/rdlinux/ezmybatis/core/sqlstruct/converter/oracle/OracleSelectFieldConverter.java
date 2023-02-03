package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSelectFieldConverter;

public class OracleSelectFieldConverter extends MySqlSelectFieldConverter {
    private static volatile OracleSelectFieldConverter instance;

    protected OracleSelectFieldConverter() {
    }

    public static OracleSelectFieldConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSelectFieldConverter.class) {
                if (instance == null) {
                    instance = new OracleSelectFieldConverter();
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
