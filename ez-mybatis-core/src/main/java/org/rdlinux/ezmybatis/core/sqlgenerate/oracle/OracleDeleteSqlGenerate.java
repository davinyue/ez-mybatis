package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractDeleteSqlGenerate;

public class OracleDeleteSqlGenerate extends AbstractDeleteSqlGenerate {
    private static volatile OracleDeleteSqlGenerate instance;

    private OracleDeleteSqlGenerate() {
    }

    public static OracleDeleteSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (OracleDeleteSqlGenerate.class) {
                if (instance == null) {
                    instance = new OracleDeleteSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    protected String getKeywordQM() {
        return "\"";
    }
}
