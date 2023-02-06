package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleNormalAliasConditionConverter;

public class DmNormalAliasConditionConverter extends OracleNormalAliasConditionConverter {
    private static volatile DmNormalAliasConditionConverter instance;

    protected DmNormalAliasConditionConverter() {
    }

    public static DmNormalAliasConditionConverter getInstance() {
        if (instance == null) {
            synchronized (DmNormalAliasConditionConverter.class) {
                if (instance == null) {
                    instance = new DmNormalAliasConditionConverter();
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
