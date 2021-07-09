package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractSelectByPrimaryKeySqlGenerate;

public class OracleSelectByPrimaryKeySqlGenerate extends AbstractSelectByPrimaryKeySqlGenerate {
    private static volatile OracleSelectByPrimaryKeySqlGenerate instance;

    private OracleSelectByPrimaryKeySqlGenerate() {
    }

    public static OracleSelectByPrimaryKeySqlGenerate getInstance() {
        if (instance == null) {
            synchronized (OracleSelectByPrimaryKeySqlGenerate.class) {
                if (instance == null) {
                    instance = new OracleSelectByPrimaryKeySqlGenerate();
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
