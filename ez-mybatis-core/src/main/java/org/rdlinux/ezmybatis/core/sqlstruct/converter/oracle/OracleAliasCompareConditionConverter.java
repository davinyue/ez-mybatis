package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlAliasCompareConditionConverter;

public class OracleAliasCompareConditionConverter extends MySqlAliasCompareConditionConverter {
    private static volatile OracleAliasCompareConditionConverter instance;

    protected OracleAliasCompareConditionConverter() {
    }

    public static OracleAliasCompareConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleAliasCompareConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleAliasCompareConditionConverter();
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
