package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Alias;
import org.rdlinux.ezmybatis.core.sqlstruct.GroupBy;
import org.rdlinux.ezmybatis.core.sqlstruct.Limit;
import org.rdlinux.ezmybatis.core.sqlstruct.OrderBy;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class OracleLimitConverter extends AbstractConverter<Limit> implements Converter<Limit> {
    public static final String ROW_NUM_ALIAS = "ORA_ROWNUM";
    private static volatile OracleLimitConverter instance;

    protected OracleLimitConverter() {
    }

    public static OracleLimitConverter getInstance() {
        if (instance == null) {
            synchronized (OracleLimitConverter.class) {
                if (instance == null) {
                    instance = new OracleLimitConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doToSqlPart(Type type, StringBuilder sqlBuilder, Configuration configuration, Limit limit,
                                        MybatisParamHolder mybatisParamHolder) {
        if (limit == null) {
            return sqlBuilder;
        }
        GroupBy groupBy = null;
        OrderBy orderBy = null;
        EzQuery<?> query = limit.getQuery();
        if (query != null) {
            groupBy = query.getGroupBy();
            orderBy = query.getOrderBy();
        }
        if ((groupBy == null || groupBy.getItems() == null || groupBy.getItems().isEmpty())
                && (orderBy == null || orderBy.getItems() == null || orderBy.getItems().isEmpty())) {
            String bodyAlias = Alias.getAlias();
            return new StringBuilder("SELECT ").append(bodyAlias).append(".* ")
                    .append(" FROM ( ").append(sqlBuilder).append(" ) ").append(bodyAlias)
                    .append(" WHERE ").append(bodyAlias).append(".").append(ROW_NUM_ALIAS)
                    .append(" > ")
                    .append(limit.getSkip());
        } else {
            String bodyAlias = Alias.getAlias();
            String outSqlBody = "SELECT " + bodyAlias + ".*, ROWNUM " + ROW_NUM_ALIAS +
                    " FROM (" + sqlBuilder + ") " + bodyAlias + " WHERE ROWNUM <= " +
                    (limit.getSkip() + limit.getSize()) + " ";
            String outAlias = Alias.getAlias();
            String outSqlHead = "SELECT " + outAlias + ".* FROM ( ";
            String outSqlTail = " ) " + outAlias + " WHERE " + outAlias + "." + ROW_NUM_ALIAS + " > " +
                    limit.getSkip();
            return new StringBuilder().append(outSqlHead).append(outSqlBody).append(outSqlTail);
        }
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.ORACLE;
    }
}
