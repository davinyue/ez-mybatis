package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.utils.Assert;

public abstract class AbstractEzQueryToSql implements EzQueryToSql {
    @Override
    public String toSql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query) {
        Assert.notNull(query, "query can not be null");
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder = this.selectToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.fromToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.joinsToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.whereToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.groupByToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.orderByToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.havingToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.limitToSql(sqlBuilder, configuration, query, paramHolder);
        return sqlBuilder.toString();
    }

    @Override
    public String toCountSql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query) {
        Assert.notNull(query, "query can not be null");
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder = this.selectCountToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.fromToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.joinsToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.whereToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.groupByToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.havingToSql(sqlBuilder, configuration, query, paramHolder);
        return sqlBuilder.toString();
    }

    protected StringBuilder selectCountToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                             MybatisParamHolder paramHolder) {
        sqlBuilder.append("SELECT COUNT(*) ");
        return sqlBuilder;
    }

    protected StringBuilder selectToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                        MybatisParamHolder paramHolder) {
        Select select = query.getSelect();
        Assert.notNull(select, "select can not be null");
        return select.queryToSqlPart(sqlBuilder, configuration, query, paramHolder);
    }

    protected StringBuilder fromToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                      MybatisParamHolder paramHolder) {
        From from = query.getFrom();
        Converter<From> converter = EzMybatisContent.getConverter(configuration, From.class);
        return converter.toSqlPart(Converter.Type.SELECT, sqlBuilder, configuration, from, paramHolder);
    }

    protected abstract StringBuilder limitToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                                MybatisParamHolder paramHolder);

    protected StringBuilder orderByToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                         MybatisParamHolder paramHolder) {
        OrderBy order = query.getOrderBy();
        if (order == null || order.getItems() == null) {
            return sqlBuilder;
        } else {
            return order.toSqlPart(sqlBuilder, configuration, query, paramHolder);
        }
    }

    protected StringBuilder groupByToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                         MybatisParamHolder paramHolder) {
        GroupBy group = query.getGroupBy();
        if (group == null || group.getItems() == null) {
            return sqlBuilder;
        } else {
            return group.toSqlPart(sqlBuilder, configuration, query, paramHolder);
        }
    }

    protected StringBuilder whereToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                       MybatisParamHolder paramHolder) {
        Where where = query.getWhere();
        Converter<Where> converter = EzMybatisContent.getConverter(configuration, Where.class);
        return converter.toSqlPart(Converter.Type.SELECT, sqlBuilder, configuration, where, paramHolder);
    }

    protected StringBuilder havingToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                        MybatisParamHolder paramHolder) {
        Having having = query.getHaving();
        Converter<Having> converter = EzMybatisContent.getConverter(configuration, Having.class);
        return converter.toSqlPart(Converter.Type.SELECT, sqlBuilder, configuration, having, paramHolder);
    }

    protected StringBuilder joinsToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                       MybatisParamHolder paramHolder) {
        if (query.getJoins() != null) {
            Converter<Join> converter = EzMybatisContent.getConverter(configuration, Join.class);
            for (Join join : query.getJoins()) {
                sqlBuilder = converter.toSqlPart(Converter.Type.SELECT, sqlBuilder, configuration, join,
                        paramHolder);
            }
        }
        return sqlBuilder;
    }
}
