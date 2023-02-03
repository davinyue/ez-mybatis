package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSelectMinFieldConverter;

public class OracleSelectMinFieldConverter extends MySqlSelectMinFieldConverter {
    private static volatile OracleSelectMinFieldConverter instance;

    protected OracleSelectMinFieldConverter() {
    }

    public static OracleSelectMinFieldConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSelectMinFieldConverter.class) {
                if (instance == null) {
                    instance = new OracleSelectMinFieldConverter();
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
