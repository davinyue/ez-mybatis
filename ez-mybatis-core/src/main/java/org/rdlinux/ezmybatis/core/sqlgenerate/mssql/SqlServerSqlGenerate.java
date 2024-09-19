package org.rdlinux.ezmybatis.core.sqlgenerate.mssql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzJdbcBatchSql;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerate;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.Collection;

public class SqlServerSqlGenerate implements SqlGenerate {
    private static volatile SqlServerSqlGenerate instance;

    protected SqlServerSqlGenerate() {
    }

    public static SqlServerSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (SqlServerSqlGenerate.class) {
                if (instance == null) {
                    instance = new SqlServerSqlGenerate();
                }
            }
        }
        return instance;
    }


    @Override
    public String getInsertSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, Table table,
                               Object entity) {
        return SqlServerInsertSqlGenerate.getInstance().getInsertSql(configuration, mybatisParamHolder, table, entity);
    }

    @Override
    public String getBatchInsertSql(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                                    Table table, Collection<Object> models) {
        return SqlServerInsertSqlGenerate.getInstance().getBatchInsertSql(configuration, mybatisParamHolder, table,
                models);
    }

    @Override
    public EzJdbcBatchSql getJdbcBatchInsertSql(Configuration configuration, Table table, Collection<?> models) {
        return SqlServerInsertSqlGenerate.getInstance().getJdbcBatchInsertSql(configuration, table, models);
    }

    @Override
    public String getInsertByQuerySql(Configuration configuration, MybatisParamHolder mybatisParamHolder, Table table,
                                      EzQuery<?> query) {
        return SqlServerInsertSqlGenerate.getInstance().getInsertByQuerySql(configuration, mybatisParamHolder, table, query);
    }

    @Override
    public String getSelectByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Table table,
                                   Class<?> ntClass, Object id) {
        return SqlServerSelectSqlGenerate.getInstance().getSelectByIdSql(configuration, paramHolder, table, ntClass, id);
    }

    @Override
    public String getSelectByIdsSql(Configuration configuration, MybatisParamHolder paramHolder, Table table,
                                    Class<?> ntClass, Collection<?> ids) {
        return SqlServerSelectSqlGenerate.getInstance().getSelectByIdsSql(configuration, paramHolder, table, ntClass, ids);
    }

    @Override
    public String getQuerySql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query) {
        return SqlServerSelectSqlGenerate.getInstance().getQuerySql(configuration, paramHolder, query);
    }

    @Override
    public String getQueryCountSql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query) {
        return SqlServerSelectSqlGenerate.getInstance().getQueryCountSql(configuration, paramHolder, query);
    }

    @Override
    public String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, Table table,
                               Object entity, boolean isReplace) {
        return SqlServerUpdateSqlGenerate.getInstance().getUpdateSql(configuration, mybatisParamHolder, table, entity,
                isReplace);
    }

    @Override
    public String getBatchUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                                    Table table, Collection<Object> models, boolean isReplace) {
        return SqlServerUpdateSqlGenerate.getInstance().getBatchUpdateSql(configuration, mybatisParamHolder, table, models,
                isReplace);
    }

    @Override
    public String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, EzUpdate update) {
        return SqlServerUpdateSqlGenerate.getInstance().getUpdateSql(configuration, mybatisParamHolder, update);
    }

    @Override
    public String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                               Collection<EzUpdate> updates) {
        return SqlServerUpdateSqlGenerate.getInstance().getUpdateSql(configuration, mybatisParamHolder, updates);
    }

    @Override
    public EzJdbcBatchSql getJdbcBatchUpdateSql(Configuration configuration, Table table, Collection<?> models,
                                                Collection<String> updateFields, boolean isReplace) {
        return SqlServerUpdateSqlGenerate.getInstance().getJdbcBatchUpdateSql(configuration, table, models, updateFields,
                isReplace);
    }

    @Override
    public String getDeleteByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Table table,
                                   Class<?> ntClass, Object id) {
        return SqlServerDeleteSqlGenerate.getInstance().getDeleteByIdSql(configuration, paramHolder, table, ntClass, id);
    }

    @Override
    public String getBatchDeleteByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Table table,
                                        Class<?> ntClass, Collection<?> ids) {
        return SqlServerDeleteSqlGenerate.getInstance().getBatchDeleteByIdSql(configuration, paramHolder, table, ntClass,
                ids);
    }

    @Override
    public String getDeleteSql(Configuration configuration, MybatisParamHolder paramHolder, EzDelete delete) {
        return SqlServerDeleteSqlGenerate.getInstance().getDeleteSql(configuration, paramHolder, delete);
    }

    @Override
    public String getDeleteSql(Configuration configuration, MybatisParamHolder paramHolder,
                               Collection<EzDelete> deletes) {
        return SqlServerDeleteSqlGenerate.getInstance().getDeleteSql(configuration, paramHolder, deletes);
    }
}
