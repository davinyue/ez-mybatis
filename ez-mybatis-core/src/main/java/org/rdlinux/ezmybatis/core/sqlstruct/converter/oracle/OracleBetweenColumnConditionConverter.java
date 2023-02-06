package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlBetweenColumnConditionConverter;

public class OracleBetweenColumnConditionConverter extends MySqlBetweenColumnConditionConverter {
    private static volatile OracleBetweenColumnConditionConverter instance;

    protected OracleBetweenColumnConditionConverter() {
    }

    public static OracleBetweenColumnConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleBetweenColumnConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleBetweenColumnConditionConverter();
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
