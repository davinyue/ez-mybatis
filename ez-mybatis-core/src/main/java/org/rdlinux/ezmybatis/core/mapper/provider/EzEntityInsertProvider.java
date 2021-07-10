package org.rdlinux.ezmybatis.core.mapper.provider;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;

import java.util.List;

public class EzEntityInsertProvider {
    public String insert(@Param(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION) Configuration configuration,
                         @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS) Class<?> ntClass,
                         @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Object entity) {
        return SqlGenerateFactory.getSqlGenerate().getInsertSql(configuration, entity);
    }

    public String batchInsert(@Param(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION) Configuration configuration,
                              @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS) Class<?> ntClass,
                              @Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) List<Object> entitys) {
        return SqlGenerateFactory.getSqlGenerate().getBatchInsertSql(configuration, entitys);
    }
}
