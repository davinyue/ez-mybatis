package org.rdlinux.ezmybatis.core.sqlgenerate.mssql;

import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractEzDeleteToSql;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;

public class SqlServerEzDeleteToSql extends AbstractEzDeleteToSql {
    private static volatile SqlServerEzDeleteToSql instance;

    private SqlServerEzDeleteToSql() {
    }

    public static SqlServerEzDeleteToSql getInstance() {
        if (instance == null) {
            synchronized (SqlServerEzDeleteToSql.class) {
                if (instance == null) {
                    instance = new SqlServerEzDeleteToSql();
                }
            }
        }
        return instance;
    }

    @Override
    protected void joinsToSql(SqlGenerateContext sqlGenerateContext, EzDelete delete) {
    }
}
