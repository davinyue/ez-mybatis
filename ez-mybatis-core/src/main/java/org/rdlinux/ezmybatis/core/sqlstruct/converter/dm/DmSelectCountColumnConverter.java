package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSelectCountColumnConverter;

public class DmSelectCountColumnConverter extends OracleSelectCountColumnConverter {
    private static volatile DmSelectCountColumnConverter instance;

    protected DmSelectCountColumnConverter() {
    }

    public static DmSelectCountColumnConverter getInstance() {
        if (instance == null) {
            synchronized (DmSelectCountColumnConverter.class) {
                if (instance == null) {
                    instance = new DmSelectCountColumnConverter();
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
