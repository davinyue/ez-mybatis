package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlGroupConditionConverter;

public class OracleGroupConditionConverter extends MySqlGroupConditionConverter {
    private static volatile OracleGroupConditionConverter instance;

    protected OracleGroupConditionConverter() {
    }

    public static OracleGroupConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleGroupConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleGroupConditionConverter();
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
