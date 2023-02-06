package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlColumnCompareFieldConditionConverter;

public class OracleColumnCompareFieldConditionConverter extends MySqlColumnCompareFieldConditionConverter {
    private static volatile OracleColumnCompareFieldConditionConverter instance;

    protected OracleColumnCompareFieldConditionConverter() {
    }

    public static OracleColumnCompareFieldConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleColumnCompareFieldConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleColumnCompareFieldConditionConverter();
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
