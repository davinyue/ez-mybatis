package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractQueryToSql;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlpart.EzLimit;

public class MySqlQueryToSql extends AbstractQueryToSql {
    private static volatile MySqlQueryToSql instance;

    private MySqlQueryToSql() {
    }

    public static MySqlQueryToSql getInstance() {
        if (instance == null) {
            synchronized ( MySqlQueryToSql.class ) {
                if (instance == null) {
                    instance = new MySqlQueryToSql();
                }
            }
        }
        return instance;
    }

    @Override
    public String getKeywordQM() {
        return "`";
    }

    @Override
    protected StringBuilder limitToSql(StringBuilder sqlBuilder, Configuration configuration, EzLimit limit,
                                       MybatisParamHolder mybatisParamHolder) {
        if (limit == null) {
            return sqlBuilder;
        }
        sqlBuilder.append(" ").append("LIMIT ").append(limit.getSkip()).append(", ").append(limit.getSize());
        return sqlBuilder;
    }
}
