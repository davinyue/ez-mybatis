package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleCaseWhenUpdateFieldItemConverter;

public class DmCaseWhenUpdateFieldItemConverter extends OracleCaseWhenUpdateFieldItemConverter {
    private static volatile DmCaseWhenUpdateFieldItemConverter instance;

    protected DmCaseWhenUpdateFieldItemConverter() {
    }

    public static DmCaseWhenUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (DmCaseWhenUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new DmCaseWhenUpdateFieldItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.DM;
    }
}
