package org.rdlinux.ezmybatis.core.sqlgenerate.mssql;

import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractEzUpdateToSql;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;

public class SqlServerEzUpdateToSql extends AbstractEzUpdateToSql {
    private static volatile SqlServerEzUpdateToSql instance;

    private SqlServerEzUpdateToSql() {
    }

    public static SqlServerEzUpdateToSql getInstance() {
        if (instance == null) {
            synchronized (SqlServerEzUpdateToSql.class) {
                if (instance == null) {
                    instance = new SqlServerEzUpdateToSql();
                }
            }
        }
        return instance;
    }

    @Override
    protected void joinsToSql(SqlGenerateContext sqlGenerateContext, EzUpdate update) {
    }
}
