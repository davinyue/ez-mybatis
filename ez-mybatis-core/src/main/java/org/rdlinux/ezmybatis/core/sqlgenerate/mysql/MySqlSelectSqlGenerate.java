package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractSelectSqlGenerate;

public class MySqlSelectSqlGenerate extends AbstractSelectSqlGenerate {
    private static volatile MySqlSelectSqlGenerate instance;

    private MySqlSelectSqlGenerate() {
    }

    public static MySqlSelectSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (MySqlInsertSqlGenerate.class) {
                if (instance == null) {
                    instance = new MySqlSelectSqlGenerate();
                }
            }
        }
        return instance;
    }


    @Override
    protected String getKeywordQM() {
        return "`";
    }
}
