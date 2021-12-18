package org.rdlinux.ezmybatis.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.mapper.provider.EzEntitySelectProvider;

import java.util.List;
import java.util.Map;

/**
 * 通用mapper
 */
public interface EzMapper {
    /**
     * 根据sql查询一条数据并返回map
     */
    @SelectProvider(type = EzEntitySelectProvider.class, method = "selectBySql")
    Map<String, Object> selectOneMapBySql(@Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql);

    /**
     * 根据sql查询多条数据, 并返回list map
     */
    @SelectProvider(type = EzEntitySelectProvider.class, method = "selectBySql")
    List<Map<String, Object>> selectMapBySql(@Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql);

    @SelectProvider(type = EzEntitySelectProvider.class, method = "query")
    <Rt> List<Rt> query(@Param(EzMybatisConstant.MAPPER_PARAM_QUERY) EzQuery<Rt> query);

    @SelectProvider(type = EzEntitySelectProvider.class, method = "query")
    <Rt> Rt queryOne(@Param(EzMybatisConstant.MAPPER_PARAM_QUERY) EzQuery<Rt> query);
}
