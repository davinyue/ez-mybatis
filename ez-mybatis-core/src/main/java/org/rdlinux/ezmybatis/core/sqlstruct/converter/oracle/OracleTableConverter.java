package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlTableConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

public class OracleTableConverter extends MySqlTableConverter implements Converter<Table> {
    private static volatile OracleTableConverter instance;

    protected OracleTableConverter() {
    }

    public static OracleTableConverter getInstance() {
        if (instance == null) {
            synchronized (OracleTableConverter.class) {
                if (instance == null) {
                    instance = new OracleTableConverter();
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
