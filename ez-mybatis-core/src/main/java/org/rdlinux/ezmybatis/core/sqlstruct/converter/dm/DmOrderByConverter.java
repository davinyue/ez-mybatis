package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleOrderByConverter;

public class DmOrderByConverter extends OracleOrderByConverter {
    private static volatile DmOrderByConverter instance;

    protected DmOrderByConverter() {
    }

    public static DmOrderByConverter getInstance() {
        if (instance == null) {
            synchronized (DmOrderByConverter.class) {
                if (instance == null) {
                    instance = new DmOrderByConverter();
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
