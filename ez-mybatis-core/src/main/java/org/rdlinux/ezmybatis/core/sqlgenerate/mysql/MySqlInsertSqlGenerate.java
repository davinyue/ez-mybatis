package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractInsertSqlGenerate;

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
    protected String getKeywordQM() {
        return "`";
    }

    @Override
    public String getBatchInsertSql(Configuration configuration, List<Object> entitys) {
        String insertSql = this.getInsertSql(configuration, entitys.get(0));
        String flag = "VALUES ";
        int vIndex = insertSql.indexOf(flag);
        String valve = insertSql.substring(vIndex + flag.length());
        String prefix = insertSql.substring(0, vIndex + flag.length());
        StringBuilder sqlBuilder = new StringBuilder(prefix);
        for (int i = 0; i < entitys.size(); i++) {
            sqlBuilder.append(valve.replaceAll(EzMybatisConstant.MAPPER_PARAM_ENTITY + ".",
                    EzMybatisConstant.MAPPER_PARAM_ENTITYS + "[" + i + "]" + "."));
            if (i + 1 < entitys.size()) {
                sqlBuilder.append(", ");
            }
        }
        return sqlBuilder.toString();
    }
}
