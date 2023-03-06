package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlFunctionConverter;

public class OracleFunctionConverter extends MySqlFunctionConverter implements Converter<Function> {
    private static volatile OracleFunctionConverter instance;

    protected OracleFunctionConverter() {
    }

    public static OracleFunctionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleFunctionConverter.class) {
                if (instance == null) {
                    instance = new OracleFunctionConverter();
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
