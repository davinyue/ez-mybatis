package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSqlConditionConverter;

public class DmSqlConditionConverter extends OracleSqlConditionConverter {
    private static volatile DmSqlConditionConverter instance;

    protected DmSqlConditionConverter() {
    }

    public static DmSqlConditionConverter getInstance() {
        if (instance == null) {
            synchronized (DmSqlConditionConverter.class) {
                if (instance == null) {
                    instance = new DmSqlConditionConverter();
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
