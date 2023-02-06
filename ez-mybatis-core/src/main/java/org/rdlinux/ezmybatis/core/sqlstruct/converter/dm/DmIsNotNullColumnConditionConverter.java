package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleIsNotNullColumnConditionConverter;

public class DmIsNotNullColumnConditionConverter extends OracleIsNotNullColumnConditionConverter {
    private static volatile DmIsNotNullColumnConditionConverter instance;

    protected DmIsNotNullColumnConditionConverter() {
    }

    public static DmIsNotNullColumnConditionConverter getInstance() {
        if (instance == null) {
            synchronized (DmIsNotNullColumnConditionConverter.class) {
                if (instance == null) {
                    instance = new DmIsNotNullColumnConditionConverter();
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
