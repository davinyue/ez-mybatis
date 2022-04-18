package org.rdlinux.ezmybatis.core.mapper.provider;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.annotation.MethodName;
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;
import org.rdlinux.ezmybatis.utils.DbTypeUtils;

import java.util.Collection;
import java.util.Map;

public class EzInsertProvider {
    public static final String INSERT_METHOD = "insert";
    public static final String BATCH_INSERT_METHOD = "batchInsert";
    public static final String INSERT_BY_SQL_METHOD = "insertBySql";

    @MethodName(INSERT_METHOD)
    public String insert(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Object entity = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITY);
        return SqlGenerateFactory.getSqlGenerate(DbTypeUtils.getDbType(configuration))
                .getInsertSql(configuration, paramHolder, entity);
    }

    @MethodName(BATCH_INSERT_METHOD)
    public String batchInsert(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Collection<Object> entitys = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITYS);
        return SqlGenerateFactory.getSqlGenerate(DbTypeUtils.getDbType(configuration))
                .getBatchInsertSql(configuration, paramHolder, entitys);
    }

    @MethodName(INSERT_BY_SQL_METHOD)
    public String insertBySql(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        String sql = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_SQL);
        Map<String, Object> sqlParam = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_SQLPARAM);
        param.putAll(sqlParam);
        return sql;
    }
}
