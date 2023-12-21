package org.rdlinux.ezmybatis.core.sqlgenerate.postgre;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerate;
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
    public String getQuerySql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query) {
        return PostgreSqlEzQueryToSql.getInstance().toSql(configuration, paramHolder, query);
    }

    @Override
    public String getDeleteByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Table table,
                                   Class<?> ntClass, Object id) {
        return PostgreSqlDeleteSqlGenerate.getInstance().getDeleteByIdSql(configuration, paramHolder, table, ntClass, id);
    }

    @Override
    public String getBatchDeleteByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Table table,
                                        Class<?> ntClass, Collection<?> ids) {
        return PostgreSqlDeleteSqlGenerate.getInstance().getBatchDeleteByIdSql(configuration, paramHolder, table, ntClass,
                ids);
    }

    @Override
    public String getDeleteSql(Configuration configuration, MybatisParamHolder paramHolder, EzDelete delete) {
        return PostgreSqlDeleteSqlGenerate.getInstance().getDeleteSql(configuration, paramHolder, delete);
    }

    @Override
    public String getDeleteSql(Configuration configuration, MybatisParamHolder paramHolder,
                               Collection<EzDelete> deletes) {
        return PostgreSqlDeleteSqlGenerate.getInstance().getDeleteSql(configuration, paramHolder, deletes);
    }
}
