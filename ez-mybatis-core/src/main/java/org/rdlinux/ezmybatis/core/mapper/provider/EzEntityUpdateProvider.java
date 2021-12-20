package org.rdlinux.ezmybatis.core.mapper.provider;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;

import java.util.List;
import java.util.Map;

public class EzEntityUpdateProvider {
    public String update(@Param(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION) Configuration configuration,
                         @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Object entity) {
        return SqlGenerateFactory.getSqlGenerate(configuration).getUpdateSql(configuration, entity, false);
    }

    public String batchUpdate(@Param(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION) Configuration configuration,
                              @Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) List<Object> entitys) {
        return SqlGenerateFactory.getSqlGenerate(configuration).getBatchUpdateSql(configuration, entitys,
                false);
    }

    public String replace(@Param(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION) Configuration configuration,
                          @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Object entity) {
        return SqlGenerateFactory.getSqlGenerate(configuration).getUpdateSql(configuration, entity, true);
    }

    public String batchReplace(@Param(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION) Configuration configuration,
                               @Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) List<Object> entitys) {
        return SqlGenerateFactory.getSqlGenerate(configuration).getBatchUpdateSql(configuration, entitys,
                true);
    }

    public String updateByEzUpdate(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        EzUpdate update = (EzUpdate) param.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        return SqlGenerateFactory.getSqlGenerate(configuration).getUpdateSql(configuration, update, param);
    }

    @SuppressWarnings("unchecked")
    public String batchUpdateByEzUpdate(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        List<EzUpdate> updates = (List<EzUpdate>) param.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        return SqlGenerateFactory.getSqlGenerate(configuration).getUpdateSql(configuration, updates, param);
    }

    @SuppressWarnings("unchecked")
    public String updateBySql(Map<String, Object> param) {
        String sql = (String) param.get(EzMybatisConstant.MAPPER_PARAM_SQL);
        Map<String, Object> sqlParam = (Map<String, Object>) param.get(EzMybatisConstant.MAPPER_PARAM_SQLPARAM);
        param.putAll(sqlParam);
        return sql;
    }
}
