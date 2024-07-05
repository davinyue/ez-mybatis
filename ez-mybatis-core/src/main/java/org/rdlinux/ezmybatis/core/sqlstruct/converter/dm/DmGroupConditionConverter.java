package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleGroupConditionConverter;

public class DmGroupConditionConverter extends OracleGroupConditionConverter {
    private static volatile DmGroupConditionConverter instance;

    protected DmGroupConditionConverter() {
    }

    public static DmGroupConditionConverter getInstance() {
        if (instance == null) {
            synchronized (DmGroupConditionConverter.class) {
                if (instance == null) {
                    instance = new DmGroupConditionConverter();
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
