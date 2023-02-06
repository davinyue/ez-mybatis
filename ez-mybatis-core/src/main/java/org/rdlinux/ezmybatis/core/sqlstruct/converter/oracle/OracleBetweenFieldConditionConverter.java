package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlBetweenFieldConditionConverter;

public class OracleBetweenFieldConditionConverter extends MySqlBetweenFieldConditionConverter {
    private static volatile OracleBetweenFieldConditionConverter instance;

    protected OracleBetweenFieldConditionConverter() {
    }

    public static OracleBetweenFieldConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleBetweenFieldConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleBetweenFieldConditionConverter();
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
