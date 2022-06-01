package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractInsertSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
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
                                    Collection<Object> entitys) {
        Assert.notEmpty(entitys, "entitys cannot be empty");
        StringBuilder sqlBuilder = new StringBuilder("BEGIN \n");
        for (Object entity : entitys) {
            String insertSql = this.getInsertSql(configuration, mybatisParamHolder, entity);
            sqlBuilder.append(insertSql).append(";\n");
        }
        sqlBuilder.append("END;");
        return sqlBuilder.toString();
    }
}
