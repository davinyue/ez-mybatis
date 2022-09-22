package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlDbTableConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;

public class OracleDbTableConverter extends MySqlDbTableConverter implements Converter<DbTable> {
    private static volatile OracleDbTableConverter instance;

    protected OracleDbTableConverter() {
    }

    public static OracleDbTableConverter getInstance() {
        if (instance == null) {
            synchronized (OracleDbTableConverter.class) {
                if (instance == null) {
                    instance = new OracleDbTableConverter();
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
