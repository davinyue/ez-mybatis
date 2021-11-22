package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerate;

import java.util.List;
import java.util.Map;

public class OracleSqlGenerate implements SqlGenerate {
    private static volatile OracleSqlGenerate instance;

    private OracleSqlGenerate() {
    }

    public static OracleSqlGenerate getInstance() {
        if (instance == null) {
            synchronized ( OracleSqlGenerate.class ) {
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
    public String getQuerySql(Configuration configuration, Class<?> ntClass, EzQuery query,
                              Map<String, Object> mybatisParam) {
        return OracleSelectSqlGenerate.getInstance().getQuerySql(configuration, ntClass, query, mybatisParam);
    }

    @Override
    public String getQueryCountSql(Configuration configuration, Class<?> ntClass, EzQuery query,
                                   Map<String, Object> mybatisParam) {
        return OracleSelectSqlGenerate.getInstance().getQueryCountSql(configuration, ntClass, query, mybatisParam);
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
    public String getDeleteByIdSql(Configuration configuration, Class<?> ntClass, Object id) {
        return OracleDeleteSqlGenerate.getInstance().getDeleteByIdSql(configuration, ntClass, id);
    }

    @Override
    public String getBatchDeleteByIdSql(Configuration configuration, Class<?> ntClass, List<?> ids) {
        return OracleDeleteSqlGenerate.getInstance().getBatchDeleteByIdSql(configuration, ntClass, ids);
    }
}
