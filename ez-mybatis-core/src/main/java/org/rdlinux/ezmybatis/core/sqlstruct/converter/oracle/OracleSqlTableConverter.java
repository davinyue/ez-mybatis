package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSqlTableConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.SqlTable;

public class OracleSqlTableConverter extends MySqlSqlTableConverter implements Converter<SqlTable> {
    private static volatile OracleSqlTableConverter instance;

    protected OracleSqlTableConverter() {
    }

    public static OracleSqlTableConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSqlTableConverter.class) {
                if (instance == null) {
                    instance = new OracleSqlTableConverter();
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
