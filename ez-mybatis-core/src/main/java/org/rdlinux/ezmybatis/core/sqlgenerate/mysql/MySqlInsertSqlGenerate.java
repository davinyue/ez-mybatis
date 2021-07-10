package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractInsertSqlGenerate;

public class MySqlInsertSqlGenerate extends AbstractInsertSqlGenerate {
    private static volatile MySqlInsertSqlGenerate instance;

    private MySqlInsertSqlGenerate() {
    }

    public static MySqlInsertSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (MySqlInsertSqlGenerate.class) {
                if (instance == null) {
                    instance = new MySqlInsertSqlGenerate();
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
