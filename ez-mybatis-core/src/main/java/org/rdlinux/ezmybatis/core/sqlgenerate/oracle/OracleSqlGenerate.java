package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzDelete;
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
                                    Table table, Collection<Object> entitys) {
        return OracleInsertSqlGenerate.getInstance().getBatchInsertSql(configuration, mybatisParamHolder, table,
                entitys);
    }

    @Override
    public String getSelectByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Class<?> ntClass,
                                   Object id) {
        return OracleSelectSqlGenerate.getInstance().getSelectByIdSql(configuration, paramHolder, ntClass, id);
    }

    @Override
    public String getSelectByIdsSql(Configuration configuration, MybatisParamHolder paramHolder, Class<?> ntClass,
                                    Collection<?> ids) {
        return OracleSelectSqlGenerate.getInstance().getSelectByIdsSql(configuration, paramHolder, ntClass, ids);
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
    public String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, Object entity,
                               boolean isReplace) {
        return OracleUpdateSqlGenerate.getInstance().getUpdateSql(configuration, mybatisParamHolder, entity, isReplace);
    }

    @Override
    public String getBatchUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                                    Collection<Object> entitys, boolean isReplace) {
        return OracleUpdateSqlGenerate.getInstance().getBatchUpdateSql(configuration, mybatisParamHolder, entitys,
                isReplace);
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
    public String getDeleteByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Class<?> ntClass,
                                   Object id) {
        return OracleDeleteSqlGenerate.getInstance().getDeleteByIdSql(configuration, paramHolder, ntClass, id);
    }

    @Override
    public String getBatchDeleteByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Class<?> ntClass,
                                        Collection<?> ids) {
        return OracleDeleteSqlGenerate.getInstance().getBatchDeleteByIdSql(configuration, paramHolder, ntClass, ids);
    }

    @Override
    public String getDeleteSql(Configuration configuration, MybatisParamHolder paramHolder, EzDelete delete) {
        return OracleDeleteSqlGenerate.getInstance().getDeleteSql(configuration, paramHolder, delete);
    }

    @Override
    public String getDeleteSql(Configuration configuration, MybatisParamHolder paramHolder, Collection<EzDelete> deletes) {
        return OracleDeleteSqlGenerate.getInstance().getDeleteSql(configuration, paramHolder, deletes);
    }
}
