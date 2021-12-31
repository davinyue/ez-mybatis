package org.rdlinux.ezmybatis.core.mapper.provider;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.annotation.SqlProviderMethod;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;

import java.util.List;
import java.util.Map;

public class EzUpdateProvider {
    public static final String UPDATE_METHOD = "update";
    public static final String BATCH_UPDATE_METHOD = "batchUpdate";
    public static final String REPLACE_METHOD = "replace";
    public static final String BATCH_REPLACE_METHOD = "batchReplace";
    public static final String UPDATE_BY_EZ_UPDATE_METHOD = "updateByEzUpdate";
    public static final String BATCH_UPDATE_BY_EZ_UPDATE_METHOD = "batchUpdateByEzUpdate";
    public static final String UPDATE_BY_SQL_METHOD = "updateBySql";

    @SqlProviderMethod(UPDATE_METHOD)
    public String update(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Object entity = param.get(EzMybatisConstant.MAPPER_PARAM_ENTITY);
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        return SqlGenerateFactory.getSqlGenerate(configuration).getUpdateSql(configuration, paramHolder, entity,
                false);
    }

    @SuppressWarnings("unchecked")
    @SqlProviderMethod(BATCH_UPDATE_METHOD)
    public String batchUpdate(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        List<Object> entitys = (List<Object>) param.get(EzMybatisConstant.MAPPER_PARAM_ENTITYS);
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        return SqlGenerateFactory.getSqlGenerate(configuration).getBatchUpdateSql(configuration, paramHolder, entitys,
                false);
    }

    @SqlProviderMethod(REPLACE_METHOD)
    public String replace(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Object entity = param.get(EzMybatisConstant.MAPPER_PARAM_ENTITY);
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        return SqlGenerateFactory.getSqlGenerate(configuration).getUpdateSql(configuration, paramHolder, entity,
                true);
    }

    @SuppressWarnings("unchecked")
    @SqlProviderMethod(BATCH_REPLACE_METHOD)
    public String batchReplace(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        List<Object> entitys = (List<Object>) param.get(EzMybatisConstant.MAPPER_PARAM_ENTITYS);
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        return SqlGenerateFactory.getSqlGenerate(configuration).getBatchUpdateSql(configuration, paramHolder, entitys,
                true);
    }

    @SqlProviderMethod(UPDATE_BY_EZ_UPDATE_METHOD)
    public String updateByEzUpdate(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        EzUpdate update = (EzUpdate) param.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        return SqlGenerateFactory.getSqlGenerate(configuration).getUpdateSql(configuration, paramHolder, update);
    }

    @SuppressWarnings("unchecked")
    @SqlProviderMethod(BATCH_UPDATE_BY_EZ_UPDATE_METHOD)
    public String batchUpdateByEzUpdate(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        List<EzUpdate> updates = (List<EzUpdate>) param.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        return SqlGenerateFactory.getSqlGenerate(configuration).getUpdateSql(configuration, paramHolder, updates);
    }

    @SuppressWarnings("unchecked")
    @SqlProviderMethod(UPDATE_BY_SQL_METHOD)
    public String updateBySql(Map<String, Object> param) {
        String sql = (String) param.get(EzMybatisConstant.MAPPER_PARAM_SQL);
        Map<String, Object> sqlParam = (Map<String, Object>) param.get(EzMybatisConstant.MAPPER_PARAM_SQLPARAM);
        param.putAll(sqlParam);
        return sql;
    }
}
