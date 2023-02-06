package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlIsNotNullAliasConditionConverter;

public class OracleIsNotNullAliasConditionConverter extends MySqlIsNotNullAliasConditionConverter {
    private static volatile OracleIsNotNullAliasConditionConverter instance;

    protected OracleIsNotNullAliasConditionConverter() {
    }

    public static OracleIsNotNullAliasConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleIsNotNullAliasConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleIsNotNullAliasConditionConverter();
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
