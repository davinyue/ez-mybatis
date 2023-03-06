package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleFunctionConverter;

public class DmFunctionConverter extends OracleFunctionConverter implements Converter<Function> {
    private static volatile DmFunctionConverter instance;

    protected DmFunctionConverter() {
    }

    public static DmFunctionConverter getInstance() {
        if (instance == null) {
            synchronized (DmFunctionConverter.class) {
                if (instance == null) {
                    instance = new DmFunctionConverter();
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
