package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractInsertSqlGenerate;

import java.util.List;

public class OracleInsertSqlGenerate extends AbstractInsertSqlGenerate {
    private static volatile OracleInsertSqlGenerate instance;

    private OracleInsertSqlGenerate() {
    }

    public static OracleInsertSqlGenerate getInstance() {
        if (instance == null) {
            synchronized ( OracleInsertSqlGenerate.class ) {
                if (instance == null) {
                    instance = new OracleInsertSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    protected String getKeywordQM() {
        return "\"";
    }

    @Override
    public String getBatchInsertSql(Configuration configuration, List<Object> entitys) {
        StringBuilder sqlBuilder = new StringBuilder();
        for (int i = 0; i < entitys.size(); i++) {
            String insertSql = this.getInsertSql(configuration, entitys.get(i));
            if (i == 0) {
                insertSql = insertSql.replaceAll("INSERT INTO", "INSERT ALL INTO");
            } else {
                insertSql = insertSql.replaceAll("INSERT INTO", "INTO");
            }
            sqlBuilder.append(insertSql.replaceAll(EzMybatisConstant.MAPPER_PARAM_ENTITY + ".",
                    EzMybatisConstant.MAPPER_PARAM_ENTITYS + "[" + i + "]" + ".")).append(" \n");
            if (i + 1 == entitys.size()) {
                sqlBuilder.append(" SELECT 1 FROM DUAL");
            }
        }
        return sqlBuilder.toString();
    }
}
