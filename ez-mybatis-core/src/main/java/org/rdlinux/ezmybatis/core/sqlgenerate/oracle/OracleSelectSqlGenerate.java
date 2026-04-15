package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractSelectSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;

public class OracleSelectSqlGenerate extends AbstractSelectSqlGenerate {
    private static volatile OracleSelectSqlGenerate instance;

    private OracleSelectSqlGenerate() {
    }

    public static OracleSelectSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (OracleSelectSqlGenerate.class) {
                if (instance == null) {
                    instance = new OracleSelectSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getQuerySql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        return OracleEzQueryToSql.getInstance().toSql(sqlGenerateContext, query);
    }

    @Override
    public String getQueryCountSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        return OracleEzQueryToSql.getInstance().toCountSql(sqlGenerateContext, query);
    }
}
