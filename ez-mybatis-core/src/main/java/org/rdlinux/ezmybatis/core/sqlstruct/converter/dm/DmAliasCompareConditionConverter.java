package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleAliasCompareConditionConverter;

public class DmAliasCompareConditionConverter extends OracleAliasCompareConditionConverter {
    private static volatile DmAliasCompareConditionConverter instance;

    protected DmAliasCompareConditionConverter() {
    }

    public static DmAliasCompareConditionConverter getInstance() {
        if (instance == null) {
            synchronized (DmAliasCompareConditionConverter.class) {
                if (instance == null) {
                    instance = new DmAliasCompareConditionConverter();
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
