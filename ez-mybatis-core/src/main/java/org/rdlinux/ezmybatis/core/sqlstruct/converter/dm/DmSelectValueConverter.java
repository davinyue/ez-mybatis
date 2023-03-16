package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSelectValueConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectValue;

public class DmSelectValueConverter extends OracleSelectValueConverter implements Converter<SelectValue> {
    private static volatile DmSelectValueConverter instance;

    protected DmSelectValueConverter() {
    }

    public static DmSelectValueConverter getInstance() {
        if (instance == null) {
            synchronized (DmSelectValueConverter.class) {
                if (instance == null) {
                    instance = new DmSelectValueConverter();
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
