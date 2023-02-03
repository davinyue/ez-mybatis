package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSelectMinColumnConverter;

public class OracleSelectMinColumnConverter extends MySqlSelectMinColumnConverter {
    private static volatile OracleSelectMinColumnConverter instance;

    protected OracleSelectMinColumnConverter() {
    }

    public static OracleSelectMinColumnConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSelectMinColumnConverter.class) {
                if (instance == null) {
                    instance = new OracleSelectMinColumnConverter();
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
