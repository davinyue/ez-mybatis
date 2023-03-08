package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlFunctionUpdateColumnItemConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.FunctionUpdateColumnItem;

public class OracleFunctionUpdateColumnItemConverter extends MySqlFunctionUpdateColumnItemConverter
        implements Converter<FunctionUpdateColumnItem> {
    private static volatile OracleFunctionUpdateColumnItemConverter instance;

    protected OracleFunctionUpdateColumnItemConverter() {
    }

    public static OracleFunctionUpdateColumnItemConverter getInstance() {
        if (instance == null) {
            synchronized (OracleFunctionUpdateColumnItemConverter.class) {
                if (instance == null) {
                    instance = new OracleFunctionUpdateColumnItemConverter();
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
