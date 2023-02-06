package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlIsNullColumnConditionConverter;

public class OracleIsNullColumnConditionConverter extends MySqlIsNullColumnConditionConverter {
    private static volatile OracleIsNullColumnConditionConverter instance;

    protected OracleIsNullColumnConditionConverter() {
    }

    public static OracleIsNullColumnConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleIsNullColumnConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleIsNullColumnConditionConverter();
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
