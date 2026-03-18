package org.rdlinux.ezmybatis.core.mapper.provider;

import org.rdlinux.ezmybatis.annotation.MethodName;
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbDialectProviderLoader;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlExpand;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
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
    public static final String EXPAND_UPDATE_METHOD = "expandUpdate";

    @MethodName(UPDATE_METHOD)
    public String update(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.ofMyBatisParam(param);
        Object entity = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITY);
        return DbDialectProviderLoader.getProvider(EzMybatisContent.getDbType(sqlGenerateContext.getConfiguration()))
                .getSqlGenerate().getUpdateSql(sqlGenerateContext, null, entity, false);
    }

    @MethodName(UPDATE_BY_TABLE_METHOD)
    public String updateByTable(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.ofMyBatisParam(param);
        Object entity = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITY);
        Table table = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_TABLE);
        return DbDialectProviderLoader.getProvider(EzMybatisContent.getDbType(sqlGenerateContext.getConfiguration()))
                .getSqlGenerate().getUpdateSql(sqlGenerateContext, table, entity, false);
    }


    @MethodName(BATCH_UPDATE_METHOD)
    public String batchUpdate(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.ofMyBatisParam(param);
        Collection<Object> entities = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITYS);
        return DbDialectProviderLoader.getProvider(EzMybatisContent.getDbType(sqlGenerateContext.getConfiguration()))
                .getSqlGenerate().getBatchUpdateSql(sqlGenerateContext, null, entities, false);
    }

    @MethodName(BATCH_UPDATE_BY_TABLE_METHOD)
    public String batchUpdateByTable(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.ofMyBatisParam(param);
        Collection<Object> entities = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITYS);
        Table table = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_TABLE);
        return DbDialectProviderLoader.getProvider(EzMybatisContent.getDbType(sqlGenerateContext.getConfiguration()))
                .getSqlGenerate().getBatchUpdateSql(sqlGenerateContext, table, entities, false);
    }

    @MethodName(REPLACE_METHOD)
    public String replace(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.ofMyBatisParam(param);
        Object entity = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITY);
        return DbDialectProviderLoader.getProvider(EzMybatisContent.getDbType(sqlGenerateContext.getConfiguration()))
                .getSqlGenerate().getUpdateSql(sqlGenerateContext, null, entity, true);
    }

    @MethodName(REPLACE_METHOD_BY_TABLE)
    public String replaceByTable(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.ofMyBatisParam(param);
        Object entity = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITY);
        Table table = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_TABLE);
        return DbDialectProviderLoader.getProvider(EzMybatisContent.getDbType(sqlGenerateContext.getConfiguration()))
                .getSqlGenerate().getUpdateSql(sqlGenerateContext, table, entity, true);
    }


    @MethodName(BATCH_REPLACE_METHOD)
    public String batchReplace(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.ofMyBatisParam(param);
        Collection<Object> entities = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITYS);
        return DbDialectProviderLoader.getProvider(EzMybatisContent.getDbType(sqlGenerateContext.getConfiguration()))
                .getSqlGenerate().getBatchUpdateSql(sqlGenerateContext, null, entities, true);
    }

    @MethodName(BATCH_REPLACE_BY_TABLE_METHOD)
    public String batchReplaceByTable(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.ofMyBatisParam(param);
        Collection<Object> entities = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITYS);
        Table table = sqlGenerateContext.getMybatisParamHolder().get(EzMybatisConstant.MAPPER_PARAM_TABLE);
        return DbDialectProviderLoader.getProvider(EzMybatisContent.getDbType(sqlGenerateContext.getConfiguration()))
                .getSqlGenerate().getBatchUpdateSql(sqlGenerateContext, table, entities, true);
    }

    @MethodName(UPDATE_BY_EZ_UPDATE_METHOD)
    public String updateByEzUpdate(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.ofMyBatisParam(param);
        EzUpdate update = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        return DbDialectProviderLoader.getProvider(EzMybatisContent.getDbType(sqlGenerateContext.getConfiguration()))
                .getSqlGenerate().getUpdateSql(sqlGenerateContext, update);
    }

    @MethodName(BATCH_UPDATE_BY_EZ_UPDATE_METHOD)
    public String batchUpdateByEzUpdate(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.ofMyBatisParam(param);
        Collection<EzUpdate> updates = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        return DbDialectProviderLoader.getProvider(EzMybatisContent.getDbType(sqlGenerateContext.getConfiguration()))
                .getSqlGenerate().getUpdateSql(sqlGenerateContext, updates);
    }

    @MethodName(UPDATE_BY_SQL_METHOD)
    public String updateBySql(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.ofMyBatisParam(param);
        String sql = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_SQL);
        Map<String, Object> sqlParam = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_SQLPARAM);
        param.putAll(sqlParam);
        return sql;
    }

    @MethodName(EXPAND_UPDATE_METHOD)
    public String expandUpdate(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.ofMyBatisParam(param);
        SqlExpand expand = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_UPDATE_EXPAND);
        Converter<? extends SqlExpand> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(),
                expand.getClass());
        converter.buildSql(Converter.Type.UPDATE, expand, sqlGenerateContext);
        return sqlGenerateContext.getSqlBuilder().toString();
    }
}
