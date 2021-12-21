package ink.dvc.ezmybatis.core.sqlgenerate.mysql;

import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.EzUpdate;
import ink.dvc.ezmybatis.core.sqlgenerate.AbstractUpdateSqlGenerate;

import java.util.List;
import java.util.Map;

public class MySqlUpdateSqlGenerate extends AbstractUpdateSqlGenerate {
    private static volatile MySqlUpdateSqlGenerate instance;

    private MySqlUpdateSqlGenerate() {
    }

    public static MySqlUpdateSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (MySqlUpdateSqlGenerate.class) {
                if (instance == null) {
                    instance = new MySqlUpdateSqlGenerate();
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
    public String getUpdateSql(Configuration configuration, EzUpdate update, Map<String, Object> mybatisParam) {
        return MySqlEzUpdateToSql.getInstance().toSql(configuration, update, mybatisParam);
    }

    @Override
    public String getUpdateSql(Configuration configuration, List<EzUpdate> updates, Map<String, Object> mybatisParam) {
        return MySqlEzUpdateToSql.getInstance().toSql(configuration, updates, mybatisParam);
    }
}
