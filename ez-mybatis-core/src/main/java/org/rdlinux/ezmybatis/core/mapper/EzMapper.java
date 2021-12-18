package org.rdlinux.ezmybatis.core.mapper;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.mapper.provider.EzEntityDeleteProvider;
import org.rdlinux.ezmybatis.core.mapper.provider.EzEntitySelectProvider;
import org.rdlinux.ezmybatis.core.mapper.provider.EzEntityUpdateProvider;

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
    <Rt> List<Rt> query(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzQuery<Rt> query);

    @SelectProvider(type = EzEntitySelectProvider.class, method = "query")
    <Rt> Rt queryOne(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzQuery<Rt> query);

    /**
     * 根据更新参数更新
     */
    @UpdateProvider(type = EzEntityUpdateProvider.class, method = "updateByEzUpdate")
    int update(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzUpdate update);

    /**
     * 根据更新参数更新
     */
    @UpdateProvider(type = EzEntityUpdateProvider.class, method = "batchUpdateByEzUpdate")
    void batchUpdate(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) List<EzUpdate> updates);

    /**
     * 批量删除
     */
    @DeleteProvider(type = EzEntityDeleteProvider.class, method = "deleteByEzDelete")
    int delete(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzDelete delete);

    /**
     * 批量删除
     */
    @DeleteProvider(type = EzEntityDeleteProvider.class, method = "batchDeleteByEzDelete")
    int batchDelete(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) List<EzDelete> deletes);
}
