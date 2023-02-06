package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleNotBetweenColumnConditionConverter;

public class DmNotBetweenColumnConditionConverter extends OracleNotBetweenColumnConditionConverter {
    private static volatile DmNotBetweenColumnConditionConverter instance;

    protected DmNotBetweenColumnConditionConverter() {
    }

    public static DmNotBetweenColumnConditionConverter getInstance() {
        if (instance == null) {
            synchronized (DmNotBetweenColumnConditionConverter.class) {
                if (instance == null) {
                    instance = new DmNotBetweenColumnConditionConverter();
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
