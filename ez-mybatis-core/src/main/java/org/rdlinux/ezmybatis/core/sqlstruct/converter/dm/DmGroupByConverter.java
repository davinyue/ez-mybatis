package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleGroupByConverter;

public class DmGroupByConverter extends OracleGroupByConverter {
    private static volatile DmGroupByConverter instance;

    protected DmGroupByConverter() {
    }

    public static DmGroupByConverter getInstance() {
        if (instance == null) {
            synchronized (DmGroupByConverter.class) {
                if (instance == null) {
                    instance = new DmGroupByConverter();
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
