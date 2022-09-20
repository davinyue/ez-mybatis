package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.From;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlFromConverter;

public class OracleFromConverter extends MySqlFromConverter implements Converter<From> {
    private static volatile OracleFromConverter instance;

    protected OracleFromConverter() {
    }

    public static OracleFromConverter getInstance() {
        if (instance == null) {
            synchronized (OracleFromConverter.class) {
                if (instance == null) {
                    instance = new OracleFromConverter();
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
