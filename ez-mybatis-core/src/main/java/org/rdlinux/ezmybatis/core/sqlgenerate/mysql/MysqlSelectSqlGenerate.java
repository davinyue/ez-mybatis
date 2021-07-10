package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractSelectSqlGenerate;

public class MysqlSelectSqlGenerate extends AbstractSelectSqlGenerate {
    private static volatile MysqlSelectSqlGenerate instance;

    private MysqlSelectSqlGenerate() {
    }

    public static MysqlSelectSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (MySqlInsertSqlGenerate.class) {
                if (instance == null) {
                    instance = new MysqlSelectSqlGenerate();
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
