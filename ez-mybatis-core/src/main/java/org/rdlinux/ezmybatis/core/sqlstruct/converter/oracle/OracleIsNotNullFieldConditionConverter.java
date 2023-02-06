package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlIsNotNullFieldConditionConverter;

public class OracleIsNotNullFieldConditionConverter extends MySqlIsNotNullFieldConditionConverter {
    private static volatile OracleIsNotNullFieldConditionConverter instance;

    protected OracleIsNotNullFieldConditionConverter() {
    }

    public static OracleIsNotNullFieldConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleIsNotNullFieldConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleIsNotNullFieldConditionConverter();
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
