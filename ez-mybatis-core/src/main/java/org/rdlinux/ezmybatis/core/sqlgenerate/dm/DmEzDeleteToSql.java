package org.rdlinux.ezmybatis.core.sqlgenerate.dm;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.oracle.OracleEzDeleteToSql;
import org.rdlinux.ezmybatis.core.sqlstruct.Limit;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class DmEzDeleteToSql extends OracleEzDeleteToSql {
    private static volatile DmEzDeleteToSql instance;

    private DmEzDeleteToSql() {
    }

    public static DmEzDeleteToSql getInstance() {
        if (instance == null) {
            synchronized (DmEzDeleteToSql.class) {
                if (instance == null) {
                    instance = new DmEzDeleteToSql();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder limitToSql(StringBuilder sqlBuilder, Configuration configuration, EzDelete delete,
                                       MybatisParamHolder mybatisParamHolder) {
        Limit limit = delete.getLimit();
        Converter<Limit> converter = EzMybatisContent.getConverter(configuration, Limit.class);
        return converter.buildSql(Converter.Type.DELETE, sqlBuilder, configuration, limit, mybatisParamHolder);
    }

    @Override
    protected StringBuilder handleWhereLimit(StringBuilder sqlBuilder, Configuration configuration, EzDelete delete,
                                             MybatisParamHolder paramHolder) {
        return sqlBuilder;
    }
}
