package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractEzQueryToSql;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
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
    protected StringBuilder whereToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                       MybatisParamHolder mybatisParamHolder) {
        StringBuilder sql = super.whereToSql(sqlBuilder, configuration, query, mybatisParamHolder);
        Limit limit = query.getLimit();
        GroupBy groupBy = query.getGroupBy();
        OrderBy orderBy = query.getOrderBy();
        if (limit != null && (groupBy == null || groupBy.getItems() == null || groupBy.getItems().isEmpty())
                && (orderBy == null || orderBy.getItems() == null || orderBy.getItems().isEmpty())) {
            if (query.getWhere() == null) {
                sql.append(" WHERE ");
            } else {
                sql.append(" AND ");
            }
            sql.append(" ROWNUM <= ").append(limit.getSkip() + limit.getSize());
        }
        return sql;
    }
}
