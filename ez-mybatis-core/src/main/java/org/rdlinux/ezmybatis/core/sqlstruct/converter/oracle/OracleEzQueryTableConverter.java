package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlEzQueryTableConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EzQueryTable;

public class OracleEzQueryTableConverter extends MySqlEzQueryTableConverter implements Converter<EzQueryTable> {
    private static volatile OracleEzQueryTableConverter instance;

    protected OracleEzQueryTableConverter() {
    }

    public static OracleEzQueryTableConverter getInstance() {
        if (instance == null) {
            synchronized (OracleEzQueryTableConverter.class) {
                if (instance == null) {
                    instance = new OracleEzQueryTableConverter();
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
