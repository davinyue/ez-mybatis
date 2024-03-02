package org.rdlinux.ezmybatis.core.sqlgenerate.dm;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.oracle.OracleEzUpdateToSql;
import org.rdlinux.ezmybatis.core.sqlstruct.Limit;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class DmEzUpdateToSql extends OracleEzUpdateToSql {
    private static volatile DmEzUpdateToSql instance;

    private DmEzUpdateToSql() {
    }

    public static DmEzUpdateToSql getInstance() {
        if (instance == null) {
            synchronized (DmEzUpdateToSql.class) {
                if (instance == null) {
                    instance = new DmEzUpdateToSql();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder handleWhereLimit(StringBuilder sqlBuilder, Configuration configuration, EzUpdate update,
                                             MybatisParamHolder mybatisParamHolder) {
        return sqlBuilder;
    }

    @Override
    protected StringBuilder limitToSql(StringBuilder sqlBuilder, Configuration configuration, EzUpdate update,
                                       MybatisParamHolder mybatisParamHolder) {
        Limit limit = update.getLimit();
        Converter<Limit> converter = EzMybatisContent.getConverter(configuration, Limit.class);
        return converter.buildSql(Converter.Type.UPDATE, sqlBuilder, configuration, limit, mybatisParamHolder);
    }
}
