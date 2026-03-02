package org.rdlinux.ezmybatis.core.mapper.provider;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.annotation.MethodName;
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.Collection;
import java.util.Map;

public class EzInsertProvider {
    public static final String INSERT_METHOD = "insert";
    public static final String INSERT_BY_TABLE_METHOD = "insertByTable";
    public static final String BATCH_INSERT_METHOD = "batchInsert";
    public static final String BATCH_INSERT_BY_TABLE_METHOD = "batchInsertByTable";
    public static final String INSERT_BY_SQL_METHOD = "insertBySql";
    public static final String INSERT_BY_QUERY_METHOD = "insertByQuery";

    @MethodName(INSERT_METHOD)
    public String insert(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.ofMyBatisParam(param);
        Object entity = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITY);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(sqlGenerateContext.getConfiguration()))
                .getInsertSql(sqlGenerateContext, null, entity);
    }

    @MethodName(INSERT_BY_TABLE_METHOD)
    public String insertByTable(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.ofMyBatisParam(param);
        Object entity = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITY);
        Table table = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_TABLE);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(sqlGenerateContext.getConfiguration()))
                .getInsertSql(sqlGenerateContext, table, entity);
    }

    @MethodName(BATCH_INSERT_METHOD)
    public String batchInsert(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.ofMyBatisParam(param);
        Collection<Object> entities = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITYS);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(sqlGenerateContext.getConfiguration()))
                .getBatchInsertSql(sqlGenerateContext, null, entities);
    }

    @MethodName(BATCH_INSERT_BY_TABLE_METHOD)
    public String batchInsertByTable(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.ofMyBatisParam(param);
        Collection<Object> entities = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITYS);
        Table table = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_TABLE);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(sqlGenerateContext.getConfiguration()))
                .getBatchInsertSql(sqlGenerateContext, table, entities);
    }

    @MethodName(INSERT_BY_SQL_METHOD)
    public String insertBySql(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.ofMyBatisParam(param);
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        MybatisParamHolder paramHolder = new MybatisParamHolder(configuration, param);
        String sql = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_SQL);
        Map<String, Object> sqlParam = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_SQLPARAM);
        param.putAll(sqlParam);
        return sql;
    }

    @MethodName(INSERT_BY_QUERY_METHOD)
    public String insertByQuery(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.ofMyBatisParam(param);
        EzQuery<?> query = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        Table table = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_TABLE);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(sqlGenerateContext.getConfiguration()))
                .getInsertByQuerySql(sqlGenerateContext, table, query);
    }
}
