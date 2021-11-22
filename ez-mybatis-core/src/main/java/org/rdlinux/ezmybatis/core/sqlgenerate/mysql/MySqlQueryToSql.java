package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractQueryToSql;

public class MySqlQueryToSql extends AbstractQueryToSql {
    private static volatile MySqlQueryToSql instance;

    private MySqlQueryToSql() {
    }

    public static MySqlQueryToSql getInstance() {
        if (instance == null) {
            synchronized ( MySqlQueryToSql.class ) {
                if (instance == null) {
                    instance = new MySqlQueryToSql();
                }
            }
        }
        return instance;
    }

    @Override
    public String getKeywordQM() {
        return "`";
    }

}
