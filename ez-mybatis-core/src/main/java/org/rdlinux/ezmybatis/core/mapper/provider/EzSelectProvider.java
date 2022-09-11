package org.rdlinux.ezmybatis.core.mapper.provider;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.annotation.MethodName;
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.DbTypeUtils;

import java.util.Collection;
import java.util.Map;

public class EzSelectProvider {
    public static final String SELECT_BY_ID_METHOD = "selectById";
    public static final String SELECT_BY_TABLE_AND_ID_METHOD = "selectByTableAndId";
    public static final String SELECT_BY_IDS_METHOD = "selectByIds";
    public static final String SELECT_BY_TABLE_AND_IDS_METHOD = "selectByTableAndIds";
    public static final String SELECT_BY_SQL_METHOD = "selectBySql";
    public static final String QUERY_METHOD = "query";
    public static final String QUERY_COUNT_METHOD = "queryCount";

    @MethodName(SELECT_BY_ID_METHOD)
    public String selectById(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Class<?> ntClass = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Object id = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ID);
        return SqlGenerateFactory.getSqlGenerate(DbTypeUtils.getDbType(configuration))
                .getSelectByIdSql(configuration, paramHolder, null, ntClass, id);
    }

    @MethodName(SELECT_BY_TABLE_AND_ID_METHOD)
    public String selectByTableAndId(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Class<?> ntClass = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Table table = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_TABLE);
        Object id = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ID);
        return SqlGenerateFactory.getSqlGenerate(DbTypeUtils.getDbType(configuration))
                .getSelectByIdSql(configuration, paramHolder, table, ntClass, id);
    }

    @MethodName(SELECT_BY_IDS_METHOD)
    public String selectByIds(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Class<?> ntClass = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Collection<Object> ids = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_IDS);
        return SqlGenerateFactory.getSqlGenerate(DbTypeUtils.getDbType(configuration))
                .getSelectByIdsSql(configuration, paramHolder, null, ntClass, ids);
    }

    @MethodName(SELECT_BY_TABLE_AND_IDS_METHOD)
    public String selectByTableAndIds(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Class<?> ntClass = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Table table = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_TABLE);
        Collection<Object> ids = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_IDS);
        return SqlGenerateFactory.getSqlGenerate(DbTypeUtils.getDbType(configuration))
                .getSelectByIdsSql(configuration, paramHolder, table, ntClass, ids);
    }

    @MethodName(SELECT_BY_SQL_METHOD)
    public String selectBySql(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        String sql = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_SQL);
        Map<String, Object> sqlParam = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_SQLPARAM);
        param.putAll(sqlParam);
        return sql;
    }

    @MethodName(QUERY_METHOD)
    public String query(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        EzQuery<?> query = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        return SqlGenerateFactory.getSqlGenerate(DbTypeUtils.getDbType(configuration))
                .getQuerySql(configuration, paramHolder, query);
    }

    @MethodName(QUERY_COUNT_METHOD)
    public String queryCount(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        EzQuery<?> query = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        return SqlGenerateFactory.getSqlGenerate(DbTypeUtils.getDbType(configuration))
                .getQueryCountSql(configuration, paramHolder, query);
    }
}
