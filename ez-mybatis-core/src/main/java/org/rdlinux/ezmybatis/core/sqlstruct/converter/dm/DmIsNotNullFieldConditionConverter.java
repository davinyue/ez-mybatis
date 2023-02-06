package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleIsNotNullFieldConditionConverter;

public class DmIsNotNullFieldConditionConverter extends OracleIsNotNullFieldConditionConverter {
    private static volatile DmIsNotNullFieldConditionConverter instance;

    protected DmIsNotNullFieldConditionConverter() {
    }

    public static DmIsNotNullFieldConditionConverter getInstance() {
        if (instance == null) {
            synchronized (DmIsNotNullFieldConditionConverter.class) {
                if (instance == null) {
                    instance = new DmIsNotNullFieldConditionConverter();
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
