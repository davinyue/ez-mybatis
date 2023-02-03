package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSyntaxUpdateColumnItemConverter;

public class DmSyntaxUpdateColumnItemConverter extends OracleSyntaxUpdateColumnItemConverter {
    private static volatile DmSyntaxUpdateColumnItemConverter instance;

    protected DmSyntaxUpdateColumnItemConverter() {
    }

    public static DmSyntaxUpdateColumnItemConverter getInstance() {
        if (instance == null) {
            synchronized (DmSyntaxUpdateColumnItemConverter.class) {
                if (instance == null) {
                    instance = new DmSyntaxUpdateColumnItemConverter();
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
