package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlBetweenAliasConditionConverter;

public class OracleBetweenAliasConditionConverter extends MySqlBetweenAliasConditionConverter {
    private static volatile OracleBetweenAliasConditionConverter instance;

    protected OracleBetweenAliasConditionConverter() {
    }

    public static OracleBetweenAliasConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleBetweenAliasConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleBetweenAliasConditionConverter();
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
