package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlUpdateFieldItemConverter;

public class OracleUpdateFieldItemConverter extends MySqlUpdateFieldItemConverter {
    private static volatile OracleUpdateFieldItemConverter instance;

    protected OracleUpdateFieldItemConverter() {
    }

    public static OracleUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (OracleUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new OracleUpdateFieldItemConverter();
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
