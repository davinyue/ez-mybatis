package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSelectFunctionConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectFunction;

public class OracleSelectFunctionConverter extends MySqlSelectFunctionConverter implements Converter<SelectFunction> {
    private static volatile OracleSelectFunctionConverter instance;

    protected OracleSelectFunctionConverter() {
    }

    public static OracleSelectFunctionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSelectFunctionConverter.class) {
                if (instance == null) {
                    instance = new OracleSelectFunctionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.ORACLE;
    }
}
