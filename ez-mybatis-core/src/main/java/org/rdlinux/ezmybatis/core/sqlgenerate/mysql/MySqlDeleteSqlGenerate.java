package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractDeleteSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;

import java.util.Collection;

public class MySqlDeleteSqlGenerate extends AbstractDeleteSqlGenerate {
    private static volatile MySqlDeleteSqlGenerate instance;

    protected MySqlDeleteSqlGenerate() {
    }

    public static MySqlDeleteSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (MySqlDeleteSqlGenerate.class) {
                if (instance == null) {
                    instance = new MySqlDeleteSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getDeleteSql(Configuration configuration, MybatisParamHolder paramHolder, EzDelete delete) {
        return MySqlEzDeleteToSql.getInstance().toSql(configuration, paramHolder, delete);
    }

    @Override
    public String getDeleteSql(Configuration configuration, MybatisParamHolder paramHolder,
                               Collection<EzDelete> deletes) {
        return MySqlEzDeleteToSql.getInstance().toSql(configuration, paramHolder, deletes);
    }
}
