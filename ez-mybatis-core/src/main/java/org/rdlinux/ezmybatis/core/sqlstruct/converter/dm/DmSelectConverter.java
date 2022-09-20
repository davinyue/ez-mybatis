package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSelectConverter;

public class DmSelectConverter extends OracleSelectConverter {
    private static volatile DmSelectConverter instance;

    protected DmSelectConverter() {
    }

    public static DmSelectConverter getInstance() {
        if (instance == null) {
            synchronized (DmSelectConverter.class) {
                if (instance == null) {
                    instance = new DmSelectConverter();
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
