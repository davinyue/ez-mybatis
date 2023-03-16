package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSelectValueConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectValue;

public class OracleSelectValueConverter extends MySqlSelectValueConverter implements Converter<SelectValue> {
    private static volatile OracleSelectValueConverter instance;

    protected OracleSelectValueConverter() {
    }

    public static OracleSelectValueConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSelectValueConverter.class) {
                if (instance == null) {
                    instance = new OracleSelectValueConverter();
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
