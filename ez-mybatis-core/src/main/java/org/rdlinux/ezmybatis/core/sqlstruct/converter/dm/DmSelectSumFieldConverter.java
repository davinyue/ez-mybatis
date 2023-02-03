package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSelectSumFieldConverter;

public class DmSelectSumFieldConverter extends OracleSelectSumFieldConverter {
    private static volatile DmSelectSumFieldConverter instance;

    protected DmSelectSumFieldConverter() {
    }

    public static DmSelectSumFieldConverter getInstance() {
        if (instance == null) {
            synchronized (DmSelectSumFieldConverter.class) {
                if (instance == null) {
                    instance = new DmSelectSumFieldConverter();
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
