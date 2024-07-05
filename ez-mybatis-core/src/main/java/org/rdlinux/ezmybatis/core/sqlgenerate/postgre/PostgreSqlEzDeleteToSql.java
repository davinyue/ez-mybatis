package org.rdlinux.ezmybatis.core.sqlgenerate.postgre;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractEzDeleteToSql;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Limit;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class PostgreSqlEzDeleteToSql extends AbstractEzDeleteToSql {
    private static volatile PostgreSqlEzDeleteToSql instance;

    private PostgreSqlEzDeleteToSql() {
    }

    public static PostgreSqlEzDeleteToSql getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlEzDeleteToSql.class) {
                if (instance == null) {
                    instance = new PostgreSqlEzDeleteToSql();
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

    @Override
    protected StringBuilder limitToSql(StringBuilder sqlBuilder, Configuration configuration, EzDelete delete,
                                       MybatisParamHolder mybatisParamHolder) {
        Limit limit = delete.getLimit();
        Converter<Limit> converter = EzMybatisContent.getConverter(configuration, Limit.class);
        return converter.buildSql(Converter.Type.DELETE, sqlBuilder, configuration, limit, mybatisParamHolder);
    }
}
