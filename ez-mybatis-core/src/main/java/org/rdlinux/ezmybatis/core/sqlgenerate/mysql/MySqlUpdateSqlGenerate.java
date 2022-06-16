package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractUpdateSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;

import java.util.Collection;

public class MySqlUpdateSqlGenerate extends AbstractUpdateSqlGenerate {
    private static volatile MySqlUpdateSqlGenerate instance;

    private MySqlUpdateSqlGenerate() {
    }

    public static MySqlUpdateSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (MySqlUpdateSqlGenerate.class) {
                if (instance == null) {
                    instance = new MySqlUpdateSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, EzUpdate update) {
        return MySqlEzUpdateToSql.getInstance().toSql(configuration, mybatisParamHolder, update);
    }

    @Override
    public String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                               Collection<EzUpdate> updates) {
        return MySqlEzUpdateToSql.getInstance().toSql(configuration, mybatisParamHolder, updates);
    }
}
