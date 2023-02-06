package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlFieldCompareColumnConditionConverter;

public class OracleFieldCompareColumnConditionConverter extends MySqlFieldCompareColumnConditionConverter {
    private static volatile OracleFieldCompareColumnConditionConverter instance;

    protected OracleFieldCompareColumnConditionConverter() {
    }

    public static OracleFieldCompareColumnConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleFieldCompareColumnConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleFieldCompareColumnConditionConverter();
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
