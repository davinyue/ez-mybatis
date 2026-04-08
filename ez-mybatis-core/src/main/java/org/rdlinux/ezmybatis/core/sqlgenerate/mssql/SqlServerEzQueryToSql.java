package org.rdlinux.ezmybatis.core.sqlgenerate.mssql;

import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractEzQueryToSql;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.utils.AliasGenerate;

public class SqlServerEzQueryToSql extends AbstractEzQueryToSql {
    private static volatile SqlServerEzQueryToSql instance;

    private SqlServerEzQueryToSql() {
    }

    public static SqlServerEzQueryToSql getInstance() {
        if (instance == null) {
            synchronized (SqlServerEzQueryToSql.class) {
                if (instance == null) {
                    instance = new SqlServerEzQueryToSql();
                }
            }
        }
        return instance;
    }

    @Override
    protected void beforeUnionToSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        if (query.getUnions() != null && !query.getUnions().isEmpty()) {
            if ((query.getOrderBy() != null && query.getOrderBy().getItems() != null &&
                    !query.getOrderBy().getItems().isEmpty()) || query.getLimit() != null || query.getPage() != null) {
                sqlGenerateContext.getSqlBuilder().insert(0, "(SELECT * FROM (")
                        .append(") ").append(AliasGenerate.getAlias()).append(" ) ");
            } else {
                sqlGenerateContext.getSqlBuilder().insert(0, " (").append(") ");
            }
        }
    }
}
