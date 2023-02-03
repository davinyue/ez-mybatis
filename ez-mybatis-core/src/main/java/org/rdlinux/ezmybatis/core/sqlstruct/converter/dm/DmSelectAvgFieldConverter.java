package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSelectAvgFieldConverter;

public class DmSelectAvgFieldConverter extends MySqlSelectAvgFieldConverter {
    private static volatile DmSelectAvgFieldConverter instance;

    protected DmSelectAvgFieldConverter() {
    }

    public static DmSelectAvgFieldConverter getInstance() {
        if (instance == null) {
            synchronized (DmSelectAvgFieldConverter.class) {
                if (instance == null) {
                    instance = new DmSelectAvgFieldConverter();
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
