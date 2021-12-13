package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractEzQueryToSql;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Alias;
import org.rdlinux.ezmybatis.core.sqlstruct.Limit;

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
    protected StringBuilder limitToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery query,
                                       MybatisParamHolder mybatisParamHolder) {
        Limit limit = query.getLimit();
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
