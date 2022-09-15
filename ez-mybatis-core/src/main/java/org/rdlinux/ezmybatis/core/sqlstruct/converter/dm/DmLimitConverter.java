package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleLimitConverter;

public class DmLimitConverter extends OracleLimitConverter {
    private static volatile DmLimitConverter instance;

    protected DmLimitConverter() {
    }

    public static DmLimitConverter getInstance() {
        if (instance == null) {
            synchronized (DmLimitConverter.class) {
                if (instance == null) {
                    instance = new DmLimitConverter();
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
