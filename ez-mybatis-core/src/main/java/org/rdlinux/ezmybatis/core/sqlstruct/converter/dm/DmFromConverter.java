package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.From;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleFromConverter;

public class DmFromConverter extends OracleFromConverter implements Converter<From> {
    private static volatile DmFromConverter instance;

    protected DmFromConverter() {
    }

    public static DmFromConverter getInstance() {
        if (instance == null) {
            synchronized (DmFromConverter.class) {
                if (instance == null) {
                    instance = new DmFromConverter();
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
