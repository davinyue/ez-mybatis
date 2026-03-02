package org.rdlinux.ezmybatis.core.sqlgenerate.postgre;

import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlgenerate.mysql.MySqlSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.Collection;

public class PostgreSqlGenerate extends MySqlSqlGenerate implements SqlGenerate {
    private static volatile PostgreSqlGenerate instance;

    protected PostgreSqlGenerate() {
    }

    public static PostgreSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (MySqlSqlGenerate.class) {
                if (instance == null) {
                    instance = new PostgreSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getQuerySql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        return PostgreSqlEzQueryToSql.getInstance().toSql(sqlGenerateContext, query);
    }

    @Override
    public String getDeleteByIdSql(SqlGenerateContext sqlGenerateContext, Table table, Class<?> ntClass, Object id) {
        return PostgreSqlDeleteSqlGenerate.getInstance().getDeleteByIdSql(sqlGenerateContext, table, ntClass, id);
    }

    @Override
    public String getBatchDeleteByIdSql(SqlGenerateContext sqlGenerateContext, Table table, Class<?> ntClass,
                                        Collection<?> ids) {
        return PostgreSqlDeleteSqlGenerate.getInstance().getBatchDeleteByIdSql(sqlGenerateContext, table, ntClass,
                ids);
    }

    @Override
    public String getDeleteSql(SqlGenerateContext sqlGenerateContext, EzDelete delete) {
        return PostgreSqlDeleteSqlGenerate.getInstance().getDeleteSql(sqlGenerateContext, delete);
    }

    @Override
    public String getDeleteSql(SqlGenerateContext sqlGenerateContext, Collection<EzDelete> deletes) {
        return PostgreSqlDeleteSqlGenerate.getInstance().getDeleteSql(sqlGenerateContext, deletes);
    }
}
