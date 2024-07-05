package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractEzQueryToSql;

public class MySqlEzQueryToSql extends AbstractEzQueryToSql {
    private static volatile MySqlEzQueryToSql instance;

    private MySqlEzQueryToSql() {
    }

    public static MySqlEzQueryToSql getInstance() {
        if (instance == null) {
            synchronized (MySqlEzQueryToSql.class) {
                if (instance == null) {
                    instance = new MySqlEzQueryToSql();
                }
            }
        }
        return instance;
    }
}
