package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractEzQueryToSql;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Alias;
import org.rdlinux.ezmybatis.core.sqlstruct.GroupBy;
import org.rdlinux.ezmybatis.core.sqlstruct.Limit;
import org.rdlinux.ezmybatis.core.sqlstruct.OrderBy;

public class OracleEzQueryToSql extends AbstractEzQueryToSql {
    private static volatile OracleEzQueryToSql instance;

    private OracleEzQueryToSql() {
    }

    public static OracleEzQueryToSql getInstance() {
        if (instance == null) {
            synchronized (OracleEzQueryToSql.class) {
                if (instance == null) {
                    instance = new OracleEzQueryToSql();
                }
            }
        }
        return instance;
    }

    @Override
    public String toCountSql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder = this.selectCountToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.fromToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.joinsToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = super.whereToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.groupByToSql(sqlBuilder, configuration, query, paramHolder);
        sqlBuilder = this.havingToSql(sqlBuilder, configuration, query, paramHolder);
        if (query.getGroupBy() != null && !query.getGroupBy().getItems().isEmpty()) {
            return "SELECT COUNT(*) FROM ( " + sqlBuilder.toString() + ") " + Alias.getAlias();
        } else {
            return sqlBuilder.toString();
        }
    }

    @Override
    protected StringBuilder whereToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                       MybatisParamHolder mybatisParamHolder) {
        StringBuilder sql = super.whereToSql(sqlBuilder, configuration, query, mybatisParamHolder);
        Limit limit = query.getLimit();
        GroupBy groupBy = query.getGroupBy();
        OrderBy orderBy = query.getOrderBy();
        if (limit != null && (groupBy == null || groupBy.getItems() == null || groupBy.getItems().isEmpty())
                && (orderBy == null || orderBy.getItems() == null || orderBy.getItems().isEmpty())) {
            if (sql.indexOf("WHERE") == -1) {
                sql.append(" WHERE ");
            } else {
                sql.append(" AND ");
            }
            sql.append(" ROWNUM <= ").append(limit.getSkip() + limit.getSize());
        }
        return sql;
    }
}
