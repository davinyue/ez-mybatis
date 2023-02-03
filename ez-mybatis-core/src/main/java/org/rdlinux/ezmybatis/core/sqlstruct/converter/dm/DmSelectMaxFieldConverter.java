package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSelectMaxFieldConverter;

public class DmSelectMaxFieldConverter extends OracleSelectMaxFieldConverter {
    private static volatile DmSelectMaxFieldConverter instance;

    protected DmSelectMaxFieldConverter() {
    }

    public static DmSelectMaxFieldConverter getInstance() {
        if (instance == null) {
            synchronized (DmSelectMaxFieldConverter.class) {
                if (instance == null) {
                    instance = new DmSelectMaxFieldConverter();
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
