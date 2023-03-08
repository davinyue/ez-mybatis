package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlFunctionUpdateFieldItemConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.FunctionUpdateFieldItem;

public class OracleFunctionUpdateFieldItemConverter extends MySqlFunctionUpdateFieldItemConverter
        implements Converter<FunctionUpdateFieldItem> {
    private static volatile OracleFunctionUpdateFieldItemConverter instance;

    protected OracleFunctionUpdateFieldItemConverter() {
    }

    public static OracleFunctionUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (OracleFunctionUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new OracleFunctionUpdateFieldItemConverter();
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
