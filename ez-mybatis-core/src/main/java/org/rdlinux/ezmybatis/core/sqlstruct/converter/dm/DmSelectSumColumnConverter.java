package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSelectSumColumnConverter;

public class DmSelectSumColumnConverter extends OracleSelectSumColumnConverter {
    private static volatile DmSelectSumColumnConverter instance;

    protected DmSelectSumColumnConverter() {
    }

    public static DmSelectSumColumnConverter getInstance() {
        if (instance == null) {
            synchronized (DmSelectSumColumnConverter.class) {
                if (instance == null) {
                    instance = new DmSelectSumColumnConverter();
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
