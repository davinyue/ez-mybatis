package org.rdlinux.ezmybatis.core.sqlstruct.converter.mssql;

import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.SqlTable;

public class SqlServerSqlTableConverter extends AbstractConverter<SqlTable> implements Converter<SqlTable> {
    private static volatile SqlServerSqlTableConverter instance;

    protected SqlServerSqlTableConverter() {
    }

    public static SqlServerSqlTableConverter getInstance() {
        if (instance == null) {
            synchronized (SqlServerSqlTableConverter.class) {
                if (instance == null) {
                    instance = new SqlServerSqlTableConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, SqlTable table, SqlGenerateContext sqlGenerateContext) {
        sqlGenerateContext.getSqlBuilder().append(" (").append(table.getSql()).append(") ");
        if (type == Type.SELECT) {
            sqlGenerateContext.getSqlBuilder().append(table.getAlias()).append(" ");
        }
    }

}
