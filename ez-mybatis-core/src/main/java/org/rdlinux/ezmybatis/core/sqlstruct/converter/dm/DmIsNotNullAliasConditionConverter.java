package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleIsNotNullAliasConditionConverter;

public class DmIsNotNullAliasConditionConverter extends OracleIsNotNullAliasConditionConverter {
    private static volatile DmIsNotNullAliasConditionConverter instance;

    protected DmIsNotNullAliasConditionConverter() {
    }

    public static DmIsNotNullAliasConditionConverter getInstance() {
        if (instance == null) {
            synchronized (DmIsNotNullAliasConditionConverter.class) {
                if (instance == null) {
                    instance = new DmIsNotNullAliasConditionConverter();
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
