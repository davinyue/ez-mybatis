package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleCaseWhenUpdateColumnItemConverter;

public class DmCaseWhenUpdateColumnItemConverter extends OracleCaseWhenUpdateColumnItemConverter {
    private static volatile DmCaseWhenUpdateColumnItemConverter instance;

    protected DmCaseWhenUpdateColumnItemConverter() {
    }

    public static DmCaseWhenUpdateColumnItemConverter getInstance() {
        if (instance == null) {
            synchronized (DmCaseWhenUpdateColumnItemConverter.class) {
                if (instance == null) {
                    instance = new DmCaseWhenUpdateColumnItemConverter();
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
