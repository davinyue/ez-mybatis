package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlNormalFieldConditionConverter;

public class OracleNormalFieldConditionConverter extends MySqlNormalFieldConditionConverter {
    private static volatile OracleNormalFieldConditionConverter instance;

    protected OracleNormalFieldConditionConverter() {
    }

    public static OracleNormalFieldConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleNormalFieldConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleNormalFieldConditionConverter();
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
