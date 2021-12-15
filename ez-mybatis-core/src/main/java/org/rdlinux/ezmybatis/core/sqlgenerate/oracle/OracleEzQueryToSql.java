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
    private static final String ROW_NUM_ALIAS = "ORACLE_ROW_NO";
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
    protected StringBuilder selectToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery query,
                                        MybatisParamHolder mybatisParamHolder) {
        StringBuilder sql = super.selectToSql(sqlBuilder, configuration, query, mybatisParamHolder);
        Limit limit = query.getLimit();
        GroupBy groupBy = query.getGroupBy();
        OrderBy orderBy = query.getOrderBy();
        if (limit != null && (groupBy == null || groupBy.getItems() == null || groupBy.getItems().isEmpty())
                && (orderBy == null || orderBy.getItems() == null || orderBy.getItems().isEmpty())) {
            sql.append(", ROWNUM ").append(ROW_NUM_ALIAS).append(" ");
        }
        return sql;
    }

    @Override
    protected StringBuilder whereToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery query,
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

    @Override
    protected StringBuilder limitToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery query,
                                       MybatisParamHolder mybatisParamHolder) {
        Limit limit = query.getLimit();
        if (limit == null) {
            return sqlBuilder;
        }
        GroupBy groupBy = query.getGroupBy();
        OrderBy orderBy = query.getOrderBy();
        if ((groupBy == null || groupBy.getItems() == null || groupBy.getItems().isEmpty())
                && (orderBy == null || orderBy.getItems() == null || orderBy.getItems().isEmpty())) {
            String bodyAlias = Alias.getAlias();
            return new StringBuilder("SELECT ").append(bodyAlias).append(".* ")
                    .append(" FROM ( ").append(sqlBuilder).append(" ) ").append(bodyAlias)
                    .append(" WHERE ").append(bodyAlias).append(".").append(ROW_NUM_ALIAS).append(" > ")
                    .append(limit.getSkip());
        } else {
            String bodyAlias = Alias.getAlias();
            String outSqlBody = "SELECT " + bodyAlias + ".*, ROWNUM " + ROW_NUM_ALIAS +
                    " FROM (" + sqlBuilder + ") " + bodyAlias + " WHERE ROWNUM <= " +
                    (limit.getSkip() + limit.getSize()) + " ";
            String outAlias = Alias.getAlias();
            String outSqlHead = "SELECT " + outAlias + ".* FROM ( ";
            String outSqlTail = " ) " + outAlias + " WHERE " + outAlias + "." + ROW_NUM_ALIAS + " > " + limit.getSkip();
            return new StringBuilder().append(outSqlHead).append(outSqlBody).append(outSqlTail);
        }
    }
}
