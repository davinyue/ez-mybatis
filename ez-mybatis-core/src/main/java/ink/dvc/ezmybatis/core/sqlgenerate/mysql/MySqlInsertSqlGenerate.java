package ink.dvc.ezmybatis.core.sqlgenerate.mysql;

import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.constant.EzMybatisConstant;
import ink.dvc.ezmybatis.core.sqlgenerate.AbstractInsertSqlGenerate;

import java.util.List;

public class MySqlInsertSqlGenerate extends AbstractInsertSqlGenerate {
    private static volatile MySqlInsertSqlGenerate instance;

    private MySqlInsertSqlGenerate() {
    }

    public static MySqlInsertSqlGenerate getInstance() {
        if (instance == null) {
            synchronized ( MySqlInsertSqlGenerate.class ) {
                if (instance == null) {
                    instance = new MySqlInsertSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getKeywordQM() {
        return "`";
    }

    @Override
    public String getBatchInsertSql(Configuration configuration, List<Object> entitys) {
        StringBuilder sqlBuilder = new StringBuilder();
        for (int i = 0; i < entitys.size(); i++) {
            String insertSql = this.getInsertSql(configuration, entitys.get(i));
            String flag = "VALUES ";
            int vIndex = insertSql.indexOf(flag);
            String valve = insertSql.substring(vIndex + flag.length());
            if (i == 0) {
                String prefix = insertSql.substring(0, vIndex + flag.length());
                sqlBuilder.append(prefix);
            }
            sqlBuilder.append(valve.replaceAll(EzMybatisConstant.MAPPER_PARAM_ENTITY + ".",
                    EzMybatisConstant.MAPPER_PARAM_ENTITYS + "[" + i + "]" + "."));
            if (i + 1 < entitys.size()) {
                sqlBuilder.append(", ");
            }
        }
        return sqlBuilder.toString();
    }
}
