package ink.dvc.ezmybatis.core.sqlgenerate.oracle;

import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.EzDelete;
import ink.dvc.ezmybatis.core.sqlgenerate.AbstractDeleteSqlGenerate;

import java.util.List;
import java.util.Map;

public class OracleDeleteSqlGenerate extends AbstractDeleteSqlGenerate {
    private static volatile OracleDeleteSqlGenerate instance;

    private OracleDeleteSqlGenerate() {
        super(OracleSelectSqlGenerate.getInstance());
    }

    public static OracleDeleteSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (OracleDeleteSqlGenerate.class) {
                if (instance == null) {
                    instance = new OracleDeleteSqlGenerate();
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
    public String getDeleteSql(Configuration configuration, EzDelete delete, Map<String, Object> mybatisParam) {
        return OracleEzDeleteToSql.getInstance().toSql(configuration, delete, mybatisParam);
    }

    @Override
    public String getDeleteSql(Configuration configuration, List<EzDelete> deletes, Map<String, Object> mybatisParam) {
        return OracleEzDeleteToSql.getInstance().toSql(configuration, deletes, mybatisParam);
    }
}
