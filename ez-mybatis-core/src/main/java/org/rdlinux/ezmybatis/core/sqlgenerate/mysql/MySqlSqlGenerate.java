package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.apache.ibatis.session.Configuration;
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
    public String getInsertSql(Configuration configuration, Object entity) {
        return MySqlInsertSqlGenerate.getInstance().getInsertSql(configuration, entity);
    }

    @Override
    public String getBatchInsertSql(Configuration configuration, List<Object> entitys) {
        return MySqlInsertSqlGenerate.getInstance().getBatchInsertSql(configuration, entitys);
    }

    @Override
    public String getSelectByIdSql(Configuration configuration, Class<?> ntClass, Object id) {
        return MysqlSelectSqlGenerate.getInstance().getSelectByIdSql(configuration, ntClass, id);
    }

    @Override
    public String getSelectByIdsSql(Configuration configuration, Class<?> ntClass, List<?> ids) {
        return MysqlSelectSqlGenerate.getInstance().getSelectByIdsSql(configuration, ntClass, ids);
    }
}
