package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractEzDeleteToSql;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Limit;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

import java.util.Collection;

public class OracleEzDeleteToSql extends AbstractEzDeleteToSql {
    private static volatile OracleEzDeleteToSql instance;

    protected OracleEzDeleteToSql() {
    }

    public static OracleEzDeleteToSql getInstance() {
        if (instance == null) {
            synchronized (OracleEzDeleteToSql.class) {
                if (instance == null) {
                    instance = new OracleEzDeleteToSql();
                }
            }
        }
        return instance;
    }


    @Override
    public String toSql(Configuration configuration, MybatisParamHolder paramHolder, Collection<EzDelete> deletes) {
        String sql = super.toSql(configuration, paramHolder, deletes);
        return "BEGIN \n" + sql + "END;";
    }

    @Override
    protected StringBuilder joinsToSql(StringBuilder sqlBuilder, Configuration configuration, EzDelete delete,
                                       MybatisParamHolder mybatisParamHolder) {
        return sqlBuilder;
    }

    @Override
    protected StringBuilder limitToSql(StringBuilder sqlBuilder, Configuration configuration, EzDelete delete,
                                       MybatisParamHolder mybatisParamHolder) {
        return sqlBuilder;
    }

    @Override
    protected StringBuilder whereToSql(StringBuilder sqlBuilder, Configuration configuration, EzDelete delete,
                                       MybatisParamHolder paramHolder) {
        sqlBuilder = super.whereToSql(sqlBuilder, configuration, delete, paramHolder);
        return this.handleWhereLimit(sqlBuilder, configuration, delete, paramHolder);
    }

    protected StringBuilder handleWhereLimit(StringBuilder sqlBuilder, Configuration configuration, EzDelete delete,
                                             MybatisParamHolder paramHolder) {
        Limit limit = delete.getLimit();
        if (limit == null) {
            return sqlBuilder;
        }
        if (delete.getWhere() == null) {
            sqlBuilder.append(" WHERE 1 = 1 ");
        }
        Converter<Limit> converter = EzMybatisContent.getConverter(configuration, Limit.class);
        converter.buildSql(Converter.Type.DELETE, sqlBuilder, configuration, limit, paramHolder);
        return sqlBuilder;
    }

}
