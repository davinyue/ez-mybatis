package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlUpdateColumnItemConverter;

public class OracleUpdateColumnItemConverter extends MySqlUpdateColumnItemConverter {
    private static volatile OracleUpdateColumnItemConverter instance;

    protected OracleUpdateColumnItemConverter() {
    }

    public static OracleUpdateColumnItemConverter getInstance() {
        if (instance == null) {
            synchronized (OracleUpdateColumnItemConverter.class) {
                if (instance == null) {
                    instance = new OracleUpdateColumnItemConverter();
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
