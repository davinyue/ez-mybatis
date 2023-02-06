package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlIsNotNullColumnConditionConverter;

public class OracleIsNotNullColumnConditionConverter extends MySqlIsNotNullColumnConditionConverter {
    private static volatile OracleIsNotNullColumnConditionConverter instance;

    protected OracleIsNotNullColumnConditionConverter() {
    }

    public static OracleIsNotNullColumnConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleIsNotNullColumnConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleIsNotNullColumnConditionConverter();
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
