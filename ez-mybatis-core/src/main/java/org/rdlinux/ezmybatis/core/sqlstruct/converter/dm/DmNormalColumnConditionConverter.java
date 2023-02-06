package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleNormalColumnConditionConverter;

public class DmNormalColumnConditionConverter extends OracleNormalColumnConditionConverter {
    private static volatile DmNormalColumnConditionConverter instance;

    protected DmNormalColumnConditionConverter() {
    }

    public static DmNormalColumnConditionConverter getInstance() {
        if (instance == null) {
            synchronized (DmNormalColumnConditionConverter.class) {
                if (instance == null) {
                    instance = new DmNormalColumnConditionConverter();
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
