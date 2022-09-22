package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSqlTableConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.SqlTable;

public class DmSqlTableConverter extends OracleSqlTableConverter implements Converter<SqlTable> {
    private static volatile DmSqlTableConverter instance;

    protected DmSqlTableConverter() {
    }

    public static DmSqlTableConverter getInstance() {
        if (instance == null) {
            synchronized (DmSqlTableConverter.class) {
                if (instance == null) {
                    instance = new DmSqlTableConverter();
                }
            }
        }
        return instance;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.DM;
    }
}
