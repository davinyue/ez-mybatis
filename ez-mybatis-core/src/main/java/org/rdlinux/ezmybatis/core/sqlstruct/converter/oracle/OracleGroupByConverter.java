package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlGroupByConverter;

public class OracleGroupByConverter extends MySqlGroupByConverter {
    private static volatile OracleGroupByConverter instance;

    protected OracleGroupByConverter() {
    }

    public static OracleGroupByConverter getInstance() {
        if (instance == null) {
            synchronized (OracleGroupByConverter.class) {
                if (instance == null) {
                    instance = new OracleGroupByConverter();
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
