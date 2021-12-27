package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractEzUpdateToSql;

public class MySqlEzUpdateToSql extends AbstractEzUpdateToSql {
    private static volatile MySqlEzUpdateToSql instance;

    private MySqlEzUpdateToSql() {
    }

    public static MySqlEzUpdateToSql getInstance() {
        if (instance == null) {
            synchronized (MySqlEzUpdateToSql.class) {
                if (instance == null) {
                    instance = new MySqlEzUpdateToSql();
                }
            }
        }
        return instance;
    }
}
