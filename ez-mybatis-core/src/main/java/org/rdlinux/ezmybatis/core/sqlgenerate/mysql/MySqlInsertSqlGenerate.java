package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.rdlinux.ezmybatis.core.sqlgenerate.InsertSqlGenerate;

public class MySqlInsertSqlGenerate implements InsertSqlGenerate {
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
    public String getInsertSql(Object entity) {
        return "INSERT INTO `user` (`name`) VALUES ('李四');";
    }
}
