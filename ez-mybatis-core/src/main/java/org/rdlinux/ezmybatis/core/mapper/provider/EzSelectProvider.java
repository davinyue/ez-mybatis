package org.rdlinux.ezmybatis.core.mapper.provider;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.annotation.SqlProviderMethod;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;

import java.util.List;
import java.util.Map;

public class EzSelectProvider {
    public static final String SELECT_BY_ID_METHOD = "selectById";
    public static final String SELECT_BY_IDS_METHOD = "selectByIds";
    public static final String SELECT_BY_SQL_METHOD = "selectBySql";
    public static final String QUERY_METHOD = "query";
    public static final String QUERY_COUNT_METHOD = "queryCount";

    @SqlProviderMethod(SELECT_BY_ID_METHOD)
    public String selectById(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Class<?> ntClass = (Class<?>) param.get(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Object id = param.get(EzMybatisConstant.MAPPER_PARAM_ID);
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        return SqlGenerateFactory.getSqlGenerate(configuration).getSelectByIdSql(configuration, paramHolder, ntClass,
                id);
    }

    @SuppressWarnings("unchecked")
    @SqlProviderMethod(SELECT_BY_IDS_METHOD)
    public String selectByIds(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Class<?> ntClass = (Class<?>) param.get(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        List<Object> ids = (List<Object>) param.get(EzMybatisConstant.MAPPER_PARAM_IDS);
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        return SqlGenerateFactory.getSqlGenerate(configuration).getSelectByIdsSql(configuration, paramHolder,
                ntClass, ids);
    }

    @SuppressWarnings("unchecked")
    @SqlProviderMethod(SELECT_BY_SQL_METHOD)
    public String selectBySql(Map<String, Object> param) {
        String sql = (String) param.get(EzMybatisConstant.MAPPER_PARAM_SQL);
        Map<String, Object> sqlParam = (Map<String, Object>) param.get(EzMybatisConstant.MAPPER_PARAM_SQLPARAM);
        param.putAll(sqlParam);
        return sql;
    }

    @SqlProviderMethod(QUERY_METHOD)
    public String query(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        EzQuery<?> query = (EzQuery<?>) param.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        return SqlGenerateFactory.getSqlGenerate(configuration).getQuerySql(configuration, paramHolder, query);
    }

    @SqlProviderMethod(QUERY_COUNT_METHOD)
    public String queryCount(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        EzQuery<?> query = (EzQuery<?>) param.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        return SqlGenerateFactory.getSqlGenerate(configuration).getQueryCountSql(configuration, paramHolder, query);
    }
}
