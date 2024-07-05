package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleEzQueryConverter;

public class DmEzQueryConverter extends OracleEzQueryConverter {
    private static volatile DmEzQueryConverter instance;

    protected DmEzQueryConverter() {
    }

    public static DmEzQueryConverter getInstance() {
        if (instance == null) {
            synchronized (DmEzQueryConverter.class) {
                if (instance == null) {
                    instance = new DmEzQueryConverter();
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
