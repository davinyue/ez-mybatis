package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractSelectByPrimaryKeySqlGenerate;

public class MysqlSelectByPrimaryKeySqlGenerate extends AbstractSelectByPrimaryKeySqlGenerate {
    private static volatile MysqlSelectByPrimaryKeySqlGenerate instance;

    private MysqlSelectByPrimaryKeySqlGenerate() {
    }

    public static MysqlSelectByPrimaryKeySqlGenerate getInstance() {
        if (instance == null) {
            synchronized (MySqlInsertSqlGenerate.class) {
                if (instance == null) {
                    instance = new MysqlSelectByPrimaryKeySqlGenerate();
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
