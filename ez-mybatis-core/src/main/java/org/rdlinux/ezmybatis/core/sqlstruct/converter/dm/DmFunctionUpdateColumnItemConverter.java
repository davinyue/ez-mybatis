package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleFunctionUpdateColumnItemConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.FunctionUpdateColumnItem;

public class DmFunctionUpdateColumnItemConverter extends OracleFunctionUpdateColumnItemConverter
        implements Converter<FunctionUpdateColumnItem> {
    private static volatile DmFunctionUpdateColumnItemConverter instance;

    protected DmFunctionUpdateColumnItemConverter() {
    }

    public static DmFunctionUpdateColumnItemConverter getInstance() {
        if (instance == null) {
            synchronized (DmFunctionUpdateColumnItemConverter.class) {
                if (instance == null) {
                    instance = new DmFunctionUpdateColumnItemConverter();
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
