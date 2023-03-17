package org.rdlinux.ezmybatis.expand.oracle.converter;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSelectValueConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectValue;

public class OracleMergeConverter extends OracleSelectValueConverter implements Converter<SelectValue> {
    private static volatile OracleMergeConverter instance;

    protected OracleMergeConverter() {
    }

    public static OracleMergeConverter getInstance() {
        if (instance == null) {
            synchronized (OracleMergeConverter.class) {
                if (instance == null) {
                    instance = new OracleMergeConverter();
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
