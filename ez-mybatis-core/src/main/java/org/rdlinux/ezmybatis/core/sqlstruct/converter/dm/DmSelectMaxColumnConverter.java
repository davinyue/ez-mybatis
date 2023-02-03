package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSelectMaxColumnConverter;

public class DmSelectMaxColumnConverter extends OracleSelectMaxColumnConverter {
    private static volatile DmSelectMaxColumnConverter instance;

    protected DmSelectMaxColumnConverter() {
    }

    public static DmSelectMaxColumnConverter getInstance() {
        if (instance == null) {
            synchronized (DmSelectMaxColumnConverter.class) {
                if (instance == null) {
                    instance = new DmSelectMaxColumnConverter();
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
