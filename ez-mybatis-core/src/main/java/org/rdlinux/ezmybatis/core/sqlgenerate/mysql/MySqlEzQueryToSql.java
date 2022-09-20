package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractEzQueryToSql;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Alias;

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

    @Override
    public String toCountSql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query) {
        String sql = super.toCountSql(configuration, paramHolder, query);
        if (query.getGroupBy() != null && !query.getGroupBy().getItems().isEmpty()) {
            return "SELECT COUNT(1) FROM ( " + sql + " ) " + Alias.getAlias();
        } else {
            return sql;
        }
    }
}
