package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractUpdateSqlGenerate;

import java.util.List;

public class OracleUpdateSqlGenerate extends AbstractUpdateSqlGenerate {
    private static volatile OracleUpdateSqlGenerate instance;

    private OracleUpdateSqlGenerate() {
    }

    public static OracleUpdateSqlGenerate getInstance() {
        if (instance == null) {
            synchronized ( OracleUpdateSqlGenerate.class ) {
                if (instance == null) {
                    instance = new OracleUpdateSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getBatchUpdateSql(Configuration configuration, List<Object> entitys, boolean isReplace) {
        return "BEGIN " + super.getBatchUpdateSql(configuration, entitys, isReplace) + " END;";
    }

    @Override
    protected String getKeywordQM() {
        return "\"";
    }
}
