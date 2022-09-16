package org.rdlinux.ezmybatis.core.mapper.provider;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.annotation.MethodName;
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.Collection;
import java.util.Map;

public class EzUpdateProvider {
    public static final String UPDATE_METHOD = "update";
    public static final String UPDATE_BY_TABLE_METHOD = "updateByTable";
    public static final String BATCH_UPDATE_METHOD = "batchUpdate";
    public static final String BATCH_UPDATE_BY_TABLE_METHOD = "batchUpdateByTable";
    public static final String REPLACE_METHOD = "replace";
    public static final String REPLACE_METHOD_BY_TABLE = "replaceByTable";
    public static final String BATCH_REPLACE_METHOD = "batchReplace";
    public static final String BATCH_REPLACE_BY_TABLE_METHOD = "batchReplaceByTable";
    public static final String UPDATE_BY_EZ_UPDATE_METHOD = "updateByEzUpdate";
    public static final String BATCH_UPDATE_BY_EZ_UPDATE_METHOD = "batchUpdateByEzUpdate";
    public static final String UPDATE_BY_SQL_METHOD = "updateBySql";

    @MethodName(UPDATE_METHOD)
    public String update(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Object entity = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITY);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                .getUpdateSql(configuration, paramHolder, null, entity, false);
    }

    @MethodName(UPDATE_BY_TABLE_METHOD)
    public String updateByTable(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Object entity = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITY);
        Table table = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_TABLE);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                .getUpdateSql(configuration, paramHolder, table, entity, false);
    }


    @MethodName(BATCH_UPDATE_METHOD)
    public String batchUpdate(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Collection<Object> entitys = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITYS);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                .getBatchUpdateSql(configuration, paramHolder, null, entitys, false);
    }

    @MethodName(BATCH_UPDATE_BY_TABLE_METHOD)
    public String batchUpdateByTable(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Collection<Object> entitys = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITYS);
        Table table = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_TABLE);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                .getBatchUpdateSql(configuration, paramHolder, table, entitys, false);
    }

    @MethodName(REPLACE_METHOD)
    public String replace(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Object entity = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITY);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                .getUpdateSql(configuration, paramHolder, null, entity, true);
    }

    @MethodName(REPLACE_METHOD_BY_TABLE)
    public String replaceByTable(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Object entity = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITY);
        Table table = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_TABLE);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                .getUpdateSql(configuration, paramHolder, table, entity, true);
    }


    @MethodName(BATCH_REPLACE_METHOD)
    public String batchReplace(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Collection<Object> entitys = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITYS);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                .getBatchUpdateSql(configuration, paramHolder, null, entitys, true);
    }

    @MethodName(BATCH_REPLACE_BY_TABLE_METHOD)
    public String batchReplaceByTable(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Collection<Object> entitys = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITYS);
        Table table = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_TABLE);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                .getBatchUpdateSql(configuration, paramHolder, table, entitys, true);
    }

    @MethodName(UPDATE_BY_EZ_UPDATE_METHOD)
    public String updateByEzUpdate(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        EzUpdate update = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                .getUpdateSql(configuration, paramHolder, update);
    }

    @MethodName(BATCH_UPDATE_BY_EZ_UPDATE_METHOD)
    public String batchUpdateByEzUpdate(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Collection<EzUpdate> updates = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
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
