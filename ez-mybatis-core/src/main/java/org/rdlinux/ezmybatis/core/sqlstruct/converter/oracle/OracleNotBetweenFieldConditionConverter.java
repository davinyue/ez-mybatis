package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlNotBetweenFieldConditionConverter;

public class OracleNotBetweenFieldConditionConverter extends MySqlNotBetweenFieldConditionConverter {
    private static volatile OracleNotBetweenFieldConditionConverter instance;

    protected OracleNotBetweenFieldConditionConverter() {
    }

    public static OracleNotBetweenFieldConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleNotBetweenFieldConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleNotBetweenFieldConditionConverter();
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
