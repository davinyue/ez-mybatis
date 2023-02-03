package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleUpdateFieldItemConverter;

public class DmUpdateFieldItemConverter extends OracleUpdateFieldItemConverter {
    private static volatile DmUpdateFieldItemConverter instance;

    protected DmUpdateFieldItemConverter() {
    }

    public static DmUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (DmUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new DmUpdateFieldItemConverter();
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
