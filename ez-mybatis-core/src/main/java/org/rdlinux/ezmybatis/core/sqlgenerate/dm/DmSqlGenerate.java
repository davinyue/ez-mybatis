package org.rdlinux.ezmybatis.core.sqlgenerate.dm;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.oracle.OracleSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.Collection;

public class DmSqlGenerate extends OracleSqlGenerate {
    private static volatile DmSqlGenerate instance;

    private DmSqlGenerate() {
    }

    public static DmSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (DmSqlGenerate.class) {
                if (instance == null) {
                    instance = new DmSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getQuerySql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query) {
        return DmEzQueryToSql.getInstance().toSql(configuration, paramHolder, query);
    }

    @Override
    public String getQueryCountSql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query) {
        return DmEzQueryToSql.getInstance().toCountSql(configuration, paramHolder, query);
    }

    @Override
    public String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, Table table,
                               Object entity, boolean isReplace) {
        return DmUpdateSqlGenerate.getInstance().getUpdateSql(configuration, mybatisParamHolder, table, entity,
                isReplace);
    }

    @Override
    public String getBatchUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                                    Table table, Collection<Object> models, boolean isReplace) {
        return DmUpdateSqlGenerate.getInstance().getBatchUpdateSql(configuration, mybatisParamHolder, table,
                models, isReplace);
    }

    @Override
    public String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, EzUpdate update) {
        return DmUpdateSqlGenerate.getInstance().getUpdateSql(configuration, mybatisParamHolder, update);
    }

    @Override
    public String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                               Collection<EzUpdate> updates) {
        return DmUpdateSqlGenerate.getInstance().getUpdateSql(configuration, mybatisParamHolder, updates);
    }

    @Override
    public String getDeleteByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Table table,
                                   Class<?> ntClass, Object id) {
        return DmDeleteSqlGenerate.getInstance().getDeleteByIdSql(configuration, paramHolder, table, ntClass, id);
    }

    @Override
    public String getBatchDeleteByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Table table,
                                        Class<?> ntClass, Collection<?> ids) {
        return DmDeleteSqlGenerate.getInstance().getBatchDeleteByIdSql(configuration, paramHolder, table, ntClass,
                ids);
    }

    @Override
    public String getDeleteSql(Configuration configuration, MybatisParamHolder paramHolder, EzDelete delete) {
        return DmDeleteSqlGenerate.getInstance().getDeleteSql(configuration, paramHolder, delete);
    }

    @Override
    public String getDeleteSql(Configuration configuration, MybatisParamHolder paramHolder,
                               Collection<EzDelete> deletes) {
        return DmDeleteSqlGenerate.getInstance().getDeleteSql(configuration, paramHolder, deletes);
    }
}
