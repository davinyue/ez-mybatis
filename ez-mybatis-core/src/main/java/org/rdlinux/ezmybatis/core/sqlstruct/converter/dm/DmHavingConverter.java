package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.Having;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleHavingConverter;

public class DmHavingConverter extends OracleHavingConverter implements Converter<Having> {
    private static volatile DmHavingConverter instance;

    protected DmHavingConverter() {
    }

    public static DmHavingConverter getInstance() {
        if (instance == null) {
            synchronized (DmHavingConverter.class) {
                if (instance == null) {
                    instance = new DmHavingConverter();
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
