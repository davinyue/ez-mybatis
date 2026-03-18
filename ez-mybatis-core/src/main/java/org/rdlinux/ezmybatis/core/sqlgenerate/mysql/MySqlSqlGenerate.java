package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzJdbcBatchSql;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.Collection;

public class MySqlSqlGenerate implements SqlGenerate {
    private static volatile MySqlSqlGenerate instance;

    protected MySqlSqlGenerate() {
    }

    public static MySqlSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (MySqlSqlGenerate.class) {
                if (instance == null) {
                    instance = new MySqlSqlGenerate();
                }
            }
        }
        return instance;
    }


    @Override
    public String getInsertSql(SqlGenerateContext sqlGenerateContext, Table table,
                               Object entity) {
        return MySqlInsertSqlGenerate.getInstance().getInsertSql(sqlGenerateContext, table, entity);
    }

    @Override
    public String getBatchInsertSql(SqlGenerateContext sqlGenerateContext,
                                    Table table, Collection<Object> models) {
        return MySqlInsertSqlGenerate.getInstance().getBatchInsertSql(sqlGenerateContext, table, models);
    }

    @Override
    public EzJdbcBatchSql getJdbcBatchInsertSql(SqlGenerateContext sqlGenerateContext, Table table, Collection<?> models) {
        return MySqlInsertSqlGenerate.getInstance().getJdbcBatchInsertSql(sqlGenerateContext, table, models);
    }

    @Override
    public String getInsertByQuerySql(SqlGenerateContext sqlGenerateContext, Table table,
                                      EzQuery<?> query) {
        return MySqlInsertSqlGenerate.getInstance().getInsertByQuerySql(sqlGenerateContext, table,
                query);
    }

    @Override
    public String getSelectByIdSql(SqlGenerateContext sqlGenerateContext, Table table, Class<?> ntClass, Object id) {
        return MySqlSelectSqlGenerate.getInstance().getSelectByIdSql(sqlGenerateContext, table, ntClass, id);
    }

    @Override
    public String getSelectByIdsSql(SqlGenerateContext sqlGenerateContext, Table table, Class<?> ntClass,
                                    Collection<?> ids) {
        return MySqlSelectSqlGenerate.getInstance().getSelectByIdsSql(sqlGenerateContext, table, ntClass, ids);
    }

    @Override
    public String getQuerySql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        return MySqlSelectSqlGenerate.getInstance().getQuerySql(sqlGenerateContext, query);
    }

    @Override
    public String getQueryCountSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        return MySqlSelectSqlGenerate.getInstance().getQueryCountSql(sqlGenerateContext, query);
    }

    @Override
    public String getUpdateSql(SqlGenerateContext sqlGenerateContext, Table table,
                               Object entity, boolean isReplace) {
        return MySqlUpdateSqlGenerate.getInstance().getUpdateSql(sqlGenerateContext, table, entity,
                isReplace);
    }

    @Override
    public String getBatchUpdateSql(SqlGenerateContext sqlGenerateContext,
                                    Table table, Collection<Object> models, boolean isReplace) {
        return MySqlUpdateSqlGenerate.getInstance().getBatchUpdateSql(sqlGenerateContext, table, models,
                isReplace);
    }

    @Override
    public String getUpdateSql(SqlGenerateContext sqlGenerateContext, EzUpdate update) {
        return MySqlUpdateSqlGenerate.getInstance().getUpdateSql(sqlGenerateContext, update);
    }

    @Override
    public String getUpdateSql(SqlGenerateContext sqlGenerateContext,
                               Collection<EzUpdate> updates) {
        return MySqlUpdateSqlGenerate.getInstance().getUpdateSql(sqlGenerateContext, updates);
    }

    @Override
    public EzJdbcBatchSql getJdbcBatchUpdateSql(SqlGenerateContext sqlGenerateContext, Table table, Collection<?> models,
                                                Collection<String> updateFields, boolean isReplace) {
        return MySqlUpdateSqlGenerate.getInstance().getJdbcBatchUpdateSql(sqlGenerateContext, table, models, updateFields,
                isReplace);
    }

    @Override
    public String getDeleteByIdSql(SqlGenerateContext sqlGenerateContext, Table table, Class<?> ntClass, Object id) {
        return MySqlDeleteSqlGenerate.getInstance().getDeleteByIdSql(sqlGenerateContext, table, ntClass, id);
    }

    @Override
    public String getBatchDeleteByIdSql(SqlGenerateContext sqlGenerateContext, Table table, Class<?> ntClass,
                                        Collection<?> ids) {
        return MySqlDeleteSqlGenerate.getInstance().getBatchDeleteByIdSql(sqlGenerateContext, table, ntClass,
                ids);
    }

    @Override
    public String getDeleteSql(SqlGenerateContext sqlGenerateContext, EzDelete delete) {
        return MySqlDeleteSqlGenerate.getInstance().getDeleteSql(sqlGenerateContext, delete);
    }

    @Override
    public String getDeleteSql(SqlGenerateContext sqlGenerateContext, Collection<EzDelete> deletes) {
        return MySqlDeleteSqlGenerate.getInstance().getDeleteSql(sqlGenerateContext, deletes);
    }
}
