package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlColumnCompareConditionConverter;

public class OracleColumnCompareConditionConverter extends MySqlColumnCompareConditionConverter {
    private static volatile OracleColumnCompareConditionConverter instance;

    protected OracleColumnCompareConditionConverter() {
    }

    public static OracleColumnCompareConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleColumnCompareConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleColumnCompareConditionConverter();
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
