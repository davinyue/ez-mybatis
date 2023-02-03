package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSelectColumnConverter;

public class DmSelectColumnConverter extends OracleSelectColumnConverter {
    private static volatile DmSelectColumnConverter instance;

    protected DmSelectColumnConverter() {
    }

    public static DmSelectColumnConverter getInstance() {
        if (instance == null) {
            synchronized (DmSelectColumnConverter.class) {
                if (instance == null) {
                    instance = new DmSelectColumnConverter();
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
