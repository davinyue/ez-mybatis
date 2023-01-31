package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlCaseWhenConverter;

public class DmCaseWhenConverter extends MySqlCaseWhenConverter {
    private static volatile DmCaseWhenConverter instance;

    protected DmCaseWhenConverter() {
    }

    public static DmCaseWhenConverter getInstance() {
        if (instance == null) {
            synchronized (DmCaseWhenConverter.class) {
                if (instance == null) {
                    instance = new DmCaseWhenConverter();
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
