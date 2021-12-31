package org.rdlinux.ezmybatis.core.mapper;

import org.apache.ibatis.annotations.*;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.mapper.provider.EzDeleteProvider;
import org.rdlinux.ezmybatis.core.mapper.provider.EzInsertProvider;
import org.rdlinux.ezmybatis.core.mapper.provider.EzSelectProvider;
import org.rdlinux.ezmybatis.core.mapper.provider.EzUpdateProvider;

import java.util.List;
import java.util.Map;

/**
 * 通用mapper
 */
@Mapper
public interface EzMapper {
    /**
     * 根据sql插入记录
     */
    @InsertProvider(type = EzInsertProvider.class, method = EzInsertProvider.INSERT_BY_SQL_METHOD)
    Integer insertBySql(@Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql,
                        @Param(EzMybatisConstant.MAPPER_PARAM_SQLPARAM) Map<String, Object> param);

    /**
     * 根据sql查询一条数据并返回map
     */
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_SQL_METHOD)
    Map<String, Object> selectOneMapBySql(@Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql,
                                          @Param(EzMybatisConstant.MAPPER_PARAM_SQLPARAM) Map<String, Object> param);

    /**
     * 根据sql查询多条数据, 并返回list map
     */
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_SQL_METHOD)
    List<Map<String, Object>> selectMapBySql(@Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql,
                                             @Param(EzMybatisConstant.MAPPER_PARAM_SQLPARAM) Map<String, Object> param);

    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.QUERY_METHOD)
    <Rt> List<Rt> query(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzQuery<Rt> query);

    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.QUERY_METHOD)
    <Rt> Rt queryOne(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzQuery<Rt> query);

    /**
     * 根据更新参数更新
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.UPDATE_BY_EZ_UPDATE_METHOD)
    int update(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzUpdate update);

    /**
     * 根据更新参数更新
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.BATCH_UPDATE_BY_EZ_UPDATE_METHOD)
    void batchUpdate(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) List<EzUpdate> updates);

    /**
     * 根据sql更新
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.UPDATE_BY_SQL_METHOD)
    Integer updateBySql(@Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql,
                        @Param(EzMybatisConstant.MAPPER_PARAM_SQLPARAM) Map<String, Object> param);

    /**
     * 批量删除
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.DELETE_BY_EZ_DELETE_METHOD)
    int delete(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzDelete delete);

    /**
     * 批量删除
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.BATCH_DELETE_BY_EZ_DELETE_METHOD)
    void batchDelete(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) List<EzDelete> deletes);

    /**
     * 根据sql删除
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.DELETE_BY_SQL_METHOD)
    Integer deleteBySql(@Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql,
                        @Param(EzMybatisConstant.MAPPER_PARAM_SQLPARAM) Map<String, Object> param);

    /**
     * 插入, 注意, 该接口仅能插入单条实体数据, 不能传入map或collection或array
     */
    //@InsertProvider(type = EzEntityInsertProvider.class, method = "insert")
    //int insert(Object entity);
}
