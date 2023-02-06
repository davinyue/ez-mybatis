package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleNotBetweenFieldConditionConverter;

public class DmNotBetweenFieldConditionConverter extends OracleNotBetweenFieldConditionConverter {
    private static volatile DmNotBetweenFieldConditionConverter instance;

    protected DmNotBetweenFieldConditionConverter() {
    }

    public static DmNotBetweenFieldConditionConverter getInstance() {
        if (instance == null) {
            synchronized (DmNotBetweenFieldConditionConverter.class) {
                if (instance == null) {
                    instance = new DmNotBetweenFieldConditionConverter();
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
