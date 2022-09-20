package org.rdlinux.ezmybatis.core.mapper;

import org.apache.ibatis.annotations.*;
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.mapper.provider.EzDeleteProvider;
import org.rdlinux.ezmybatis.core.mapper.provider.EzInsertProvider;
import org.rdlinux.ezmybatis.core.mapper.provider.EzSelectProvider;
import org.rdlinux.ezmybatis.core.mapper.provider.EzUpdateProvider;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 基础mapper
 *
 * @param <Nt> 实体类型
 * @param <Pt> 主键类型
 */
public interface EzBaseMapper<Nt, Pt extends Serializable> {
    /**
     * 插入, 注意, 该接口仅能插入单条实体数据, 不能传入map或collection或arra
     */
    @InsertProvider(type = EzInsertProvider.class, method = EzInsertProvider.INSERT_METHOD)
    int insert(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Nt entity);

    /**
     * 插入, 指定表, 注意, 该接口仅能插入单条实体数据, 不能传入map或collection或array
     */
    @InsertProvider(type = EzInsertProvider.class, method = EzInsertProvider.INSERT_BY_TABLE_METHOD)
    int insertByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                      @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Nt entity);

    /**
     * 批量插入
     */
    @InsertProvider(type = EzInsertProvider.class, method = EzInsertProvider.BATCH_INSERT_METHOD)
    int batchInsert(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<Nt> entitys);

    /**
     * 批量插入, 指定表
     */
    @InsertProvider(type = EzInsertProvider.class, method = EzInsertProvider.BATCH_INSERT_BY_TABLE_METHOD)
    int batchInsertByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                           @Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<Nt> entitys);

    /**
     * 更新, 只更新非空字段
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.UPDATE_METHOD)
    int update(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Nt entity);

    /**
     * 更新, 只更新非空字段
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.UPDATE_BY_TABLE_METHOD)
    int updateByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                      @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Nt entity);

    /**
     * 批量更新, 只更新非空字段
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.BATCH_UPDATE_METHOD)
    int batchUpdate(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<Nt> entitys);

    /**
     * 批量更新, 只更新非空字段
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.BATCH_UPDATE_BY_TABLE_METHOD)
    int batchUpdateByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                           @Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<Nt> entitys);

    /**
     * 更新, 更新所有字段
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.REPLACE_METHOD)
    int replace(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Nt entity);

    /**
     * 更新, 更新所有字段
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.REPLACE_METHOD_BY_TABLE)
    int replaceByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                       @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Nt entity);

    /**
     * 批量更新, 更新所有字段
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.BATCH_REPLACE_METHOD)
    int batchReplace(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<Nt> entitys);

    /**
     * 批量更新, 更新所有字段
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.BATCH_REPLACE_BY_TABLE_METHOD)
    int batchReplaceByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                            @Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<Nt> entitys);

    /**
     * 删除
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.DELETE_METHOD)
    int delete(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Nt entity);

    /**
     * 删除
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.DELETE_BY_TABLE_METHOD)
    int deleteByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                      @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Nt entity);

    /**
     * 批量删除
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.BATCH_DELETE_METHOD)
    int batchDelete(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<Nt> entitys);

    /**
     * 批量删除
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.BATCH_DELETE_BY_TABLE_METHOD)
    int batchDeleteByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                           @Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<Nt> entitys);

    /**
     * 根据主键删除
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.DELETE_BY_ID_METHOD)
    int deleteById(@Param(EzMybatisConstant.MAPPER_PARAM_ID) Pt id);

    /**
     * 根据主键删除
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.DELETE_BY_TABLE_AND_ID_METHOD)
    int deleteByTableAndId(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                           @Param(EzMybatisConstant.MAPPER_PARAM_ID) Pt id);

    /**
     * 根据主键批量删除
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.BATCH_DELETE_BY_ID_METHOD)
    int batchDeleteById(@Param(EzMybatisConstant.MAPPER_PARAM_IDS) Collection<Pt> ids);

    /**
     * 根据主键批量删除
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.BATCH_DELETE_BY_TABLE_AND_ID_METHOD)
    int batchDeleteByTableAndId(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                                @Param(EzMybatisConstant.MAPPER_PARAM_IDS) Collection<Pt> ids);

    /**
     * 根据主键查询
     */
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_ID_METHOD)
    Nt selectById(@Param(EzMybatisConstant.MAPPER_PARAM_ID) Pt id);

    /**
     * 根据主键查询
     */
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_TABLE_AND_ID_METHOD)
    Nt selectByTableAndId(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                          @Param(EzMybatisConstant.MAPPER_PARAM_ID) Pt id);

    /**
     * 根据主键批量查询
     */
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_IDS_METHOD)
    List<Nt> selectByIds(@Param(EzMybatisConstant.MAPPER_PARAM_IDS) Collection<Pt> ids);

    /**
     * 根据主键批量查询
     */
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_TABLE_AND_IDS_METHOD)
    List<Nt> selectByTableAndIds(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                                 @Param(EzMybatisConstant.MAPPER_PARAM_IDS) Collection<Pt> ids);

    /**
     * 根据sql查询一条数据
     */
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_SQL_METHOD)
    Nt selectOneBySql(@Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql,
                      @Param(EzMybatisConstant.MAPPER_PARAM_SQLPARAM) Map<String, Object> param);

    /**
     * 根据sql查询多条数据
     */
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_SQL_METHOD)
    List<Nt> selectBySql(@Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql,
                         @Param(EzMybatisConstant.MAPPER_PARAM_SQLPARAM) Map<String, Object> param);

    /**
     * 据ezQuery查询数据
     */
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.QUERY_METHOD)
    List<Nt> query(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzQuery<Nt> query);


    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.QUERY_METHOD)
    Nt queryOne(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzQuery<Nt> query);

    /**
     * 根据ezQuery查询count
     */
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.QUERY_COUNT_METHOD)
    int queryCount(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzQuery<Nt> query);
}
