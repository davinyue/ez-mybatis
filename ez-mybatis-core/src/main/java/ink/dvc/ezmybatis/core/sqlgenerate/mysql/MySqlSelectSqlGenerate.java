package ink.dvc.ezmybatis.core.sqlgenerate.mysql;

import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.EzQuery;
import ink.dvc.ezmybatis.core.sqlgenerate.AbstractSelectSqlGenerate;

import java.util.Map;

public class MySqlSelectSqlGenerate extends AbstractSelectSqlGenerate {
    private static volatile MySqlSelectSqlGenerate instance;

    private MySqlSelectSqlGenerate() {
    }

    public static MySqlSelectSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (MySqlInsertSqlGenerate.class) {
                if (instance == null) {
                    instance = new MySqlSelectSqlGenerate();
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
    public String getQuerySql(Configuration configuration, EzQuery<?> query, Map<String, Object> mybatisParam) {
        return MySqlEzQueryToSql.getInstance().toSql(configuration, query, mybatisParam);
    }

    @Override
    public String getQueryCountSql(Configuration configuration, EzQuery<?> query, Map<String, Object> mybatisParam) {
        return MySqlEzQueryToSql.getInstance().toCountSql(configuration, query, mybatisParam);
    }
}
