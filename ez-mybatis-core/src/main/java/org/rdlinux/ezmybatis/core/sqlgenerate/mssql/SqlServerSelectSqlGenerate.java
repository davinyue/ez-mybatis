package org.rdlinux.ezmybatis.core.sqlgenerate.mssql;

import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractSelectSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlgenerate.mysql.MySqlInsertSqlGenerate;

public class SqlServerSelectSqlGenerate extends AbstractSelectSqlGenerate {
    private static volatile SqlServerSelectSqlGenerate instance;

    private SqlServerSelectSqlGenerate() {
    }

    public static SqlServerSelectSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (MySqlInsertSqlGenerate.class) {
                if (instance == null) {
                    instance = new SqlServerSelectSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getQuerySql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        return SqlServerEzQueryToSql.getInstance().toSql(sqlGenerateContext, query);
    }

    @Override
    public String getQueryCountSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        return SqlServerEzQueryToSql.getInstance().toCountSql(sqlGenerateContext, query);
    }
}
