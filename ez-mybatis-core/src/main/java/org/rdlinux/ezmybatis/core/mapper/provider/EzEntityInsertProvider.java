package org.rdlinux.ezmybatis.core.mapper.provider;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;

import java.util.List;
import java.util.Map;

public class EzEntityInsertProvider {
    public String insert(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Object entity = param.get(EzMybatisConstant.MAPPER_PARAM_ENTITY);
        MybatisParamHolder mybatisParamHolder = new MybatisParamHolder(param);
        return SqlGenerateFactory.getSqlGenerate(configuration).getInsertSql(configuration, mybatisParamHolder, entity);
    }

    @SuppressWarnings("unchecked")
    public String batchInsert(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        List<Object> entitys = (List<Object>) param.get(EzMybatisConstant.MAPPER_PARAM_ENTITYS);
        MybatisParamHolder mybatisParamHolder = new MybatisParamHolder(param);
        return SqlGenerateFactory.getSqlGenerate(configuration).getBatchInsertSql(configuration, mybatisParamHolder,
                entitys);
    }

    @SuppressWarnings("unchecked")
    public String insertBySql(Map<String, Object> param) {
        String sql = (String) param.get(EzMybatisConstant.MAPPER_PARAM_SQL);
        Map<String, Object> sqlParam = (Map<String, Object>) param.get(EzMybatisConstant.MAPPER_PARAM_SQLPARAM);
        param.putAll(sqlParam);
        return sql;
    }
}
