package org.rdlinux.ezmybatis.core.sqlgenerate.postgre;

import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractEzDeleteToSql;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;

public class PostgreSqlEzDeleteToSql extends AbstractEzDeleteToSql {
    private static volatile PostgreSqlEzDeleteToSql instance;

    private PostgreSqlEzDeleteToSql() {
    }

    public static PostgreSqlEzDeleteToSql getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlEzDeleteToSql.class) {
                if (instance == null) {
                    instance = new PostgreSqlEzDeleteToSql();
                }
            }
        }
        return instance;
    }

    @Override
    protected void joinsToSql(SqlGenerateContext sqlGenerateContext, EzDelete delete) {
    }
}
