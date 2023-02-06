package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleIsNullFieldConditionConverter;

public class DmIsNullFieldConditionConverter extends OracleIsNullFieldConditionConverter {
    private static volatile DmIsNullFieldConditionConverter instance;

    protected DmIsNullFieldConditionConverter() {
    }

    public static DmIsNullFieldConditionConverter getInstance() {
        if (instance == null) {
            synchronized (DmIsNullFieldConditionConverter.class) {
                if (instance == null) {
                    instance = new DmIsNullFieldConditionConverter();
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
