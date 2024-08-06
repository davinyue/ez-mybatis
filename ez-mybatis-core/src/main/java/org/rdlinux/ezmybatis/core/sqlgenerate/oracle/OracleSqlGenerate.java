package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzJdbcBatchSql;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerate;
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
    public String getInsertSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, Table table,
                               Object entity) {
        return OracleInsertSqlGenerate.getInstance().getInsertSql(configuration, mybatisParamHolder, table, entity);
    }

    @Override
    public String getBatchInsertSql(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                                    Table table, Collection<Object> models) {
        return OracleInsertSqlGenerate.getInstance().getBatchInsertSql(configuration, mybatisParamHolder, table,
                models);
    }

    @Override
    public EzJdbcBatchSql getJdbcBatchInsertSql(Configuration configuration, Table table, Collection<?> models) {
        return OracleInsertSqlGenerate.getInstance().getJdbcBatchInsertSql(configuration, table, models);
    }

    @Override
    public String getInsertByQuerySql(Configuration configuration, MybatisParamHolder mybatisParamHolder, Table table,
                                      EzQuery<?> query) {
        return OracleInsertSqlGenerate.getInstance().getInsertByQuerySql(configuration, mybatisParamHolder, table,
                query);
    }

    @Override
    public String getSelectByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Table table,
                                   Class<?> ntClass, Object id) {
        return OracleSelectSqlGenerate.getInstance().getSelectByIdSql(configuration, paramHolder, table, ntClass, id);
    }

    @Override
    public String getSelectByIdsSql(Configuration configuration, MybatisParamHolder paramHolder, Table table,
                                    Class<?> ntClass, Collection<?> ids) {
        return OracleSelectSqlGenerate.getInstance().getSelectByIdsSql(configuration, paramHolder, table, ntClass, ids);
    }

    @Override
    public String getQuerySql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query) {
        return OracleSelectSqlGenerate.getInstance().getQuerySql(configuration, paramHolder, query);
    }

    @Override
    public String getQueryCountSql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query) {
        return OracleSelectSqlGenerate.getInstance().getQueryCountSql(configuration, paramHolder, query);
    }

    @Override
    public String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, Table table,
                               Object entity, boolean isReplace) {
        return OracleUpdateSqlGenerate.getInstance().getUpdateSql(configuration, mybatisParamHolder, table, entity,
                isReplace);
    }

    @Override
    public String getBatchUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                                    Table table, Collection<Object> models, boolean isReplace) {
        return OracleUpdateSqlGenerate.getInstance().getBatchUpdateSql(configuration, mybatisParamHolder, table,
                models, isReplace);
    }

    @Override
    public String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, EzUpdate update) {
        return OracleUpdateSqlGenerate.getInstance().getUpdateSql(configuration, mybatisParamHolder, update);
    }

    @Override
    public String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                               Collection<EzUpdate> updates) {
        return OracleUpdateSqlGenerate.getInstance().getUpdateSql(configuration, mybatisParamHolder, updates);
    }

    @Override
    public EzJdbcBatchSql getJdbcBatchUpdateSql(Configuration configuration, Table table, Collection<?> models,
                                                Collection<String> updateFields, boolean isReplace) {
        return OracleUpdateSqlGenerate.getInstance().getJdbcBatchUpdateSql(configuration, table, models, updateFields,
                isReplace);
    }

    @Override
    public String getDeleteByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Table table,
                                   Class<?> ntClass, Object id) {
        return OracleDeleteSqlGenerate.getInstance().getDeleteByIdSql(configuration, paramHolder, table, ntClass, id);
    }

    @Override
    public String getBatchDeleteByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Table table,
                                        Class<?> ntClass, Collection<?> ids) {
        return OracleDeleteSqlGenerate.getInstance().getBatchDeleteByIdSql(configuration, paramHolder, table, ntClass,
                ids);
    }

    @Override
    public String getDeleteSql(Configuration configuration, MybatisParamHolder paramHolder, EzDelete delete) {
        return OracleDeleteSqlGenerate.getInstance().getDeleteSql(configuration, paramHolder, delete);
    }

    @Override
    public String getDeleteSql(Configuration configuration, MybatisParamHolder paramHolder,
                               Collection<EzDelete> deletes) {
        return OracleDeleteSqlGenerate.getInstance().getDeleteSql(configuration, paramHolder, deletes);
    }
}
