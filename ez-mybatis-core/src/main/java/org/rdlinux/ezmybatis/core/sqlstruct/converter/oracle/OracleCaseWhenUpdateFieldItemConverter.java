package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlCaseWhenUpdateFieldItemConverter;

public class OracleCaseWhenUpdateFieldItemConverter extends MySqlCaseWhenUpdateFieldItemConverter {
    private static volatile OracleCaseWhenUpdateFieldItemConverter instance;

    protected OracleCaseWhenUpdateFieldItemConverter() {
    }

    public static OracleCaseWhenUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (OracleCaseWhenUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new OracleCaseWhenUpdateFieldItemConverter();
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
