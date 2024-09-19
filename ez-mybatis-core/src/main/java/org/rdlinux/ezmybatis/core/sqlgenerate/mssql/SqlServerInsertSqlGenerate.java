package org.rdlinux.ezmybatis.core.sqlgenerate.mssql;

import org.rdlinux.ezmybatis.core.sqlgenerate.mysql.MySqlInsertSqlGenerate;

public class SqlServerInsertSqlGenerate extends MySqlInsertSqlGenerate {
    private static volatile SqlServerInsertSqlGenerate instance;

    protected SqlServerInsertSqlGenerate() {
    }

    public static SqlServerInsertSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (SqlServerInsertSqlGenerate.class) {
                if (instance == null) {
                    instance = new SqlServerInsertSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    protected boolean insertByQueryAppendParenthesis() {
        return false;
    }
}
