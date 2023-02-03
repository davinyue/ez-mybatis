package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSelectMinColumnConverter;

public class DmSelectMinColumnConverter extends OracleSelectMinColumnConverter {
    private static volatile DmSelectMinColumnConverter instance;

    protected DmSelectMinColumnConverter() {
    }

    public static DmSelectMinColumnConverter getInstance() {
        if (instance == null) {
            synchronized (DmSelectMinColumnConverter.class) {
                if (instance == null) {
                    instance = new DmSelectMinColumnConverter();
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
