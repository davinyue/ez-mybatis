package ink.dvc.ezmybatis.core.sqlgenerate;

import ink.dvc.ezmybatis.core.EzQuery;
import ink.dvc.ezmybatis.core.sqlstruct.*;
import ink.dvc.ezmybatis.core.utils.Assert;
import org.apache.ibatis.session.Configuration;

import java.util.Map;

public abstract class AbstractEzQueryToSql implements EzQueryToSql {
    @Override
    public String toSql(Configuration configuration, EzQuery<?> query, Map<String, Object> mybatisParam) {
        StringBuilder sqlBuilder = new StringBuilder();
        MybatisParamHolder mybatisParamHolder = new MybatisParamHolder(mybatisParam);
        sqlBuilder = this.selectToSql(sqlBuilder, configuration, query, mybatisParamHolder);
        sqlBuilder = this.fromToSql(sqlBuilder, configuration, query, mybatisParamHolder);
        sqlBuilder = this.joinsToSql(sqlBuilder, configuration, query, mybatisParamHolder);
        sqlBuilder = this.whereToSql(sqlBuilder, configuration, query, mybatisParamHolder);
        sqlBuilder = this.groupByToSql(sqlBuilder, configuration, query, mybatisParamHolder);
        sqlBuilder = this.orderByToSql(sqlBuilder, configuration, query, mybatisParamHolder);
        sqlBuilder = this.havingToSql(sqlBuilder, configuration, query, mybatisParamHolder);
        sqlBuilder = this.limitToSql(sqlBuilder, configuration, query, mybatisParamHolder);
        return sqlBuilder.toString();
    }

    @Override
    public String toCountSql(Configuration configuration, EzQuery<?> query, Map<String, Object> mybatisParam) {
        MybatisParamHolder mybatisParamHolder = new MybatisParamHolder(mybatisParam);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder = this.selectCountToSql(sqlBuilder, configuration, query, mybatisParamHolder);
        sqlBuilder = this.fromToSql(sqlBuilder, configuration, query, mybatisParamHolder);
        sqlBuilder = this.joinsToSql(sqlBuilder, configuration, query, mybatisParamHolder);
        sqlBuilder = this.whereToSql(sqlBuilder, configuration, query, mybatisParamHolder);
        sqlBuilder = this.groupByToSql(sqlBuilder, configuration, query, mybatisParamHolder);
        sqlBuilder = this.havingToSql(sqlBuilder, configuration, query, mybatisParamHolder);
        return sqlBuilder.toString();
    }

    protected StringBuilder selectCountToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                             MybatisParamHolder mybatisParamHolder) {
        sqlBuilder.append("SELECT COUNT(1) ");
        return sqlBuilder;
    }

    protected StringBuilder selectToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                        MybatisParamHolder mybatisParamHolder) {
        Select select = query.getSelect();
        Assert.notNull(select, "select can not be null");
        return select.queryToSqlPart(sqlBuilder, configuration, query, mybatisParamHolder);
    }

    protected StringBuilder fromToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                      MybatisParamHolder mybatisParamHolder) {
        From from = query.getFrom();
        return from.toSqlPart(sqlBuilder, configuration, query, mybatisParamHolder);
    }

    protected abstract StringBuilder limitToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                                MybatisParamHolder mybatisParamHolder);

    protected StringBuilder orderByToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                         MybatisParamHolder mybatisParamHolder) {
        OrderBy order = query.getOrderBy();
        if (order == null || order.getItems() == null) {
            return sqlBuilder;
        } else {
            return order.toSqlPart(sqlBuilder, configuration, query, mybatisParamHolder);
        }
    }

    protected StringBuilder groupByToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                         MybatisParamHolder mybatisParamHolder) {
        GroupBy group = query.getGroupBy();
        if (group == null || group.getItems() == null) {
            return sqlBuilder;
        } else {
            return group.toSqlPart(sqlBuilder, configuration, query, mybatisParamHolder);
        }
    }

    protected StringBuilder whereToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                       MybatisParamHolder mybatisParamHolder) {
        Where where = query.getWhere();
        if (where == null || where.getConditions() == null) {
            return sqlBuilder;
        } else {
            return where.toSqlPart(sqlBuilder, configuration, query, mybatisParamHolder);
        }
    }

    protected StringBuilder havingToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                        MybatisParamHolder mybatisParamHolder) {
        Having having = query.getHaving();
        if (having == null || having.getConditions() == null) {
            return sqlBuilder;
        } else {
            return having.toSqlPart(sqlBuilder, configuration, query, mybatisParamHolder);
        }
    }

    protected StringBuilder joinsToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                       MybatisParamHolder mybatisParamHolder) {
        if (query.getJoins() != null) {
            for (Join join : query.getJoins()) {
                sqlBuilder = join.toSqlPart(sqlBuilder, configuration, query, mybatisParamHolder);
            }
        }
        return sqlBuilder;
    }
}
