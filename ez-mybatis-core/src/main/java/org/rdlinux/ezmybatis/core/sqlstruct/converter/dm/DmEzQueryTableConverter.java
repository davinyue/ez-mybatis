package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleEzQueryTableConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EzQueryTable;

public class DmEzQueryTableConverter extends OracleEzQueryTableConverter implements Converter<EzQueryTable> {
    private static volatile DmEzQueryTableConverter instance;

    protected DmEzQueryTableConverter() {
    }

    public static DmEzQueryTableConverter getInstance() {
        if (instance == null) {
            synchronized (DmEzQueryTableConverter.class) {
                if (instance == null) {
                    instance = new DmEzQueryTableConverter();
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
