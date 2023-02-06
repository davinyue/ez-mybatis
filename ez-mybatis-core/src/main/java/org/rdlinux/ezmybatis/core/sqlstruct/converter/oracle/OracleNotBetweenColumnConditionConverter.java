package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlNotBetweenColumnConditionConverter;

public class OracleNotBetweenColumnConditionConverter extends MySqlNotBetweenColumnConditionConverter {
    private static volatile OracleNotBetweenColumnConditionConverter instance;

    protected OracleNotBetweenColumnConditionConverter() {
    }

    public static OracleNotBetweenColumnConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleNotBetweenColumnConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleNotBetweenColumnConditionConverter();
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
