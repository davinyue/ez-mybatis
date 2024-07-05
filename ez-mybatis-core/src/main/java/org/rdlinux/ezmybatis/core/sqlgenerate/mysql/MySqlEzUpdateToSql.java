package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractEzUpdateToSql;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Limit;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlEzUpdateToSql extends AbstractEzUpdateToSql {
    private static volatile MySqlEzUpdateToSql instance;

    private MySqlEzUpdateToSql() {
    }

    public static MySqlEzUpdateToSql getInstance() {
        if (instance == null) {
            synchronized (MySqlEzUpdateToSql.class) {
                if (instance == null) {
                    instance = new MySqlEzUpdateToSql();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder limitToSql(StringBuilder sqlBuilder, Configuration configuration, EzUpdate update,
                                       MybatisParamHolder mybatisParamHolder) {
        Limit limit = update.getLimit();
        Converter<Limit> converter = EzMybatisContent.getConverter(configuration, Limit.class);
        return converter.buildSql(Converter.Type.UPDATE, sqlBuilder, configuration, limit, mybatisParamHolder);
    }
}
