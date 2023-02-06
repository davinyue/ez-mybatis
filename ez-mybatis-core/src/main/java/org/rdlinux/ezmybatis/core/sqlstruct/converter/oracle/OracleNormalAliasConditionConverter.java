package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlNormalAliasConditionConverter;

public class OracleNormalAliasConditionConverter extends MySqlNormalAliasConditionConverter {
    private static volatile OracleNormalAliasConditionConverter instance;

    protected OracleNormalAliasConditionConverter() {
    }

    public static OracleNormalAliasConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleNormalAliasConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleNormalAliasConditionConverter();
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
