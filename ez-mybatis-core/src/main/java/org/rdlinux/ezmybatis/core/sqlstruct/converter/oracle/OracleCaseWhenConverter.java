package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlCaseWhenConverter;

public class OracleCaseWhenConverter extends MySqlCaseWhenConverter {
    private static volatile OracleCaseWhenConverter instance;

    protected OracleCaseWhenConverter() {
    }

    public static OracleCaseWhenConverter getInstance() {
        if (instance == null) {
            synchronized (OracleCaseWhenConverter.class) {
                if (instance == null) {
                    instance = new OracleCaseWhenConverter();
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
