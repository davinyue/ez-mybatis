package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSelectColumnConverter;

public class OracleSelectColumnConverter extends MySqlSelectColumnConverter {
    private static volatile OracleSelectColumnConverter instance;

    protected OracleSelectColumnConverter() {
    }

    public static OracleSelectColumnConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSelectColumnConverter.class) {
                if (instance == null) {
                    instance = new OracleSelectColumnConverter();
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
