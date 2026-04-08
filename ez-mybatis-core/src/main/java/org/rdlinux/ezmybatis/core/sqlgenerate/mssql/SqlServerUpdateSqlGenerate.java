package org.rdlinux.ezmybatis.core.sqlgenerate.mssql;

import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractUpdateSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;

import java.util.Collection;

public class SqlServerUpdateSqlGenerate extends AbstractUpdateSqlGenerate {
    private static volatile SqlServerUpdateSqlGenerate instance;

    private SqlServerUpdateSqlGenerate() {
    }

    public static SqlServerUpdateSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (SqlServerUpdateSqlGenerate.class) {
                if (instance == null) {
                    instance = new SqlServerUpdateSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getUpdateSql(SqlGenerateContext sqlGenerateContext, EzUpdate update) {
        return SqlServerEzUpdateToSql.getInstance().toSql(sqlGenerateContext, update);
    }

    @Override
    public String getUpdateSql(SqlGenerateContext sqlGenerateContext, Collection<EzUpdate> updates) {
        return SqlServerEzUpdateToSql.getInstance().toSql(sqlGenerateContext, updates);
    }
}
