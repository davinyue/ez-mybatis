package org.rdlinux.ezmybatis.core.mapper.provider;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.annotation.MethodName;
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbDialectProviderLoader;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EzSelectProvider {
    public static final String SELECT_BY_ID_METHOD = "selectById";
    public static final String SELECT_BY_TABLE_AND_ID_METHOD = "selectByTableAndId";
    public static final String SELECT_BY_IDS_METHOD = "selectByIds";
    public static final String SELECT_BY_TABLE_AND_IDS_METHOD = "selectByTableAndIds";
    public static final String SELECT_BY_SQL_METHOD = "selectBySql";
    public static final String QUERY_METHOD = "query";
    public static final String QUERY_COUNT_METHOD = "queryCount";

    /**
     * 处理in参数
     */
    private static String handleInParam(String sql, Map<String, Object> sqlParam, MybatisParamHolder paramHolder) {
        Set<String> paramKeys = sqlParam.keySet();
        for (String paramKey : paramKeys) {
            Object value = sqlParam.get(paramKey);
            if (value == null) {
                continue;
            }
            if (value instanceof Collection || value.getClass().isArray()) {
                Object[] valueArray;
                if (value instanceof Collection) {
                    valueArray = ((Collection<?>) value).toArray();
                } else {
                    valueArray = (Object[]) value;
                }
                String regex = "(?i)\\b(IN)\\s*\\(*[#$]\\s*\\{\\s*" + paramKey + "\\s*}\\s*\\)*";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(sql);
                if (matcher.find()) {
                    StringBuilder subSql = new StringBuilder(" IN (");
                    for (int i = 0; i < valueArray.length; i++) {
                        subSql.append(paramHolder.getMybatisParamName(valueArray[i]));
                        if (i + 1 < valueArray.length) {
                            subSql.append(", ");
                        }
                    }
                    subSql.append(") ");
                    sql = sql.replaceAll(regex, subSql.toString());
                }
            }
        }
        return sql;
    }

    @MethodName(SELECT_BY_ID_METHOD)
    public String selectById(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.ofMyBatisParam(param);
        Class<?> ntClass = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Object id = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ID);
        return DbDialectProviderLoader.getProvider(EzMybatisContent.getDbType(sqlGenerateContext.getConfiguration()))
                .getSqlGenerate().getSelectByIdSql(sqlGenerateContext, null, ntClass, id);
    }

    @MethodName(SELECT_BY_TABLE_AND_ID_METHOD)
    public String selectByTableAndId(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.ofMyBatisParam(param);
        Class<?> ntClass = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Table table = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_TABLE);
        Object id = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ID);
        return DbDialectProviderLoader.getProvider(EzMybatisContent.getDbType(sqlGenerateContext.getConfiguration()))
                .getSqlGenerate().getSelectByIdSql(sqlGenerateContext, table, ntClass, id);
    }

    @MethodName(SELECT_BY_IDS_METHOD)
    public String selectByIds(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.ofMyBatisParam(param);
        Class<?> ntClass = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Collection<Object> ids = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_IDS);
        return DbDialectProviderLoader.getProvider(EzMybatisContent.getDbType(sqlGenerateContext.getConfiguration()))
                .getSqlGenerate().getSelectByIdsSql(sqlGenerateContext, null, ntClass, ids);
    }

    @MethodName(SELECT_BY_TABLE_AND_IDS_METHOD)
    public String selectByTableAndIds(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.ofMyBatisParam(param);
        Class<?> ntClass = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Table table = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_TABLE);
        Collection<Object> ids = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_IDS);
        return DbDialectProviderLoader.getProvider(EzMybatisContent.getDbType(sqlGenerateContext.getConfiguration()))
                .getSqlGenerate().getSelectByIdsSql(sqlGenerateContext, table, ntClass, ids);
    }

    @MethodName(SELECT_BY_SQL_METHOD)
    public String selectBySql(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        MybatisParamHolder paramHolder = new MybatisParamHolder(configuration, param);
        String sql = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_SQL);
        Map<String, Object> sqlParam = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_SQLPARAM);
        param.putAll(sqlParam);
        sql = handleInParam(sql, sqlParam, paramHolder);
        return sql;
    }

    @MethodName(QUERY_METHOD)
    public String query(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.ofMyBatisParam(param);
        EzQuery<?> query = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        return DbDialectProviderLoader.getProvider(EzMybatisContent.getDbType(sqlGenerateContext.getConfiguration()))
                .getSqlGenerate().getQuerySql(sqlGenerateContext, query);
    }

    @MethodName(QUERY_COUNT_METHOD)
    public String queryCount(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.ofMyBatisParam(param);
        EzQuery<?> query = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        return DbDialectProviderLoader.getProvider(EzMybatisContent.getDbType(sqlGenerateContext.getConfiguration()))
                .getSqlGenerate().getQueryCountSql(sqlGenerateContext, query);
    }
}
