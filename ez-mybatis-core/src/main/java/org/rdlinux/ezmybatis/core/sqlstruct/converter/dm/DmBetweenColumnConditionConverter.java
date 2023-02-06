package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlBetweenColumnConditionConverter;

public class DmBetweenColumnConditionConverter extends MySqlBetweenColumnConditionConverter {
    private static volatile DmBetweenColumnConditionConverter instance;

    protected DmBetweenColumnConditionConverter() {
    }

    public static DmBetweenColumnConditionConverter getInstance() {
        if (instance == null) {
            synchronized (DmBetweenColumnConditionConverter.class) {
                if (instance == null) {
                    instance = new DmBetweenColumnConditionConverter();
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
