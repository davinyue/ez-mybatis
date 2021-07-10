package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractUpdateSqlGenerate;

public class MySqlUpdateSqlGenerate extends AbstractUpdateSqlGenerate {
    private static volatile MySqlUpdateSqlGenerate instance;

    private MySqlUpdateSqlGenerate() {
    }

    public static MySqlUpdateSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (MySqlUpdateSqlGenerate.class) {
                if (instance == null) {
                    instance = new MySqlUpdateSqlGenerate();
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
