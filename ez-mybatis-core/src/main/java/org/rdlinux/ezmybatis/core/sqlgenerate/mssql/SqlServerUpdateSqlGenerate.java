package org.rdlinux.ezmybatis.core.sqlgenerate.mssql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractUpdateSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;

import java.util.Collection;

public class SqlServerUpdateSqlGenerate extends AbstractUpdateSqlGenerate {
    private static volatile SqlServerUpdateSqlGenerate instance;

    private SqlServerUpdateSqlGenerate() {
    }

    public static SqlServerUpdateSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (SqlServerUpdateSqlGenerate.class) {
                if (instance == null) {
                    instance = new SqlServerUpdateSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, EzUpdate update) {
        return SqlServerEzUpdateToSql.getInstance().toSql(configuration, mybatisParamHolder, update);
    }

    @Override
    public String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                               Collection<EzUpdate> updates) {
        return SqlServerEzUpdateToSql.getInstance().toSql(configuration, mybatisParamHolder, updates);
    }
}
