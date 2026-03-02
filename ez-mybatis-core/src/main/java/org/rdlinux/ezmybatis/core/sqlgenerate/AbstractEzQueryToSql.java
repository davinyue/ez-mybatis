package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.utils.AliasGenerate;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.List;

/**
 * EzQuery转SQL抽象实现类
 * 提供将EzQuery对象转换为SQL语句的基础实现
 */
public abstract class AbstractEzQueryToSql implements EzQueryToSql {
    @Override
    public String toSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        Assert.notNull(query, "query can not be null");
        this.selectToSql(sqlGenerateContext, query);
        this.fromToSql(sqlGenerateContext, query);
        this.joinsToSql(sqlGenerateContext, query);
        this.whereToSql(true, sqlGenerateContext, query);
        this.onWhereToSqlEnd(true, sqlGenerateContext, query);
        this.groupByToSql(sqlGenerateContext, query);
        this.havingToSql(sqlGenerateContext, query);
        this.orderByToSql(sqlGenerateContext, query);
        this.pageToSql(sqlGenerateContext, query);
        if (query.getPage() == null) {
            this.limitToSql(sqlGenerateContext, query);
        }
        if (query.getUnions() != null && !query.getUnions().isEmpty()) {
            if (query.getOrderBy() != null && query.getOrderBy().getItems() != null &&
                    !query.getOrderBy().getItems().isEmpty()) {
                sqlGenerateContext.getSqlBuilder().insert(0, "(SELECT * FROM (")
                        .append(") ").append(AliasGenerate.getAlias()).append(" ) ");
            } else {
                sqlGenerateContext.getSqlBuilder().insert(0, " (").append(") ");
            }
        }
        this.unionToSql(sqlGenerateContext, query);
        return sqlGenerateContext.getSqlBuilder().toString();
    }


    @Override
    public String toCountSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        Assert.notNull(query, "query can not be null");
        this.selectCountToSql(sqlGenerateContext, query);
        this.fromToSql(sqlGenerateContext, query);
        this.joinsToSql(sqlGenerateContext, query);
        this.whereToSql(false, sqlGenerateContext, query);
        this.onWhereToSqlEnd(false, sqlGenerateContext, query);
        this.groupByToSql(sqlGenerateContext, query);
        this.havingToSql(sqlGenerateContext, query);
        if (query.getGroupBy() != null && !query.getGroupBy().getItems().isEmpty()) {
            sqlGenerateContext.getSqlBuilder().insert(0, "SELECT COUNT(*) FROM ( ")
                    .append(" ) ").append(AliasGenerate.getAlias());
        }
        return sqlGenerateContext.getSqlBuilder().toString();
    }

    /**
     * WHERE子句结束处理
     *
     * @param isPage             是否分页
     * @param sqlGenerateContext SQL生成上下文
     * @param query              查询对象
     */
    protected void onWhereToSqlEnd(boolean isPage, SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
    }

    /**
     * 构建计数查询的SELECT子句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param query              查询对象
     */
    protected void selectCountToSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        sqlGenerateContext.getSqlBuilder().append("SELECT ");
        if (query.getSelect() != null) {
            SqlHint sqlHint = query.getSelect().getSqlHint();
            if (sqlHint != null) {
                Converter<SqlHint> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(),
                        SqlHint.class);
                converter.buildSql(Converter.Type.SELECT, sqlHint, sqlGenerateContext);
            }
        }
        sqlGenerateContext.getSqlBuilder().append(" COUNT(*) ");
    }

    /**
     * 构建查询的SELECT子句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param query              查询对象
     */
    protected void selectToSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        Select select = query.getSelect();
        Assert.notNull(select, "select can not be null");
        Converter<Select> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(),
                Select.class);
        converter.buildSql(Converter.Type.SELECT, select, sqlGenerateContext);
    }

    /**
     * 构建查询的FROM子句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param query              查询对象
     */
    protected void fromToSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        From from = query.getFrom();
        Converter<From> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(), From.class);
        converter.buildSql(Converter.Type.SELECT, from, sqlGenerateContext);
    }

    /**
     * 构建查询的UNION子句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param query              查询对象
     */
    protected void unionToSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        List<Union> unions = query.getUnions();
        if (unions == null || unions.isEmpty()) {
            return;
        }
        Converter<Union> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(), Union.class);
        for (Union union : unions) {
            converter.buildSql(Converter.Type.SELECT, union, sqlGenerateContext);
        }
    }

    /**
     * 构建查询的分页子句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param query              查询对象
     */
    protected void pageToSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        Page page = query.getPage();
        Converter<Page> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(), Page.class);
        converter.buildSql(Converter.Type.SELECT, page, sqlGenerateContext);
    }

    /**
     * 构建查询的LIMIT子句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param query              查询对象
     */
    protected void limitToSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        Limit limit = query.getLimit();
        Converter<Limit> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(), Limit.class);
        converter.buildSql(Converter.Type.SELECT, limit, sqlGenerateContext);
    }

    /**
     * 构建查询的ORDER BY子句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param query              查询对象
     */
    protected void orderByToSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        OrderBy order = query.getOrderBy();
        Converter<OrderBy> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(),
                OrderBy.class);
        converter.buildSql(Converter.Type.SELECT, order, sqlGenerateContext);
    }

    /**
     * 构建查询的GROUP BY子句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param query              查询对象
     */
    protected void groupByToSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        GroupBy group = query.getGroupBy();
        Converter<GroupBy> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(),
                GroupBy.class);
        converter.buildSql(Converter.Type.SELECT, group, sqlGenerateContext);
    }

    /**
     * 构建查询的WHERE子句
     *
     * @param isPage             是否分页
     * @param sqlGenerateContext SQL生成上下文
     * @param query              查询对象
     */
    protected void whereToSql(boolean isPage, SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        Where where = query.getWhere();
        Converter<Where> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(), Where.class);
        converter.buildSql(Converter.Type.SELECT, where, sqlGenerateContext);
    }

    /**
     * 构建查询的HAVING子句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param query              查询对象
     */
    protected void havingToSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        Having having = query.getHaving();
        Converter<Having> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(),
                Having.class);
        converter.buildSql(Converter.Type.SELECT, having, sqlGenerateContext);
    }

    /**
     * 构建查询的JOIN子句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param query              查询对象
     */
    protected void joinsToSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        if (query.getJoins() != null) {
            Converter<Join> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(),
                    Join.class);
            for (Join join : query.getJoins()) {
                converter.buildSql(Converter.Type.SELECT, join, sqlGenerateContext);
            }
        }
    }
}
