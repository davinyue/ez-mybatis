package org.rdlinux.ezmybatis.core.sqlgenerate.dm;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.oracle.OracleSqlGenerate;

public class DmSqlGenerate extends OracleSqlGenerate {
    private static volatile DmSqlGenerate instance;

    private DmSqlGenerate() {
    }

    public static DmSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (DmSqlGenerate.class) {
                if (instance == null) {
                    instance = new DmSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getQuerySql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query) {
        return DmEzQueryToSql.getInstance().toSql(configuration, paramHolder, query);
    }

    @Override
    public String getQueryCountSql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query) {
        return DmEzQueryToSql.getInstance().toCountSql(configuration, paramHolder, query);
    }
}
