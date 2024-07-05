package org.rdlinux.ezmybatis.core.sqlgenerate.postgre;

import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractEzQueryToSql;

public class PostgreSqlEzQueryToSql extends AbstractEzQueryToSql {
    private static volatile PostgreSqlEzQueryToSql instance;

    private PostgreSqlEzQueryToSql() {
    }

    public static PostgreSqlEzQueryToSql getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlEzQueryToSql.class) {
                if (instance == null) {
                    instance = new PostgreSqlEzQueryToSql();
                }
            }
        }
        return instance;
    }
}
