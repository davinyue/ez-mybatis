package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSelectAvgColumnConverter;

public class DmSelectAvgColumnConverter extends OracleSelectAvgColumnConverter {
    private static volatile DmSelectAvgColumnConverter instance;

    protected DmSelectAvgColumnConverter() {
    }

    public static DmSelectAvgColumnConverter getInstance() {
        if (instance == null) {
            synchronized (DmSelectAvgColumnConverter.class) {
                if (instance == null) {
                    instance = new DmSelectAvgColumnConverter();
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
