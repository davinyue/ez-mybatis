package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleNotBetweenAliasConditionConverter;

public class DmNotBetweenAliasConditionConverter extends OracleNotBetweenAliasConditionConverter {
    private static volatile DmNotBetweenAliasConditionConverter instance;

    protected DmNotBetweenAliasConditionConverter() {
    }

    public static DmNotBetweenAliasConditionConverter getInstance() {
        if (instance == null) {
            synchronized (DmNotBetweenAliasConditionConverter.class) {
                if (instance == null) {
                    instance = new DmNotBetweenAliasConditionConverter();
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
