package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.Where;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleWhereConverter;

public class DmWhereConverter extends OracleWhereConverter implements Converter<Where> {
    private static volatile DmWhereConverter instance;

    protected DmWhereConverter() {
    }

    public static DmWhereConverter getInstance() {
        if (instance == null) {
            synchronized (DmWhereConverter.class) {
                if (instance == null) {
                    instance = new DmWhereConverter();
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
