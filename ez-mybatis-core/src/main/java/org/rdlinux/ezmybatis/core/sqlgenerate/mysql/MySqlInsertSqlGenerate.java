package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractInsertSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;

import java.util.List;

public class MySqlInsertSqlGenerate extends AbstractInsertSqlGenerate {
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
    public String getBatchInsertSql(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                                    List<Object> entitys) {
        StringBuilder sqlBuilder = new StringBuilder();
        for (int i = 0; i < entitys.size(); i++) {
            String insertSql = this.getInsertSql(configuration, mybatisParamHolder, entitys.get(i));
            String flag = "VALUES ";
            int vIndex = insertSql.indexOf(flag);
            String valve = insertSql.substring(vIndex + flag.length());
            if (i == 0) {
                String prefix = insertSql.substring(0, vIndex + flag.length());
                sqlBuilder.append(prefix);
            }
            sqlBuilder.append(valve);
            if (i + 1 < entitys.size()) {
                sqlBuilder.append(", ");
            }
        }
        return sqlBuilder.toString();
    }
}
