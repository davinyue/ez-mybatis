package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlstruct.From;
import org.rdlinux.ezmybatis.core.sqlstruct.Join;
import org.rdlinux.ezmybatis.core.sqlstruct.Where;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.Collection;

public abstract class AbstractEzDeleteToSql implements EzDeleteToSql {
    @Override
    public String toSql(Configuration configuration, MybatisParamHolder paramHolder, EzDelete delete) {
        Assert.notNull(delete, "delete can not be null");
        return this.toSql(configuration, delete, paramHolder);
    }

    @Override
    public String toSql(Configuration configuration, MybatisParamHolder paramHolder, Collection<EzDelete> deletes) {
        StringBuilder sql = new StringBuilder();
        for (EzDelete delete : deletes) {
            sql.append(this.toSql(configuration, delete, paramHolder)).append(";\n");
        }
        return sql.toString();
    }

    protected String toSql(Configuration configuration, EzDelete delete, MybatisParamHolder mybatisParamHolder) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder = this.deleteToSql(sqlBuilder, delete);
        sqlBuilder = this.fromToSql(sqlBuilder, configuration, delete, mybatisParamHolder);
        sqlBuilder = this.joinsToSql(sqlBuilder, configuration, delete, mybatisParamHolder);
        sqlBuilder = this.whereToSql(sqlBuilder, configuration, delete, mybatisParamHolder);
        return sqlBuilder.toString();
    }

    protected StringBuilder deleteToSql(StringBuilder sqlBuilder, EzDelete delete) {
        sqlBuilder.append("DELETE ");
        return sqlBuilder;
    }

    protected StringBuilder fromToSql(StringBuilder sqlBuilder, Configuration configuration, EzDelete delete,
                                      MybatisParamHolder mybatisParamHolder) {
        From from = delete.getFrom();
        Converter<From> converter = EzMybatisContent.getConverter(configuration, From.class);
        return converter.toSqlPart(Converter.Type.DELETE, sqlBuilder, configuration, from, mybatisParamHolder);
    }

    protected StringBuilder joinsToSql(StringBuilder sqlBuilder, Configuration configuration, EzDelete delete,
                                       MybatisParamHolder mybatisParamHolder) {
        if (delete.getJoins() != null) {
            Converter<Join> converter = EzMybatisContent.getConverter(configuration, Join.class);
            for (Join join : delete.getJoins()) {
                sqlBuilder = converter.toSqlPart(Converter.Type.DELETE, sqlBuilder, configuration, join,
                        mybatisParamHolder);
            }
        }
        return sqlBuilder;
    }

    protected StringBuilder whereToSql(StringBuilder sqlBuilder, Configuration configuration, EzDelete delete,
                                       MybatisParamHolder paramHolder) {
        Where where = delete.getWhere();
        Converter<Where> converter = EzMybatisContent.getConverter(configuration, Where.class);
        return converter.toSqlPart(Converter.Type.DELETE, sqlBuilder, configuration, where, paramHolder);
    }
}
