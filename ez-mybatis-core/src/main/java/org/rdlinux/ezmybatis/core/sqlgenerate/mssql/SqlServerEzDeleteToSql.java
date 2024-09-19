package org.rdlinux.ezmybatis.core.sqlgenerate.mssql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractEzDeleteToSql;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;

public class SqlServerEzDeleteToSql extends AbstractEzDeleteToSql {
    private static volatile SqlServerEzDeleteToSql instance;

    private SqlServerEzDeleteToSql() {
    }

    public static SqlServerEzDeleteToSql getInstance() {
        if (instance == null) {
            synchronized (SqlServerEzDeleteToSql.class) {
                if (instance == null) {
                    instance = new SqlServerEzDeleteToSql();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder joinsToSql(StringBuilder sqlBuilder, Configuration configuration, EzDelete delete,
                                       MybatisParamHolder mybatisParamHolder) {
        return sqlBuilder;
    }
}
