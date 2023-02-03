package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSelectTableAllItemConverter;

public class OracleSelectTableAllItemConverter extends MySqlSelectTableAllItemConverter {
    private static volatile OracleSelectTableAllItemConverter instance;

    protected OracleSelectTableAllItemConverter() {
    }

    public static OracleSelectTableAllItemConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSelectTableAllItemConverter.class) {
                if (instance == null) {
                    instance = new OracleSelectTableAllItemConverter();
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
