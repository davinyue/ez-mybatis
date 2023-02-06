package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlNormalColumnConditionConverter;

public class OracleNormalColumnConditionConverter extends MySqlNormalColumnConditionConverter {
    private static volatile OracleNormalColumnConditionConverter instance;

    protected OracleNormalColumnConditionConverter() {
    }

    public static OracleNormalColumnConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleNormalColumnConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleNormalColumnConditionConverter();
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
