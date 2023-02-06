package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleNormalFieldConditionConverter;

public class DmNormalFieldConditionConverter extends OracleNormalFieldConditionConverter {
    private static volatile DmNormalFieldConditionConverter instance;

    protected DmNormalFieldConditionConverter() {
    }

    public static DmNormalFieldConditionConverter getInstance() {
        if (instance == null) {
            synchronized (DmNormalFieldConditionConverter.class) {
                if (instance == null) {
                    instance = new DmNormalFieldConditionConverter();
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
