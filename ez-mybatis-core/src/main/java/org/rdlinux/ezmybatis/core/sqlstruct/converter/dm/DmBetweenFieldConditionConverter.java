package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleBetweenFieldConditionConverter;

public class DmBetweenFieldConditionConverter extends OracleBetweenFieldConditionConverter {
    private static volatile DmBetweenFieldConditionConverter instance;

    protected DmBetweenFieldConditionConverter() {
    }

    public static DmBetweenFieldConditionConverter getInstance() {
        if (instance == null) {
            synchronized (DmBetweenFieldConditionConverter.class) {
                if (instance == null) {
                    instance = new DmBetweenFieldConditionConverter();
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
