package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleUpdateColumnItemConverter;

public class DmUpdateColumnItemConverter extends OracleUpdateColumnItemConverter {
    private static volatile DmUpdateColumnItemConverter instance;

    protected DmUpdateColumnItemConverter() {
    }

    public static DmUpdateColumnItemConverter getInstance() {
        if (instance == null) {
            synchronized (DmUpdateColumnItemConverter.class) {
                if (instance == null) {
                    instance = new DmUpdateColumnItemConverter();
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
