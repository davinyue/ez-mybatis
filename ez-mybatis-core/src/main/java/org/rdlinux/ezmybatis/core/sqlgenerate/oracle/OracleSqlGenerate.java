package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzJdbcBatchSql;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.Collection;

public class OracleSqlGenerate implements SqlGenerate {
    private static volatile OracleSqlGenerate instance;

    protected OracleSqlGenerate() {
    }

    public static OracleSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (OracleSqlGenerate.class) {
                if (instance == null) {
                    instance = new OracleSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getInsertSql(SqlGenerateContext sqlGenerateContext, Table table,
                               Object entity) {
        return OracleInsertSqlGenerate.getInstance().getInsertSql(sqlGenerateContext, table, entity);
    }

    @Override
    public String getBatchInsertSql(SqlGenerateContext sqlGenerateContext,
                                    Table table, Collection<Object> models) {
        return OracleInsertSqlGenerate.getInstance().getBatchInsertSql(sqlGenerateContext, table, models);
    }

    @Override
    public EzJdbcBatchSql getJdbcBatchInsertSql(SqlGenerateContext sqlGenerateContext, Table table, Collection<?> models) {
        return OracleInsertSqlGenerate.getInstance().getJdbcBatchInsertSql(sqlGenerateContext, table, models);
    }

    @Override
    public String getInsertByQuerySql(SqlGenerateContext sqlGenerateContext, Table table,
                                      EzQuery<?> query) {
        return OracleInsertSqlGenerate.getInstance().getInsertByQuerySql(sqlGenerateContext, table,
                query);
    }

    @Override
    public String getSelectByIdSql(SqlGenerateContext sqlGenerateContext, Table table, Class<?> ntClass, Object id) {
        return OracleSelectSqlGenerate.getInstance().getSelectByIdSql(sqlGenerateContext, table, ntClass, id);
    }

    @Override
    public String getSelectByIdsSql(SqlGenerateContext sqlGenerateContext, Table table, Class<?> ntClass,
                                    Collection<?> ids) {
        return OracleSelectSqlGenerate.getInstance().getSelectByIdsSql(sqlGenerateContext, table, ntClass, ids);
    }

    @Override
    public String getQuerySql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        return OracleSelectSqlGenerate.getInstance().getQuerySql(sqlGenerateContext, query);
    }

    @Override
    public String getQueryCountSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        return OracleSelectSqlGenerate.getInstance().getQueryCountSql(sqlGenerateContext, query);
    }

    @Override
    public String getTableExistsSql(SqlGenerateContext sqlGenerateContext, DbTable table) {
        return OracleSelectSqlGenerate.getInstance().getTableExistsSql(sqlGenerateContext, table);
    }

    @Override
    public String getUpdateSql(SqlGenerateContext sqlGenerateContext, Table table,
                               Object entity, boolean isReplace) {
        return OracleUpdateSqlGenerate.getInstance().getUpdateSql(sqlGenerateContext, table, entity,
                isReplace);
    }

    @Override
    public String getBatchUpdateSql(SqlGenerateContext sqlGenerateContext,
                                    Table table, Collection<Object> models, boolean isReplace) {
        return OracleUpdateSqlGenerate.getInstance().getBatchUpdateSql(sqlGenerateContext, table, models, isReplace);
    }

    @Override
    public String getUpdateSql(SqlGenerateContext sqlGenerateContext, EzUpdate update) {
        return OracleUpdateSqlGenerate.getInstance().getUpdateSql(sqlGenerateContext, update);
    }

    @Override
    public String getUpdateSql(SqlGenerateContext sqlGenerateContext,
                               Collection<EzUpdate> updates) {
        return OracleUpdateSqlGenerate.getInstance().getUpdateSql(sqlGenerateContext, updates);
    }

    @Override
    public EzJdbcBatchSql getJdbcBatchUpdateSql(SqlGenerateContext sqlGenerateContext, Table table, Collection<?> models,
                                                Collection<String> updateFields, boolean isReplace) {
        return OracleUpdateSqlGenerate.getInstance().getJdbcBatchUpdateSql(sqlGenerateContext, table, models, updateFields,
                isReplace);
    }

    @Override
    public String getDeleteByIdSql(SqlGenerateContext sqlGenerateContext, Table table, Class<?> ntClass, Object id) {
        return OracleDeleteSqlGenerate.getInstance().getDeleteByIdSql(sqlGenerateContext, table, ntClass, id);
    }

    @Override
    public String getBatchDeleteByIdSql(SqlGenerateContext sqlGenerateContext, Table table, Class<?> ntClass,
                                        Collection<?> ids) {
        return OracleDeleteSqlGenerate.getInstance().getBatchDeleteByIdSql(sqlGenerateContext, table, ntClass,
                ids);
    }

    @Override
    public String getDeleteSql(SqlGenerateContext sqlGenerateContext, EzDelete delete) {
        return OracleDeleteSqlGenerate.getInstance().getDeleteSql(sqlGenerateContext, delete);
    }

    @Override
    public String getDeleteSql(SqlGenerateContext sqlGenerateContext, Collection<EzDelete> deletes) {
        return OracleDeleteSqlGenerate.getInstance().getDeleteSql(sqlGenerateContext, deletes);
    }
}
