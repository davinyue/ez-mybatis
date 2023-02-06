package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleBetweenAliasConditionConverter;

public class DmBetweenAliasConditionConverter extends OracleBetweenAliasConditionConverter {
    private static volatile DmBetweenAliasConditionConverter instance;

    protected DmBetweenAliasConditionConverter() {
    }

    public static DmBetweenAliasConditionConverter getInstance() {
        if (instance == null) {
            synchronized (DmBetweenAliasConditionConverter.class) {
                if (instance == null) {
                    instance = new DmBetweenAliasConditionConverter();
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
