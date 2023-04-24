package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractInsertSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.Collection;

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
                                    Table table, Collection<Object> models) {
        Assert.notEmpty(models, "models cannot be empty");
        StringBuilder sqlBuilder = new StringBuilder();
        int i = 0;
        for (Object entity : models) {
            String insertSql = this.getInsertSql(configuration, mybatisParamHolder, table, entity);
            String flag = "VALUES ";
            int vIndex = insertSql.indexOf(flag);
            String valve = insertSql.substring(vIndex + flag.length());
            if (i == 0) {
                String prefix = insertSql.substring(0, vIndex + flag.length());
                sqlBuilder.append(prefix);
            }
            sqlBuilder.append(valve);
            if (i + 1 < models.size()) {
                sqlBuilder.append(", \n");
            }
            i++;
        }
        return sqlBuilder.toString();
    }
}
