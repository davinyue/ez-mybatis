package org.rdlinux.ezmybatis.core.sqlgenerate.postgre;

import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlgenerate.mysql.MySqlDeleteSqlGenerate;

import java.util.Collection;

public class PostgreSqlDeleteSqlGenerate extends MySqlDeleteSqlGenerate {
    private static volatile PostgreSqlDeleteSqlGenerate instance;

    private PostgreSqlDeleteSqlGenerate() {
    }

    public static PostgreSqlDeleteSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlDeleteSqlGenerate.class) {
                if (instance == null) {
                    instance = new PostgreSqlDeleteSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getDeleteSql(SqlGenerateContext sqlGenerateContext, EzDelete delete) {
        return PostgreSqlEzDeleteToSql.getInstance().toSql(sqlGenerateContext, delete);
    }

    @Override
    public String getDeleteSql(SqlGenerateContext sqlGenerateContext,
                               Collection<EzDelete> deletes) {
        return PostgreSqlEzDeleteToSql.getInstance().toSql(sqlGenerateContext, deletes);
    }
}
