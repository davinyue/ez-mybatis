package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSelectMaxFieldConverter;

public class OracleSelectMaxFieldConverter extends MySqlSelectMaxFieldConverter {
    private static volatile OracleSelectMaxFieldConverter instance;

    protected OracleSelectMaxFieldConverter() {
    }

    public static OracleSelectMaxFieldConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSelectMaxFieldConverter.class) {
                if (instance == null) {
                    instance = new OracleSelectMaxFieldConverter();
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
