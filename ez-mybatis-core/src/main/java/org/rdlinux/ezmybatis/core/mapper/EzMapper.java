package org.rdlinux.ezmybatis.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.mapper.provider.EzEntitySelectProvider;

import java.util.List;

/**
 * 通用mapper
 */
public interface EzMapper {
    @SelectProvider(type = EzEntitySelectProvider.class, method = "query")
    <Rt> List<Rt> query(@Param(EzMybatisConstant.MAPPER_PARAM_QUERY) EzQuery<Rt> query);

    @SelectProvider(type = EzEntitySelectProvider.class, method = "query")
    <Rt> Rt queryOne(@Param(EzMybatisConstant.MAPPER_PARAM_QUERY) EzQuery<Rt> query);
}
