package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractSelectSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;

public class MySqlSelectSqlGenerate extends AbstractSelectSqlGenerate {
    private static volatile MySqlSelectSqlGenerate instance;

    private MySqlSelectSqlGenerate() {
    }

    public static MySqlSelectSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (MySqlInsertSqlGenerate.class) {
                if (instance == null) {
                    instance = new MySqlSelectSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getQuerySql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query) {
        return MySqlEzQueryToSql.getInstance().toSql(configuration, paramHolder, query);
    }

    @Override
    public String getQueryCountSql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query) {
        return MySqlEzQueryToSql.getInstance().toCountSql(configuration, paramHolder, query);
    }
}
