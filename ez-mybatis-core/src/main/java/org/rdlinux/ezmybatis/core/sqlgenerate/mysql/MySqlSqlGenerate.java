package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerate;

import java.util.Collection;

public class MySqlSqlGenerate implements SqlGenerate {
    private static volatile MySqlSqlGenerate instance;

    private MySqlSqlGenerate() {
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
    public String getInsertSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, Object entity) {
        return MySqlInsertSqlGenerate.getInstance().getInsertSql(configuration, mybatisParamHolder, entity);
    }

    @Override
    public String getBatchInsertSql(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                                    Collection<Object> entitys) {
        return MySqlInsertSqlGenerate.getInstance().getBatchInsertSql(configuration, mybatisParamHolder, entitys);
    }

    @Override
    public String getSelectByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Class<?> ntClass,
                                   Object id) {
        return MySqlSelectSqlGenerate.getInstance().getSelectByIdSql(configuration, paramHolder, ntClass, id);
    }

    @Override
    public String getSelectByIdsSql(Configuration configuration, MybatisParamHolder paramHolder, Class<?> ntClass,
                                    Collection<?> ids) {
        return MySqlSelectSqlGenerate.getInstance().getSelectByIdsSql(configuration, paramHolder, ntClass, ids);
    }

    @Override
    public String getQuerySql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query) {
        return MySqlSelectSqlGenerate.getInstance().getQuerySql(configuration, paramHolder, query);
    }

    @Override
    public String getQueryCountSql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query) {
        return MySqlSelectSqlGenerate.getInstance().getQueryCountSql(configuration, paramHolder, query);
    }

    @Override
    public String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, Object entity,
                               boolean isReplace) {
        return MySqlUpdateSqlGenerate.getInstance().getUpdateSql(configuration, mybatisParamHolder, entity, isReplace);
    }

    @Override
    public String getBatchUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                                    Collection<Object> entitys, boolean isReplace) {
        return MySqlUpdateSqlGenerate.getInstance().getBatchUpdateSql(configuration, mybatisParamHolder, entitys,
                isReplace);
    }

    @Override
    public String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, EzUpdate update) {
        return MySqlUpdateSqlGenerate.getInstance().getUpdateSql(configuration, mybatisParamHolder, update);
    }

    @Override
    public String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                               Collection<EzUpdate> updates) {
        return MySqlUpdateSqlGenerate.getInstance().getUpdateSql(configuration, mybatisParamHolder, updates);
    }

    @Override
    public String getDeleteByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Class<?> ntClass,
                                   Object id) {
        return MySqlDeleteSqlGenerate.getInstance().getDeleteByIdSql(configuration, paramHolder, ntClass, id);
    }

    @Override
    public String getBatchDeleteByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Class<?> ntClass,
                                        Collection<?> ids) {
        return MySqlDeleteSqlGenerate.getInstance().getBatchDeleteByIdSql(configuration, paramHolder, ntClass, ids);
    }

    @Override
    public String getDeleteSql(Configuration configuration, MybatisParamHolder paramHolder, EzDelete delete) {
        return MySqlDeleteSqlGenerate.getInstance().getDeleteSql(configuration, paramHolder, delete);
    }

    @Override
    public String getDeleteSql(Configuration configuration, MybatisParamHolder paramHolder,
                               Collection<EzDelete> deletes) {
        return MySqlDeleteSqlGenerate.getInstance().getDeleteSql(configuration, paramHolder, deletes);
    }
}
