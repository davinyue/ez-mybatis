package ink.dvc.ezmybatis.core.sqlgenerate.oracle;

import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.EzQuery;
import ink.dvc.ezmybatis.core.sqlgenerate.AbstractSelectSqlGenerate;

import java.util.Map;

public class OracleSelectSqlGenerate extends AbstractSelectSqlGenerate {
    private static volatile OracleSelectSqlGenerate instance;

    private OracleSelectSqlGenerate() {
    }

    public static OracleSelectSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (OracleSelectSqlGenerate.class) {
                if (instance == null) {
                    instance = new OracleSelectSqlGenerate();
                }
            }
        }
        return instance;
    }


    @Override
    public String getKeywordQM() {
        return "\"";
    }

    @Override
    public String getQuerySql(Configuration configuration, EzQuery<?> query, Map<String, Object> mybatisParam) {
        return OracleEzQueryToSql.getInstance().toSql(configuration, query, mybatisParam);
    }

    @Override
    public String getQueryCountSql(Configuration configuration, EzQuery<?> query, Map<String, Object> mybatisParam) {
        return OracleEzQueryToSql.getInstance().toCountSql(configuration, query, mybatisParam);
    }
}
