package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractDeleteSqlGenerate;

public class MySqlDeleteSqlGenerate extends AbstractDeleteSqlGenerate {
    private static volatile MySqlDeleteSqlGenerate instance;

    private MySqlDeleteSqlGenerate() {
    }

    public static MySqlDeleteSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (MySqlDeleteSqlGenerate.class) {
                if (instance == null) {
                    instance = new MySqlDeleteSqlGenerate();
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
