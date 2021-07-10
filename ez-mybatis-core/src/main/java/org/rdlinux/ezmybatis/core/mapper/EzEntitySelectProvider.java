package org.rdlinux.ezmybatis.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;

import java.util.List;

public class EzEntitySelectProvider {
    public String selectById(@Param(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION) Configuration configuration,
                             @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS) Class<?> ntClass,
                             @Param(EzMybatisConstant.MAPPER_PARAM_ID) Object id) {
        return SqlGenerateFactory.getSqlGenerate().getSelectByIdSql(ntClass, id);
    }

    public String selectByIds(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS) Class<?> ntClass,
                              @Param(EzMybatisConstant.MAPPER_PARAM_IDS) List<Object> ids) {
        return SqlGenerateFactory.getSqlGenerate().getSelectByIdsSql(ntClass, ids);
    }
}
