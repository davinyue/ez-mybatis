package org.rdlinux.ezmybatis.core.sqlstruct.converter.mssql;

import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.OrderBy;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlOrderByConverter;

public class SqlServerOrderByConverter extends MySqlOrderByConverter implements Converter<OrderBy> {
    private static volatile SqlServerOrderByConverter instance;

    protected SqlServerOrderByConverter() {
    }

    public static SqlServerOrderByConverter getInstance() {
        if (instance == null) {
            synchronized (SqlServerOrderByConverter.class) {
                if (instance == null) {
                    instance = new SqlServerOrderByConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, OrderBy orderBy, SqlGenerateContext sqlGenerateContext) {
        EzQuery<?> query = orderBy.getQuery();
        if (query.getPage() == null && query.getLimit() == null) {
            return;
        }
        if (orderBy.getItems() == null || orderBy.getItems().isEmpty()) {
            sqlGenerateContext.getSqlBuilder().append(" ORDER BY 1 ");
        } else {
            super.doBuildSql(type, orderBy, sqlGenerateContext);
        }
    }

}
