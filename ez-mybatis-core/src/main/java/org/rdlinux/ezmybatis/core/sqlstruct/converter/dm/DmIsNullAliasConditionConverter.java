package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleIsNullAliasConditionConverter;

public class DmIsNullAliasConditionConverter extends OracleIsNullAliasConditionConverter {
    private static volatile DmIsNullAliasConditionConverter instance;

    protected DmIsNullAliasConditionConverter() {
    }

    public static DmIsNullAliasConditionConverter getInstance() {
        if (instance == null) {
            synchronized (DmIsNullAliasConditionConverter.class) {
                if (instance == null) {
                    instance = new DmIsNullAliasConditionConverter();
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
