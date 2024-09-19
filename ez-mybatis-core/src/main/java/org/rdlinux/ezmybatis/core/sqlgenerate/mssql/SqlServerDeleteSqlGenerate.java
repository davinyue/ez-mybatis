package org.rdlinux.ezmybatis.core.sqlgenerate.mssql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractDeleteSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;

import java.util.Collection;

public class SqlServerDeleteSqlGenerate extends AbstractDeleteSqlGenerate {
    private static volatile SqlServerDeleteSqlGenerate instance;

    protected SqlServerDeleteSqlGenerate() {
    }

    public static SqlServerDeleteSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (SqlServerDeleteSqlGenerate.class) {
                if (instance == null) {
                    instance = new SqlServerDeleteSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getDeleteSql(Configuration configuration, MybatisParamHolder paramHolder, EzDelete delete) {
        return SqlServerEzDeleteToSql.getInstance().toSql(configuration, paramHolder, delete);
    }

    @Override
    public String getDeleteSql(Configuration configuration, MybatisParamHolder paramHolder,
                               Collection<EzDelete> deletes) {
        return SqlServerEzDeleteToSql.getInstance().toSql(configuration, paramHolder, deletes);
    }
}
