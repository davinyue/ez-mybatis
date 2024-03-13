package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.utils.AliasGenerate;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.List;

public abstract class AbstractEzQueryToSql implements EzQueryToSql {
    @Override
    public String toSql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query) {
        Assert.notNull(query, "query can not be null");
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder = this.selectToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.fromToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.joinsToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.whereToSql(true, sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.groupByToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.orderByToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.havingToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.limitToSql(sqlBuilder, configuration, query, paramHolder);
        if (query.getUnions() != null && !query.getUnions().isEmpty()) {
            if (query.getOrderBy() != null) {
                sqlBuilder = new StringBuilder().append(" (SELECT * FROM (").append(sqlBuilder).append(") ")
                        .append(AliasGenerate.getAlias()).append(") ");
            } else {
                sqlBuilder = new StringBuilder().append(" (").append(sqlBuilder).append(") ");
            }
        }
        sqlBuilder = this.unionToSql(sqlBuilder, configuration, query, paramHolder);
        return sqlBuilder.toString();
    }

    @Override
    public String toCountSql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query) {
        Assert.notNull(query, "query can not be null");
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder = this.selectCountToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.fromToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.joinsToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.whereToSql(false, sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.groupByToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.havingToSql(sqlBuilder, configuration, query, paramHolder);
        if (query.getGroupBy() != null && !query.getGroupBy().getItems().isEmpty()) {
            return "SELECT COUNT(*) FROM ( " + sqlBuilder.toString() + " ) " + AliasGenerate.getAlias();
        } else {
            return sqlBuilder.toString();
        }
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
        Converter<Select> converter = EzMybatisContent.getConverter(configuration, Select.class);
        return converter.buildSql(Converter.Type.SELECT, sqlBuilder, configuration, select, paramHolder);
    }

    protected StringBuilder fromToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                      MybatisParamHolder paramHolder) {
        From from = query.getFrom();
        Converter<From> converter = EzMybatisContent.getConverter(configuration, From.class);
        return converter.buildSql(Converter.Type.SELECT, sqlBuilder, configuration, from, paramHolder);
    }

    protected StringBuilder unionToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                       MybatisParamHolder paramHolder) {
        List<Union> unions = query.getUnions();
        if (unions == null || unions.isEmpty()) {
            return sqlBuilder;
        }
        Converter<Union> converter = EzMybatisContent.getConverter(configuration, Union.class);
        for (Union union : unions) {
            sqlBuilder = converter.buildSql(Converter.Type.SELECT, sqlBuilder, configuration, union, paramHolder);
        }
        return sqlBuilder;
    }

    protected StringBuilder limitToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                       MybatisParamHolder paramHolder) {
        Page limit = query.getPage();
        Converter<Page> converter = EzMybatisContent.getConverter(configuration, Page.class);
        return converter.buildSql(Converter.Type.SELECT, sqlBuilder, configuration, limit, paramHolder);
    }

    protected StringBuilder orderByToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                         MybatisParamHolder paramHolder) {
        OrderBy order = query.getOrderBy();
        Converter<OrderBy> converter = EzMybatisContent.getConverter(configuration, OrderBy.class);
        return converter.buildSql(Converter.Type.SELECT, sqlBuilder, configuration, order, paramHolder);
    }

    protected StringBuilder groupByToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                         MybatisParamHolder paramHolder) {
        GroupBy group = query.getGroupBy();
        Converter<GroupBy> converter = EzMybatisContent.getConverter(configuration, GroupBy.class);
        return converter.buildSql(Converter.Type.SELECT, sqlBuilder, configuration, group, paramHolder);
    }

    /**
     * @param isPage 是否分页
     */
    protected StringBuilder whereToSql(boolean isPage, StringBuilder sqlBuilder, Configuration configuration,
                                       EzQuery<?> query, MybatisParamHolder paramHolder) {
        Where where = query.getWhere();
        Converter<Where> converter = EzMybatisContent.getConverter(configuration, Where.class);
        return converter.buildSql(Converter.Type.SELECT, sqlBuilder, configuration, where, paramHolder);
    }

    protected StringBuilder havingToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                        MybatisParamHolder paramHolder) {
        Having having = query.getHaving();
        Converter<Having> converter = EzMybatisContent.getConverter(configuration, Having.class);
        return converter.buildSql(Converter.Type.SELECT, sqlBuilder, configuration, having, paramHolder);
    }

    protected StringBuilder joinsToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                       MybatisParamHolder paramHolder) {
        if (query.getJoins() != null) {
            Converter<Join> converter = EzMybatisContent.getConverter(configuration, Join.class);
            for (Join join : query.getJoins()) {
                sqlBuilder = converter.buildSql(Converter.Type.SELECT, sqlBuilder, configuration, join,
                        paramHolder);
            }
        }
        return sqlBuilder;
    }
}
