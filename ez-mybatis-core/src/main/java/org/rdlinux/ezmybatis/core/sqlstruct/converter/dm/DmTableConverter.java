package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleTableConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

public class DmTableConverter extends OracleTableConverter implements Converter<Table> {
    private static volatile DmTableConverter instance;

    protected DmTableConverter() {
    }

    public static DmTableConverter getInstance() {
        if (instance == null) {
            synchronized (DmTableConverter.class) {
                if (instance == null) {
                    instance = new DmTableConverter();
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
