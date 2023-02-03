package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSelectFieldConverter;

public class DmSelectFieldConverter extends OracleSelectFieldConverter {
    private static volatile DmSelectFieldConverter instance;

    protected DmSelectFieldConverter() {
    }

    public static DmSelectFieldConverter getInstance() {
        if (instance == null) {
            synchronized (DmSelectFieldConverter.class) {
                if (instance == null) {
                    instance = new DmSelectFieldConverter();
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
