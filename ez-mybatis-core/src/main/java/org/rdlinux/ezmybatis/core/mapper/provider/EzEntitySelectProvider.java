package org.rdlinux.ezmybatis.core.mapper.provider;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;

import java.util.List;
import java.util.Map;

public class EzEntitySelectProvider {
    public String selectById(@Param(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION) Configuration configuration,
                             @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS) Class<?> ntClass,
                             @Param(EzMybatisConstant.MAPPER_PARAM_ID) Object id) {
        return SqlGenerateFactory.getSqlGenerate(configuration).getSelectByIdSql(configuration, ntClass, id);
    }

    public String selectByIds(@Param(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION) Configuration configuration,
                              @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS) Class<?> ntClass,
                              @Param(EzMybatisConstant.MAPPER_PARAM_IDS) List<Object> ids) {
        return SqlGenerateFactory.getSqlGenerate(configuration).getSelectByIdsSql(configuration, ntClass, ids);
    }

    public String selectBySql(@Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql) {
        return sql;
    }

    public String query(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        EzQuery<?> query = (EzQuery<?>) param.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        return SqlGenerateFactory.getSqlGenerate(configuration).getQuerySql(configuration, query, param);
    }

    public String queryCount(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        EzQuery<?> query = (EzQuery<?>) param.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        return SqlGenerateFactory.getSqlGenerate(configuration).getQueryCountSql(configuration, query, param);
    }
}
