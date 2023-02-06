package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlIsNullAliasConditionConverter;

public class OracleIsNullAliasConditionConverter extends MySqlIsNullAliasConditionConverter {
    private static volatile OracleIsNullAliasConditionConverter instance;

    protected OracleIsNullAliasConditionConverter() {
    }

    public static OracleIsNullAliasConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleIsNullAliasConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleIsNullAliasConditionConverter();
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
