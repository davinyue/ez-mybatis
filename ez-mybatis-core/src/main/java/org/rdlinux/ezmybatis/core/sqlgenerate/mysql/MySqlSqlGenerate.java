package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerate;

public class MySqlSqlGenerate implements SqlGenerate {
    private static volatile MySqlSqlGenerate instance;

    private MySqlSqlGenerate() {
    }

    public static MySqlSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (MySqlSqlGenerate.class) {
                if (instance == null) {
                    instance = new MySqlSqlGenerate();
                }
            }
        }
        return instance;
    }


    @Override
    public String getInsertSql(Object entity) {
        return MySqlInsertSqlGenerate.getInstance().getInsertSql(entity);
    }

    @Override
    public String getSelectByPrimaryKeySql(Class<?> ntClass, Object id) {
        return MysqlSelectByPrimaryKeySqlGenerate.getInstance().getSelectByPrimaryKeySql(ntClass, id);
    }
}
