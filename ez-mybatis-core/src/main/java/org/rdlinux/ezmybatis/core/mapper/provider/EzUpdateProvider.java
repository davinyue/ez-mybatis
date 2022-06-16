package org.rdlinux.ezmybatis.core.mapper.provider;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.annotation.MethodName;
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;
import org.rdlinux.ezmybatis.utils.DbTypeUtils;

import java.util.Collection;
import java.util.Map;

public class EzUpdateProvider {
    public static final String UPDATE_METHOD = "update";
    public static final String BATCH_UPDATE_METHOD = "batchUpdate";
    public static final String REPLACE_METHOD = "replace";
    public static final String BATCH_REPLACE_METHOD = "batchReplace";
    public static final String UPDATE_BY_EZ_UPDATE_METHOD = "updateByEzUpdate";
    public static final String BATCH_UPDATE_BY_EZ_UPDATE_METHOD = "batchUpdateByEzUpdate";
    public static final String UPDATE_BY_SQL_METHOD = "updateBySql";

    @MethodName(UPDATE_METHOD)
    public String update(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Object entity = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITY);
        return SqlGenerateFactory.getSqlGenerate(DbTypeUtils.getDbType(configuration))
                .getUpdateSql(configuration, paramHolder, entity, false);
    }


    @MethodName(BATCH_UPDATE_METHOD)
    public String batchUpdate(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Collection<Object> entitys = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITYS);
        return SqlGenerateFactory.getSqlGenerate(DbTypeUtils.getDbType(configuration))
                .getBatchUpdateSql(configuration, paramHolder, entitys, false);
    }

    @MethodName(REPLACE_METHOD)
    public String replace(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Object entity = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITY);
        return SqlGenerateFactory.getSqlGenerate(DbTypeUtils.getDbType(configuration))
                .getUpdateSql(configuration, paramHolder, entity, true);
    }

    @MethodName(BATCH_REPLACE_METHOD)
    public String batchReplace(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Collection<Object> entitys = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITYS);
        return SqlGenerateFactory.getSqlGenerate(DbTypeUtils.getDbType(configuration))
                .getBatchUpdateSql(configuration, paramHolder, entitys, true);
    }

    @MethodName(UPDATE_BY_EZ_UPDATE_METHOD)
    public String updateByEzUpdate(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        EzUpdate update = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        return SqlGenerateFactory.getSqlGenerate(DbTypeUtils.getDbType(configuration))
                .getUpdateSql(configuration, paramHolder, update);
    }

    @MethodName(BATCH_UPDATE_BY_EZ_UPDATE_METHOD)
    public String batchUpdateByEzUpdate(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Collection<EzUpdate> updates = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        return SqlGenerateFactory.getSqlGenerate(DbTypeUtils.getDbType(configuration))
                .getUpdateSql(configuration, paramHolder, updates);
    }

    @MethodName(UPDATE_BY_SQL_METHOD)
    public String updateBySql(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        String sql = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_SQL);
        Map<String, Object> sqlParam = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_SQLPARAM);
        param.putAll(sqlParam);
        return sql;
    }
}
