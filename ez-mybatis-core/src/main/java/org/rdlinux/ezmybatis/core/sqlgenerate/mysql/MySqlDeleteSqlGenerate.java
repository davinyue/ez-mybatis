package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractDeleteSqlGenerate;

public class MySqlDeleteSqlGenerate extends AbstractDeleteSqlGenerate {
    private static volatile MySqlDeleteSqlGenerate instance;

    private MySqlDeleteSqlGenerate() {
        super(MySqlSelectSqlGenerate.getInstance());
    }

    public static MySqlDeleteSqlGenerate getInstance() {
        if (instance == null) {
            synchronized ( MySqlDeleteSqlGenerate.class ) {
                if (instance == null) {
                    instance = new MySqlDeleteSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getKeywordQM() {
        return "`";
    }
}
