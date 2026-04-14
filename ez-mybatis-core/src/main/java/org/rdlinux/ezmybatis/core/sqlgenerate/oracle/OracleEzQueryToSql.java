package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractEzQueryToSql;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
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
    protected void onWhereToSqlEnd(boolean isPage, SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        if (!isPage || query.getPage() != null) {
            return;
        }
        Limit limit = query.getLimit();
        if (limit == null) {
            return;
        }
        if (query.getWhere() == null) {
            sqlGenerateContext.getSqlBuilder().append(" WHERE 1 = 1 ");
        }
        Converter<Limit> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(), Limit.class);
        converter.buildSql(Converter.Type.SELECT, limit, sqlGenerateContext);
    }

    @Override
    protected void whereToSql(boolean isPage, SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        super.whereToSql(isPage, sqlGenerateContext, query);
        Page page = query.getPage();
        GroupBy groupBy = query.getGroupBy();
        OrderBy orderBy = query.getOrderBy();
        EzMybatisConfig ezMybatisConfig = EzMybatisContent.getContentConfig(sqlGenerateContext.getConfiguration())
                .getEzMybatisConfig();
        if (isPage && !ezMybatisConfig.isEnableOracleOffsetFetchPage() && page != null &&
                (groupBy == null || groupBy.getItems() == null || groupBy.getItems().isEmpty())
                && (orderBy == null || orderBy.getItems() == null || orderBy.getItems().isEmpty())) {
            if (query.getWhere() == null) {
                sqlGenerateContext.getSqlBuilder().append(" WHERE ");
            } else {
                sqlGenerateContext.getSqlBuilder().append(" AND ");
            }
            sqlGenerateContext.getSqlBuilder().append(" ROWNUM <= ").append(page.getSkip() + page.getSize());
        }
    }

    @Override
    protected void limitToSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
    }
}
