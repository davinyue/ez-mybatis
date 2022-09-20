package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlOrderByConverter;

public class OracleOrderByConverter extends MySqlOrderByConverter {
    private static volatile OracleOrderByConverter instance;

    protected OracleOrderByConverter() {
    }

    public static OracleOrderByConverter getInstance() {
        if (instance == null) {
            synchronized (OracleOrderByConverter.class) {
                if (instance == null) {
                    instance = new OracleOrderByConverter();
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
