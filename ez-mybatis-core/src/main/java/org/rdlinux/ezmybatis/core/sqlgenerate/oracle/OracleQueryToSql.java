package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractQueryToSql;

public class OracleQueryToSql extends AbstractQueryToSql {
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

}
