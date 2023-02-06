package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlNotBetweenAliasConditionConverter;

public class OracleNotBetweenAliasConditionConverter extends MySqlNotBetweenAliasConditionConverter {
    private static volatile OracleNotBetweenAliasConditionConverter instance;

    protected OracleNotBetweenAliasConditionConverter() {
    }

    public static OracleNotBetweenAliasConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleNotBetweenAliasConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleNotBetweenAliasConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.ORACLE;
    }
}
