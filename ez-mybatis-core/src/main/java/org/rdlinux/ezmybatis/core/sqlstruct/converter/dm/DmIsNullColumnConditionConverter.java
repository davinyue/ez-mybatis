package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleIsNullColumnConditionConverter;

public class DmIsNullColumnConditionConverter extends OracleIsNullColumnConditionConverter {
    private static volatile DmIsNullColumnConditionConverter instance;

    protected DmIsNullColumnConditionConverter() {
    }

    public static DmIsNullColumnConditionConverter getInstance() {
        if (instance == null) {
            synchronized (DmIsNullColumnConditionConverter.class) {
                if (instance == null) {
                    instance = new DmIsNullColumnConditionConverter();
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
