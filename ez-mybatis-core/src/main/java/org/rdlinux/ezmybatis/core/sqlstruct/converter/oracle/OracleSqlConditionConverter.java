package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSqlConditionConverter;

public class OracleSqlConditionConverter extends MySqlSqlConditionConverter {
    private static volatile OracleSqlConditionConverter instance;

    protected OracleSqlConditionConverter() {
    }

    public static OracleSqlConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSqlConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleSqlConditionConverter();
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
