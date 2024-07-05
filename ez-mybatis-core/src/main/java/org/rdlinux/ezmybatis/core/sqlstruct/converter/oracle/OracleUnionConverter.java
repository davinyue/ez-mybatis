package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.Union;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlUnionConverter;

public class OracleUnionConverter extends MySqlUnionConverter implements Converter<Union> {
    private static volatile OracleUnionConverter instance;

    protected OracleUnionConverter() {
    }

    public static OracleUnionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleUnionConverter.class) {
                if (instance == null) {
                    instance = new OracleUnionConverter();
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
