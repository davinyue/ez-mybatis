package org.rdlinux.ezmybatis.core.sqlgenerate.mssql;

import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractDeleteSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;

import java.util.Collection;

public class SqlServerDeleteSqlGenerate extends AbstractDeleteSqlGenerate {
    private static volatile SqlServerDeleteSqlGenerate instance;

    protected SqlServerDeleteSqlGenerate() {
    }

    public static SqlServerDeleteSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (SqlServerDeleteSqlGenerate.class) {
                if (instance == null) {
                    instance = new SqlServerDeleteSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getDeleteSql(SqlGenerateContext sqlGenerateContext, EzDelete delete) {
        return SqlServerEzDeleteToSql.getInstance().toSql(sqlGenerateContext, delete);
    }

    @Override
    public String getDeleteSql(SqlGenerateContext sqlGenerateContext,
                               Collection<EzDelete> deletes) {
        return SqlServerEzDeleteToSql.getInstance().toSql(sqlGenerateContext, deletes);
    }
}
