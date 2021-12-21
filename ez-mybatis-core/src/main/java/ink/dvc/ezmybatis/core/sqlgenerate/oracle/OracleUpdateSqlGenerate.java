package ink.dvc.ezmybatis.core.sqlgenerate.oracle;

import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.EzUpdate;
import ink.dvc.ezmybatis.core.sqlgenerate.AbstractUpdateSqlGenerate;

import java.util.List;
import java.util.Map;

public class OracleUpdateSqlGenerate extends AbstractUpdateSqlGenerate {
    private static volatile OracleUpdateSqlGenerate instance;

    private OracleUpdateSqlGenerate() {
    }

    public static OracleUpdateSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (OracleUpdateSqlGenerate.class) {
                if (instance == null) {
                    instance = new OracleUpdateSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getBatchUpdateSql(Configuration configuration, List<Object> entitys, boolean isReplace) {
        return "BEGIN \n" + super.getBatchUpdateSql(configuration, entitys, isReplace) + " END;";
    }

    @Override
    public String getUpdateSql(Configuration configuration, EzUpdate update, Map<String, Object> mybatisParam) {
        return OracleEzUpdateToSql.getInstance().toSql(configuration, update, mybatisParam);
    }

    @Override
    public String getUpdateSql(Configuration configuration, List<EzUpdate> updates, Map<String, Object> mybatisParam) {
        return OracleEzUpdateToSql.getInstance().toSql(configuration, updates, mybatisParam);
    }

    @Override
    public String getKeywordQM() {
        return "\"";
    }
}
