package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerate;

import java.util.List;

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
    public String getSelectByIdSql(Class<?> ntClass, Object id) {
        return MysqlSelectSqlGenerate.getInstance().getSelectByIdSql(ntClass, id);
    }

    @Override
    public String getSelectByIdsSql(Class<?> ntClass, List<?> ids) {
        return MysqlSelectSqlGenerate.getInstance().getSelectByIdsSql(ntClass, ids);
    }
}
