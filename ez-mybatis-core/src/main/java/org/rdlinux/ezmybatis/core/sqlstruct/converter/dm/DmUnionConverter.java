package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.Union;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleUnionConverter;

public class DmUnionConverter extends OracleUnionConverter implements Converter<Union> {
    private static volatile DmUnionConverter instance;

    protected DmUnionConverter() {
    }

    public static DmUnionConverter getInstance() {
        if (instance == null) {
            synchronized (DmUnionConverter.class) {
                if (instance == null) {
                    instance = new DmUnionConverter();
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
