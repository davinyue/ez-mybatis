package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleFunctionUpdateFieldItemConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.FunctionUpdateFieldItem;

public class DmFunctionUpdateFieldItemConverter extends OracleFunctionUpdateFieldItemConverter
        implements Converter<FunctionUpdateFieldItem> {
    private static volatile DmFunctionUpdateFieldItemConverter instance;

    protected DmFunctionUpdateFieldItemConverter() {
    }

    public static DmFunctionUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (DmFunctionUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new DmFunctionUpdateFieldItemConverter();
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
