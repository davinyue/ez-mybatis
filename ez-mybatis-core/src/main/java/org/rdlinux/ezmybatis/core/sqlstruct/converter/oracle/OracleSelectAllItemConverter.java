package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSelectAllItemConverter;

public class OracleSelectAllItemConverter extends MySqlSelectAllItemConverter {
    private static volatile OracleSelectAllItemConverter instance;

    protected OracleSelectAllItemConverter() {
    }

    public static OracleSelectAllItemConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSelectAllItemConverter.class) {
                if (instance == null) {
                    instance = new OracleSelectAllItemConverter();
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
