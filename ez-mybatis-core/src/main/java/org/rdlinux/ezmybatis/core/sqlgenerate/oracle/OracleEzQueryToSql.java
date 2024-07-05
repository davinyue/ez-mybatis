package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractEzQueryToSql;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.GroupBy;
import org.rdlinux.ezmybatis.core.sqlstruct.Limit;
import org.rdlinux.ezmybatis.core.sqlstruct.OrderBy;
import org.rdlinux.ezmybatis.core.sqlstruct.Page;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

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
    protected StringBuilder onWhereToSqlEnd(boolean isPage, StringBuilder sqlBuilder, Configuration configuration,
                                            EzQuery<?> query, MybatisParamHolder paramHolder) {
        if (!isPage || query.getPage() != null) {
            return sqlBuilder;
        }
        Limit limit = query.getLimit();
        if (limit == null) {
            return sqlBuilder;
        }
        if (query.getWhere() == null) {
            sqlBuilder.append(" WHERE 1 = 1 ");
        }
        Converter<Limit> converter = EzMybatisContent.getConverter(configuration, Limit.class);
        return converter.buildSql(Converter.Type.SELECT, sqlBuilder, configuration, limit, paramHolder);
    }

    @Override
    protected StringBuilder whereToSql(boolean isPage, StringBuilder sqlBuilder, Configuration configuration,
                                       EzQuery<?> query, MybatisParamHolder mybatisParamHolder) {
        StringBuilder sql = super.whereToSql(isPage, sqlBuilder, configuration, query, mybatisParamHolder);
        Page page = query.getPage();
        GroupBy groupBy = query.getGroupBy();
        OrderBy orderBy = query.getOrderBy();
        EzMybatisConfig ezMybatisConfig = EzMybatisContent.getContentConfig(configuration).getEzMybatisConfig();
        if (isPage && !ezMybatisConfig.isEnableOracleOffsetFetchPage() && page != null &&
                (groupBy == null || groupBy.getItems() == null || groupBy.getItems().isEmpty())
                && (orderBy == null || orderBy.getItems() == null || orderBy.getItems().isEmpty())) {
            if (query.getWhere() == null) {
                sql.append(" WHERE ");
            } else {
                sql.append(" AND ");
            }
            sql.append(" ROWNUM <= ").append(page.getSkip() + page.getSize());
        }
        return sql;
    }

    @Override
    protected StringBuilder limitToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                       MybatisParamHolder paramHolder) {
        return sqlBuilder;
    }
}
