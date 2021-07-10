package org.rdlinux.ezmybatis.core.mapper.provider;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;

import java.util.List;

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
}
