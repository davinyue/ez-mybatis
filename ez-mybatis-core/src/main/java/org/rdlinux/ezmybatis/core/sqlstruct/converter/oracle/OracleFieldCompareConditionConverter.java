package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlFieldCompareConditionConverter;

public class OracleFieldCompareConditionConverter extends MySqlFieldCompareConditionConverter {
    private static volatile OracleFieldCompareConditionConverter instance;

    protected OracleFieldCompareConditionConverter() {
    }

    public static OracleFieldCompareConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleFieldCompareConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleFieldCompareConditionConverter();
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
