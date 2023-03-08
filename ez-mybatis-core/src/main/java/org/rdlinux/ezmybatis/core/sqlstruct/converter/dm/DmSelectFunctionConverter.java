package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSelectFunctionConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectFunction;

public class DmSelectFunctionConverter extends OracleSelectFunctionConverter implements Converter<SelectFunction> {
    private static volatile DmSelectFunctionConverter instance;

    protected DmSelectFunctionConverter() {
    }

    public static DmSelectFunctionConverter getInstance() {
        if (instance == null) {
            synchronized (DmSelectFunctionConverter.class) {
                if (instance == null) {
                    instance = new DmSelectFunctionConverter();
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
