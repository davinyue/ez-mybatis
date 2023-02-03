package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSelectMinFieldConverter;

public class DmSelectMinFieldConverter extends OracleSelectMinFieldConverter {
    private static volatile DmSelectMinFieldConverter instance;

    protected DmSelectMinFieldConverter() {
    }

    public static DmSelectMinFieldConverter getInstance() {
        if (instance == null) {
            synchronized (DmSelectMinFieldConverter.class) {
                if (instance == null) {
                    instance = new DmSelectMinFieldConverter();
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
