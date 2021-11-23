package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractQueryToSql;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlpart.Alias;
import org.rdlinux.ezmybatis.core.sqlpart.EzLimit;

public class OracleQueryToSql extends AbstractQueryToSql {
    private static final String ROW_NUM_ALIAS = "ORACLE_ROW_NO";
    private static volatile OracleQueryToSql instance;

    private OracleQueryToSql() {
    }

    public static OracleQueryToSql getInstance() {
        if (instance == null) {
            synchronized ( OracleQueryToSql.class ) {
                if (instance == null) {
                    instance = new OracleQueryToSql();
                }
            }
        }
        return instance;
    }

    @Override
    public String getKeywordQM() {
        return "\"";
    }

    @Override
    protected StringBuilder selectCountToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery query,
                                             MybatisParamHolder mybatisParamHolder) {
        sqlBuilder.append("SELECT COUNT( 1 ) ");
        return sqlBuilder;
    }

    @Override
    protected StringBuilder limitToSql(StringBuilder sqlBuilder, Configuration configuration, EzLimit limit,
                                       MybatisParamHolder mybatisParamHolder) {
        if (limit == null) {
            return sqlBuilder;
        }
        String bodyAlias = Alias.getAlias();
        String outSqlBody = "SELECT " + bodyAlias + ".*, ROWNUM " + ROW_NUM_ALIAS +
                " FROM (" + sqlBuilder + ") " + bodyAlias;

        String outAlias = Alias.getAlias();
        String outSqlHead = "SELECT " + outAlias + ".* FROM ( ";
        String outSqlTail = " ) " + outAlias + " WHERE " + outAlias + "." + ROW_NUM_ALIAS + " > " + limit.getSkip()
                + " AND " + outAlias + "." + ROW_NUM_ALIAS + " <= " + (limit.getSkip() + limit.getSize());
        return new StringBuilder().append(outSqlHead).append(outSqlBody).append(outSqlTail);
    }
}
