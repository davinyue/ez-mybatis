package org.rdlinux.ezmybatis.core.sqlgenerate.mssql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractSelectSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.mysql.MySqlEzQueryToSql;
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
    public String getQuerySql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query) {
        return MySqlEzQueryToSql.getInstance().toSql(configuration, paramHolder, query);
    }

    @Override
    public String getQueryCountSql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query) {
        return MySqlEzQueryToSql.getInstance().toCountSql(configuration, paramHolder, query);
    }
}
