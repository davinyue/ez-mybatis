package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSelectAllItemConverter;

public class DmSelectAllItemConverter extends OracleSelectAllItemConverter {
    private static volatile DmSelectAllItemConverter instance;

    protected DmSelectAllItemConverter() {
    }

    public static DmSelectAllItemConverter getInstance() {
        if (instance == null) {
            synchronized (DmSelectAllItemConverter.class) {
                if (instance == null) {
                    instance = new DmSelectAllItemConverter();
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
