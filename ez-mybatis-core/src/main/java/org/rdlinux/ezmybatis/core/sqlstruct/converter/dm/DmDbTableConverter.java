package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleDbTableConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;

public class DmDbTableConverter extends OracleDbTableConverter implements Converter<DbTable> {
    private static volatile DmDbTableConverter instance;

    protected DmDbTableConverter() {
    }

    public static DmDbTableConverter getInstance() {
        if (instance == null) {
            synchronized (DmDbTableConverter.class) {
                if (instance == null) {
                    instance = new DmDbTableConverter();
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
