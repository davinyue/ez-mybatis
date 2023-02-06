package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlIsNullFieldConditionConverter;

public class OracleIsNullFieldConditionConverter extends MySqlIsNullFieldConditionConverter {
    private static volatile OracleIsNullFieldConditionConverter instance;

    protected OracleIsNullFieldConditionConverter() {
    }

    public static OracleIsNullFieldConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleIsNullFieldConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleIsNullFieldConditionConverter();
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
