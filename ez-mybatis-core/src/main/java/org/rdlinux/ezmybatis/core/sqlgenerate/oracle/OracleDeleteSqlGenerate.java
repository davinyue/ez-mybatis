package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractDeleteSqlGenerate;

public class OracleDeleteSqlGenerate extends AbstractDeleteSqlGenerate {
    private static volatile OracleDeleteSqlGenerate instance;

    private OracleDeleteSqlGenerate() {
        super(OracleSelectSqlGenerate.getInstance());
    }

    public static OracleDeleteSqlGenerate getInstance() {
        if (instance == null) {
            synchronized ( OracleDeleteSqlGenerate.class ) {
                if (instance == null) {
                    instance = new OracleDeleteSqlGenerate();
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
