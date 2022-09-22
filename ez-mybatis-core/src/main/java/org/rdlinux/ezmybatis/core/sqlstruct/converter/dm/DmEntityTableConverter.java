package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleEntityTableConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

public class DmEntityTableConverter extends OracleEntityTableConverter implements Converter<EntityTable> {
    private static volatile DmEntityTableConverter instance;

    protected DmEntityTableConverter() {
    }

    public static DmEntityTableConverter getInstance() {
        if (instance == null) {
            synchronized (DmEntityTableConverter.class) {
                if (instance == null) {
                    instance = new DmEntityTableConverter();
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
