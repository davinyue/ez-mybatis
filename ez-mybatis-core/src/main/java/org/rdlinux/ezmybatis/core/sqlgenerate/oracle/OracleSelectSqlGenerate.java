package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractSelectSqlGenerate;

public class OracleSelectSqlGenerate extends AbstractSelectSqlGenerate {
    private static volatile OracleSelectSqlGenerate instance;

    private OracleSelectSqlGenerate() {
    }

    public static OracleSelectSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (OracleSelectSqlGenerate.class) {
                if (instance == null) {
                    instance = new OracleSelectSqlGenerate();
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
