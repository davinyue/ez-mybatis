package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.Where;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlWhereConverter;

public class OracleWhereConverter extends MySqlWhereConverter implements Converter<Where> {
    private static volatile OracleWhereConverter instance;

    protected OracleWhereConverter() {
    }

    public static OracleWhereConverter getInstance() {
        if (instance == null) {
            synchronized (OracleWhereConverter.class) {
                if (instance == null) {
                    instance = new OracleWhereConverter();
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
