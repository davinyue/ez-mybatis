package ink.dvc.ezmybatis.core.sqlgenerate.oracle;

import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.EzDelete;
import ink.dvc.ezmybatis.core.EzQuery;
import ink.dvc.ezmybatis.core.EzUpdate;
import ink.dvc.ezmybatis.core.sqlgenerate.SqlGenerate;

import java.util.List;
import java.util.Map;

public class OracleSqlGenerate implements SqlGenerate {
    private static volatile OracleSqlGenerate instance;

    private OracleSqlGenerate() {
    }

    public static OracleSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (OracleSqlGenerate.class) {
                if (instance == null) {
                    instance = new OracleSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getInsertSql(Configuration configuration, Object entity) {
        return OracleInsertSqlGenerate.getInstance().getInsertSql(configuration, entity);
    }

    @Override
    public String getBatchInsertSql(Configuration configuration, List<Object> entitys) {
        return OracleInsertSqlGenerate.getInstance().getBatchInsertSql(configuration, entitys);
    }

    @Override
    public String getSelectByIdSql(Configuration configuration, Class<?> ntClass, Object id) {
        return OracleSelectSqlGenerate.getInstance().getSelectByIdSql(configuration, ntClass, id);
    }

    @Override
    public String getSelectByIdsSql(Configuration configuration, Class<?> ntClass, List<?> ids) {
        return OracleSelectSqlGenerate.getInstance().getSelectByIdsSql(configuration, ntClass, ids);
    }

    @Override
    public String getQuerySql(Configuration configuration, EzQuery<?> query, Map<String, Object> mybatisParam) {
        return OracleSelectSqlGenerate.getInstance().getQuerySql(configuration, query, mybatisParam);
    }

    @Override
    public String getQueryCountSql(Configuration configuration, EzQuery<?> query, Map<String, Object> mybatisParam) {
        return OracleSelectSqlGenerate.getInstance().getQueryCountSql(configuration, query, mybatisParam);
    }

    @Override
    public String getUpdateSql(Configuration configuration, Object entity, boolean isReplace) {
        return OracleUpdateSqlGenerate.getInstance().getUpdateSql(configuration, entity, isReplace);
    }

    @Override
    public String getBatchUpdateSql(Configuration configuration, List<Object> entitys, boolean isReplace) {
        return OracleUpdateSqlGenerate.getInstance().getBatchUpdateSql(configuration, entitys, isReplace);
    }

    @Override
    public String getUpdateSql(Configuration configuration, EzUpdate update, Map<String, Object> mybatisParam) {
        return OracleUpdateSqlGenerate.getInstance().getUpdateSql(configuration, update, mybatisParam);
    }

    @Override
    public String getUpdateSql(Configuration configuration, List<EzUpdate> updates, Map<String, Object> mybatisParam) {
        return OracleUpdateSqlGenerate.getInstance().getUpdateSql(configuration, updates, mybatisParam);
    }

    @Override
    public String getDeleteByIdSql(Configuration configuration, Class<?> ntClass, Object id) {
        return OracleDeleteSqlGenerate.getInstance().getDeleteByIdSql(configuration, ntClass, id);
    }

    @Override
    public String getBatchDeleteByIdSql(Configuration configuration, Class<?> ntClass, List<?> ids) {
        return OracleDeleteSqlGenerate.getInstance().getBatchDeleteByIdSql(configuration, ntClass, ids);
    }

    @Override
    public String getDeleteSql(Configuration configuration, EzDelete delete, Map<String, Object> mybatisParam) {
        return OracleDeleteSqlGenerate.getInstance().getDeleteSql(configuration, delete, mybatisParam);
    }

    @Override
    public String getDeleteSql(Configuration configuration, List<EzDelete> deletes, Map<String, Object> mybatisParam) {
        return OracleDeleteSqlGenerate.getInstance().getDeleteSql(configuration, deletes, mybatisParam);
    }
}
