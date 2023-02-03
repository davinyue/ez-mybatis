package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSelectCountFieldConverter;

public class DmSelectCountFieldConverter extends OracleSelectCountFieldConverter {
    private static volatile DmSelectCountFieldConverter instance;

    protected DmSelectCountFieldConverter() {
    }

    public static DmSelectCountFieldConverter getInstance() {
        if (instance == null) {
            synchronized (DmSelectCountFieldConverter.class) {
                if (instance == null) {
                    instance = new DmSelectCountFieldConverter();
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
