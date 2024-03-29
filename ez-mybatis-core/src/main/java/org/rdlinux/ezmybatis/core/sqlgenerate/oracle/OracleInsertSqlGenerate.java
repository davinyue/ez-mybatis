package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractInsertSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.Collection;

public class OracleInsertSqlGenerate extends AbstractInsertSqlGenerate {
    private static volatile OracleInsertSqlGenerate instance;

    private OracleInsertSqlGenerate() {
    }

    public static OracleInsertSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (OracleInsertSqlGenerate.class) {
                if (instance == null) {
                    instance = new OracleInsertSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getBatchInsertSql(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                                    Table table, Collection<Object> models) {
        Assert.notEmpty(models, "models cannot be empty");
        StringBuilder sqlBuilder = new StringBuilder("BEGIN \n");
        for (Object entity : models) {
            String insertSql = this.getInsertSql(configuration, mybatisParamHolder, table, entity);
            sqlBuilder.append(insertSql).append(";\n");
        }
        sqlBuilder.append("END;");
        return sqlBuilder.toString();
    }
}
