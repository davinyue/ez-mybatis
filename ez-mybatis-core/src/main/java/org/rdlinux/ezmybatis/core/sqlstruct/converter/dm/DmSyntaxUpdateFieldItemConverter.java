package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSyntaxUpdateFieldItemConverter;

public class DmSyntaxUpdateFieldItemConverter extends OracleSyntaxUpdateFieldItemConverter {
    private static volatile DmSyntaxUpdateFieldItemConverter instance;

    protected DmSyntaxUpdateFieldItemConverter() {
    }

    public static DmSyntaxUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (DmSyntaxUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new DmSyntaxUpdateFieldItemConverter();
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
