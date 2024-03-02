package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlPageConverter;

public class DmPageConverter extends MySqlPageConverter {
    private static volatile DmPageConverter instance;

    protected DmPageConverter() {
    }

    public static DmPageConverter getInstance() {
        if (instance == null) {
            synchronized (DmPageConverter.class) {
                if (instance == null) {
                    instance = new DmPageConverter();
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
