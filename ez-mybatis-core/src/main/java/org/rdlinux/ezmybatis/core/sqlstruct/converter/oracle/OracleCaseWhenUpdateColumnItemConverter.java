package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlCaseWhenUpdateColumnItemConverter;

public class OracleCaseWhenUpdateColumnItemConverter extends MySqlCaseWhenUpdateColumnItemConverter {
    private static volatile OracleCaseWhenUpdateColumnItemConverter instance;

    protected OracleCaseWhenUpdateColumnItemConverter() {
    }

    public static OracleCaseWhenUpdateColumnItemConverter getInstance() {
        if (instance == null) {
            synchronized (OracleCaseWhenUpdateColumnItemConverter.class) {
                if (instance == null) {
                    instance = new OracleCaseWhenUpdateColumnItemConverter();
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
